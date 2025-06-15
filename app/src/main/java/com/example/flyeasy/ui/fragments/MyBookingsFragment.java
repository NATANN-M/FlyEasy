package com.example.flyeasy.ui.fragments;

import android.content.Intent; // Import for sharing
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu; // Import for PopupMenu
import android.widget.Toast;   // Import for Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flyeasy.R;
import com.example.flyeasy.databinding.FragmentMyBookingsBinding;
import com.example.flyeasy.model.BookingEntity;
import com.example.flyeasy.model.FlightEntity;
import com.example.flyeasy.model.SavedFlightEntity;
import com.example.flyeasy.ui.adapters.BookingAdapter;
import com.example.flyeasy.viewmodel.BookingViewModel;
import com.example.flyeasy.viewmodel.SavedFlightViewModel;

public class MyBookingsFragment extends Fragment {
    private FlightEntity selectedFlight;
    private FragmentMyBookingsBinding binding;

    private SavedFlightViewModel savedFlightViewModel;
    private BookingViewModel bookingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyBookingsBinding.inflate(inflater, container, false);
        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        savedFlightViewModel = new ViewModelProvider(this).get(SavedFlightViewModel.class);

        // Initialize Adapter with ticket click listener
        BookingAdapter bookingAdapter = new BookingAdapter((booking, itemView) -> {
            // Navigate to TicketFragment and pass data
            Bundle bundle = new Bundle();

            // Passenger and Booking Core Details
            bundle.putString("fullName", booking.getFullName());
            bundle.putString("email", booking.getEmail());
            bundle.putString("phone", booking.getPhone()); //  phone in BookingEntity
            bundle.putString("passportNumber", booking.getPassportNumber()); // passportNumber
            bundle.putString("bookingId", String.valueOf(booking.getId()));
            bundle.putString("bookingDate", booking.getBookingDate()); // The date the booking was made

            // Stored Flight Details from BookingEntity
            bundle.putString("flightId", String.valueOf(booking.getFlightId())); // The original flight ID
            bundle.putString("flightOrigin", booking.getFlightOrigin());
            bundle.putString("flightDestination", booking.getFlightDestination());
            bundle.putString("flightAirlineName", booking.getFlightAirlineName());
            bundle.putString("flightDepartureTime", booking.getFlightDepartureTime());
            bundle.putDouble("flightPriceAtBooking", booking.getFlightPriceAtBooking()); // Or String.valueOf()


            bundle.putString("seat", "N/A"); // Placeholder - Update if seat info is available in BookingEntity





            // L pass  TicketFragment  using the new fields:
            bundle.putString("flight", String.valueOf(booking.getFlightId()));
            bundle.putString("airline", booking.getFlightAirlineName());       // "airline" in TicketFragment
            bundle.putString("from", booking.getFlightOrigin());              // "from" in TicketFragment
            bundle.putString("to", booking.getFlightDestination());            // "to" in TicketFragment
            bundle.putString("date", booking.getBookingDate());
            bundle.putString("time", booking.getFlightDepartureTime());         // "time" in TicketFragment



            Navigation.findNavController(itemView).navigate(R.id.action_myBookingsFragment_to_ticketFragment, bundle);
        });

        // Setup long click listener for context menu (delete, share)
        bookingAdapter.setOnBookingLongClickListener((itemView, booking) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), itemView); // itemView is the anchor for the popup
            popupMenu.getMenuInflater().inflate(R.menu.menu_booking_context, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_delete) {
                    bookingViewModel.deleteBooking(booking); // Ensure deleteBooking is in BookingViewModel
                    Toast.makeText(requireContext(), "Booking deleted", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_share) {
                    shareBooking(booking); // Call the updated shareBooking method
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });

        bookingAdapter.setOnSaveFlightClickListener(booking -> {
            if (booking != null) {
                // Create a SavedFlightEntity from the BookingEntity's flight details
                SavedFlightEntity savedFlight = new SavedFlightEntity(
                        booking.getFlightAirlineName(), // Assuming constructor matches
                        booking.getFlightOrigin(),
                        booking.getFlightDestination(),
                        booking.getFlightDepartureTime(),
                        booking.getFlightPriceAtBooking()

                );

                savedFlightViewModel.insert(savedFlight);

                Toast.makeText(requireContext(), "Flight saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error: Cannot save null booking details.", Toast.LENGTH_SHORT).show();
                Log.e("MyBookingsFragment", "Attempted to save a null booking from adapter callback.");
            }
        });



        // Setup RecyclerView
        binding.recyclerViewBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewBookings.setAdapter(bookingAdapter);

        // Observe ViewModel data for updates to the booking list
        bookingViewModel.getAllBookings().observe(getViewLifecycleOwner(), bookings -> {
            if (bookings != null) {
                bookingAdapter.submitList(bookings); // Update adapter with the new list

            }
        });
    }

    /**
     * Handles sharing the booking details via an Intent.
     *
     * @param booking The BookingEntity containing details to share.
     */
    private void shareBooking(BookingEntity booking) {
        if (booking == null) {
            Toast.makeText(requireContext(), "Cannot share empty booking details.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder shareBodyBuilder = new StringBuilder();
        shareBodyBuilder.append("✈️ My Flight Booking Details ✈️\n");
        shareBodyBuilder.append("------------------------------------\n");

        if (booking.getFullName() != null && !booking.getFullName().isEmpty()) {
            shareBodyBuilder.append("Passenger Name: ").append(booking.getFullName()).append("\n");
        }
        if (booking.getEmail() != null && !booking.getEmail().isEmpty()) {
            shareBodyBuilder.append("Email: ").append(booking.getEmail()).append("\n");
        }
        shareBodyBuilder.append("Flight ID/Number: ").append(booking.getFlightId()).append("\n");

        if (booking.getBookingDate() != null && !booking.getBookingDate().isEmpty()) {
            shareBodyBuilder.append("Booking Date: ").append(booking.getBookingDate()).append("\n");
        }



        shareBodyBuilder.append("------------------------------------\n");
        shareBodyBuilder.append("Shared from FlyEasy App!");

        String shareText = shareBodyBuilder.toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Flight Booking Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Start an activity chooser
        try {
            startActivity(Intent.createChooser(shareIntent, "Share Booking Details via"));
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(requireContext(), "No app available to share.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}