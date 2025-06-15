package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.databinding.ItemBookingBinding;
import com.example.flyeasy.model.BookingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<BookingEntity> bookingList = new ArrayList<>();
    private OnTicketClickListener ticketClickListener; // For the "View Ticket" button
    private OnBookingLongClickListener bookingLongClickListener; // For long press on the whole item
    private OnSaveFlightClickListener saveFlightClickListener; //  "Save Flight" button

    // Interface for the "View Ticket" button click
    public interface OnTicketClickListener {
        void onTicketClick(BookingEntity booking, View buttonView);
    }

    // Interface for long click on the entire item view
    public interface OnBookingLongClickListener {
        void onLongClick(View itemView, BookingEntity booking);
    }

    // Interface for the "Save Flight" button click -->
    public interface OnSaveFlightClickListener {
        void onSaveFlightClick(BookingEntity booking);
    }

    // Constructor: Takes the listener for the "View Ticket" button
    public BookingAdapter(OnTicketClickListener ticketClickListener) {
        this.ticketClickListener = ticketClickListener;
    }

    // Method to set the long click listener for the entire item
    public void setOnBookingLongClickListener(OnBookingLongClickListener listener) {
        this.bookingLongClickListener = listener;
    }

    //  Method to set the save flight click listener -->
    public void setOnSaveFlightClickListener(OnSaveFlightClickListener listener) {
        this.saveFlightClickListener = listener;
    }

    // Method to update the list of bookings
    public void submitList(List<BookingEntity> newList) {
        bookingList.clear();
        if (newList != null) {
            bookingList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingBinding binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        // Pass all listener instances to the ViewHolder
        return new BookingViewHolder(binding, ticketClickListener, bookingLongClickListener, saveFlightClickListener); // <-- Pass new listener
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingEntity currentBooking = bookingList.get(position);
        holder.bind(currentBooking);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookingBinding binding;
        // Store the listener instances passed from the Adapter
        private final OnTicketClickListener localTicketClickListener;
        private final OnBookingLongClickListener localBookingLongClickListener;
        private final OnSaveFlightClickListener localSaveFlightClickListener; // Store save flight listener

        public BookingViewHolder(ItemBookingBinding binding,
                                 OnTicketClickListener ticketClickListener,
                                 OnBookingLongClickListener bookingLongClickListener,
                                 OnSaveFlightClickListener saveFlightClickListener) { //  Receive new listener
            super(binding.getRoot());
            this.binding = binding;
            this.localTicketClickListener = ticketClickListener;
            this.localBookingLongClickListener = bookingLongClickListener;
            this.localSaveFlightClickListener = saveFlightClickListener; //  Store new listener

            // --- Setup Long Click Listener for the entire item view ---
            itemView.setOnLongClickListener(view -> {
                if (localBookingLongClickListener != null) {
                    int currentPosition = getAdapterPosition(); // Use getBindingAdapterPosition() for newer AGP
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        Object tag = itemView.getTag();
                        if (tag instanceof BookingEntity) {
                            localBookingLongClickListener.onLongClick(view, (BookingEntity) tag);
                            return true;
                        }
                    }
                }
                return false;
            });

            // --- Setup Click Listener for the "View Ticket" button ---
            binding.buttonViewTicket.setOnClickListener(buttonView -> {
                if (localTicketClickListener != null) {
                    int currentPosition = getAdapterPosition(); // Use getBindingAdapterPosition() for newer AGP
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        Object tag = itemView.getTag();
                        if (tag instanceof BookingEntity) {
                            localTicketClickListener.onTicketClick((BookingEntity) tag, buttonView);
                        }
                    }
                }
            });

            //  Setup Click Listener for the "Save Flight" button ---
            //  "btnSaveFlight" in  item_booking.xml
            if (binding.btnSaveFlight != null) {
                binding.btnSaveFlight.setOnClickListener(saveButtonView -> {
                    if (localSaveFlightClickListener != null) {
                        int currentPosition = getAdapterPosition(); //  getBindingAdapterPosition()
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            Object tag = itemView.getTag(); // Get the BookingEntity from the tag
                            if (tag instanceof BookingEntity) {
                                localSaveFlightClickListener.onSaveFlightClick((BookingEntity) tag);
                            }
                        }
                    }
                });
            }
        }

        public void bind(BookingEntity booking) {

            itemView.setTag(booking);

            // --- Standard Booking Details ---
            String passengerText = "Passenger: " + (booking.getFullName() != null ? booking.getFullName() : "N/A");
            binding.textViewName.setText(passengerText);

            String emailText = "Email: " + (booking.getEmail() != null ? booking.getEmail() : "N/A");
            binding.textViewEmail.setText(emailText);

            String bookingDateText = "Booked on: " + (booking.getBookingDate() != null ? booking.getBookingDate() : "N/A");
            binding.textViewBookingDate.setText(bookingDateText);

            //  Stored Flight Details ---
            if (binding.textViewFlightId != null) {
                String flightIdText = "Flight ID: " + booking.getFlightId();
                binding.textViewFlightId.setText(flightIdText);
            }

            if (binding.textViewFlightOrigin != null) {
                String originText = "From: " + (booking.getFlightOrigin() != null ? booking.getFlightOrigin() : "N/A");
                binding.textViewFlightOrigin.setText(originText);
            }

            if (binding.textViewFlightDestination != null) {
                String destinationText = "To: " + (booking.getFlightDestination() != null ? booking.getFlightDestination() : "N/A");
                binding.textViewFlightDestination.setText(destinationText);
            }

            if (binding.textViewFlightAirline != null) {
                String airlineText = "Airline: " + (booking.getFlightAirlineName() != null ? booking.getFlightAirlineName() : "N/A");
                binding.textViewFlightAirline.setText(airlineText);
            }

            if (binding.textViewFlightDepartureTime != null) {
                String departureText = "Departs: " + (booking.getFlightDepartureTime() != null ? booking.getFlightDepartureTime() : "N/A");
                binding.textViewFlightDepartureTime.setText(departureText);
            }

            if (binding.textViewFlightPrice != null) {
                String priceText = String.format(Locale.getDefault(), "Price Paid: $%.2f", booking.getFlightPriceAtBooking());
                binding.textViewFlightPrice.setText(priceText);
            }
        }

    }
}