package com.example.flyeasy.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.TicketEntity;
import com.example.flyeasy.viewmodel.TicketViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<TicketEntity> ticketList = new ArrayList<>();
    private final Context context;
    private final TicketViewModel ticketViewModel;

    public TicketAdapter(Context context, TicketViewModel ticketViewModel) {
        this.context = context;
        this.ticketViewModel = ticketViewModel;
    }

    public void setTicketList(List<TicketEntity> tickets) {
        this.ticketList = tickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketEntity ticket = ticketList.get(position);

        holder.flightInfo.setText("Flight: " + ticket.flight + " (" + ticket.airline + ")");
        holder.fromTo.setText("From: " + ticket.from + " → To: " + ticket.to);
        holder.dateTime.setText("Date: " + ticket.flightDate + " | Time: " + ticket.flightTime);
        holder.passenger.setText("Passenger: " + ticket.passenger);

        // Generate QR Code
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(ticket.bookingId, BarcodeFormat.QR_CODE, 150, 150);
            Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);

            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            holder.qrImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

         //Delete ticket
        holder.deleteBtn.setOnClickListener(v -> {
            ticketViewModel.deleteTicket(ticket);
            Toast.makeText(context, "Ticket deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        ImageView qrImage;
        TextView flightInfo, fromTo, dateTime, passenger;
        ImageButton deleteBtn;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            qrImage = itemView.findViewById(R.id.qrImage);
            flightInfo = itemView.findViewById(R.id.flightInfo);
            fromTo = itemView.findViewById(R.id.fromTo);
            dateTime = itemView.findViewById(R.id.dateTime);
            passenger = itemView.findViewById(R.id.passenger);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}

