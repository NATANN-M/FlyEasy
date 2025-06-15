package com.example.flyeasy.ui.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color; // Make sure Color is imported
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils; // For TextUtils.isEmpty
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flyeasy.R; // Assuming you have string resources like R.string.data_not_available
import com.example.flyeasy.databinding.FragmentTicketBinding;
import com.example.flyeasy.model.TicketEntity;
import com.example.flyeasy.viewmodel.TicketViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix; // Correct import for BitMatrix
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class TicketFragment extends Fragment {

    private FragmentTicketBinding binding;
    private final String NOT_AVAILABLE = "N/A";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TicketViewModel ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);


        binding.shareTicketBtn.setOnClickListener(v -> {
            saveAndShareTicket(binding.getRoot());
        });

        binding.downloadPdfBtn.setOnClickListener(v -> {
            createPdfFromTicketView(binding.ticketCard); // Pass the view you want to capture
        });


        Bundle arguments = getArguments();
        String flight = null;
        String airline = null;
        String from = null;
        String to = null;
        String bookingDate = null;
        String passenger = null;
        String seat = null;
        String email = null;
        String bookingId = null;
        String phone = null;
        String passportNumber = null;
        double flightPrice = 0;
        String flightDate = null;
        String flightTime = null;
        if (arguments != null) {
            // Retrieve all data from the bundle
            flight = arguments.getString("flight", NOT_AVAILABLE);
            airline = arguments.getString("airline", NOT_AVAILABLE);
            from = arguments.getString("from", NOT_AVAILABLE);
            to = arguments.getString("to", NOT_AVAILABLE);
            bookingDate = arguments.getString("bookingDate", NOT_AVAILABLE);
            passenger = arguments.getString("fullName", NOT_AVAILABLE);
            seat = arguments.getString("seat", NOT_AVAILABLE);
            email = arguments.getString("email", NOT_AVAILABLE);
            bookingId = arguments.getString("bookingId", NOT_AVAILABLE);
            phone = arguments.getString("phone", NOT_AVAILABLE);
            passportNumber = arguments.getString("passportNumber", NOT_AVAILABLE);
            flightPrice = arguments.getDouble("flightPriceAtBooking", 0.0);
            String flightDepartureTime = arguments.getString("flightDepartureTime", NOT_AVAILABLE);

            // Parse flight departure date and time
            flightDate = NOT_AVAILABLE;
            flightTime = NOT_AVAILABLE;
            if (!flightDepartureTime.equals(NOT_AVAILABLE)) {
                String[] parts = flightDepartureTime.split(" ");
                if (parts.length >= 2) {
                    flightDate = parts[0];
                    flightTime = parts[1];
                } else {
                    flightDate = flightDepartureTime; // Fallback if time isn't present
                }
            }

            // Set data to views
            String flightDisplay = "Flight: " + flight;
            if (!airline.equals(NOT_AVAILABLE)) {
                flightDisplay += " (" + airline + ")";
            }
            binding.flightInfo.setText(flightDisplay);

            binding.fromTo.setText("From: " + from + " → To: " + to);

            String dateTimeDisplay = "Date: " + flightDate;
            if (!flightTime.equals(NOT_AVAILABLE)) {
                dateTimeDisplay += " | Time: " + flightTime;
            }
            binding.dateTime.setText(dateTimeDisplay);

            binding.passengerName.setText("Passenger: " + passenger);
            binding.seatNumber.setText("Seat: " + seat);
            binding.bookingId.setText("Booking ID: " + bookingId);

            // Display contact details
            binding.phoneText.setText("Phone: " + phone);
            binding.passportText.setText("Passport: " + passportNumber);
            binding.emailText.setText("Email: " + email);
            binding.bookingDateText.setText("Booked on: " + bookingDate);
            binding.priceText.setText(String.format("Price: $%.2f", flightPrice));

            // QR Code content
            StringBuilder qrBuilder = new StringBuilder();
            appendFieldToQr(qrBuilder, "Flight", flight);
            appendFieldToQr(qrBuilder, "Airline", airline);
            appendFieldToQr(qrBuilder, "From", from);
            appendFieldToQr(qrBuilder, "To", to);
            appendFieldToQr(qrBuilder, "Departure Date", flightDate);
            appendFieldToQr(qrBuilder, "Departure Time", flightTime);
            appendFieldToQr(qrBuilder, "Passenger", passenger);
            appendFieldToQr(qrBuilder, "Seat", seat);
            appendFieldToQr(qrBuilder, "Email", email);
            appendFieldToQr(qrBuilder, "Phone", phone);
            appendFieldToQr(qrBuilder, "Passport", passportNumber);
            appendFieldToQr(qrBuilder, "Booking ID", bookingId);
            appendFieldToQr(qrBuilder, "Booking Date", bookingDate);
            appendFieldToQr(qrBuilder, "Price", String.format("%.2f", flightPrice));

            if (qrBuilder.length() > 0) {
                generateQRCode(qrBuilder.toString());
            } else {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "No data available for QR code", Toast.LENGTH_SHORT).show();
                }
                binding.qrCodeImage.setVisibility(View.GONE);
            }


        }

        TicketEntity ticket = new TicketEntity();
        ticket.flight = flight;
        ticket.airline = airline;
        ticket.from = from;
        ticket.to = to;
        ticket.bookingDate = bookingDate;
        ticket.passenger = passenger;
        ticket.seat = seat;
        ticket.email = email;
        ticket.bookingId = bookingId;
        ticket.phone = phone;
        ticket.passportNumber = passportNumber;
        ticket.flightPrice = flightPrice;
        ticket.flightDate = flightDate;
        ticket.flightTime = flightTime;

        ticketViewModel.insertTicket(ticket);

    }

    /**
     * Appends a field to the QR content string builder only if the value is not "N/A" or empty.
     * @param builder StringBuilder to append to.
     * @param label The label for the field.
     * @param value The value of the field.
     */
    private void appendFieldToQr(StringBuilder builder, String label, String value) {
        if (!TextUtils.isEmpty(value) && !value.equals(NOT_AVAILABLE)) {
            if (builder.length() > 0) {
                builder.append("\n"); // Add a newline before the next field if builder is not empty
            }
            builder.append(label).append(": ").append(value);
        }
    }

    private void generateQRCode(String text) {
        if (TextUtils.isEmpty(text)) {
            binding.qrCodeImage.setVisibility(View.GONE); // Hide if no text
            if (getContext() != null) {
                Toast.makeText(getContext(), "QR data is empty", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        QRCodeWriter writer = new QRCodeWriter();
        try {
            int size = 200;
            if (size <= 0) size = 512; // Fallback size if resource is not found or invalid

            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565); // RGB_565 is more memory efficient

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            binding.qrCodeImage.setImageBitmap(bitmap);
            binding.qrCodeImage.setVisibility(View.VISIBLE);

        } catch (WriterException e) {
            e.printStackTrace();
            if (getContext() != null) {

                Toast.makeText(getContext(), "NO QR CODE GENERATED ", Toast.LENGTH_SHORT).show();
            }
            binding.qrCodeImage.setVisibility(View.GONE); // Hide on failure
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            if (getContext() != null) {
                Toast.makeText(getContext(), "QR size resource not found, using default.", Toast.LENGTH_SHORT).show();
            }
            // Optionally, retry with a default size or handle differently
            binding.qrCodeImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Important to prevent memory leaks with ViewBinding in Fragments
    }


    private void createPdfFromTicketView(View view) {
        // Measure the view
        int width = view.getWidth();
        int height = view.getHeight();

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        pdfDocument.finishPage(page);

        // Save to Downloads folder
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "FlyEasy_Ticket_" + new Date().getTime() + ".pdf");

        try {
            pdfFile.createNewFile();
            FileOutputStream out = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(out);
            pdfDocument.close();
            out.close();

            Toast.makeText(getContext(), "PDF downloaded to Downloads", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to create PDF", Toast.LENGTH_SHORT).show();
        }
    }





    private void saveAndShareTicket(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bg = view.getBackground();
        if (bg != null) {
            bg.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        try {
            File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "ticket_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            Uri uri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".provider", file);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Ticket"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save/share ticket", Toast.LENGTH_SHORT).show();
        }
    }

}