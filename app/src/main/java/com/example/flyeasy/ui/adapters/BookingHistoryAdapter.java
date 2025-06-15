package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.BookingEntity;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private List<BookingEntity> bookingList;

    public void setBookingList(List<BookingEntity> list) {
        this.bookingList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapter.ViewHolder holder, int position) {
        BookingEntity booking = bookingList.get(position);
        holder.txtBookingInfo.setText(
                "Booking ID: " + booking.getId() +
                        "\nPassenger Name: " + booking.getFullName() +
                        "\nEmail: " + booking.getEmail() +
                        "\nFlight Origin: " + booking.getFlightOrigin() +
                        "\nFlight Destination: " + booking.getFlightDestination() +
                        "\nFlight ID: " + booking.getFlightId() +
                        "\nDate: " + booking.getBookingDate()
        );
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookingInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookingInfo = itemView.findViewById(R.id.txtBookingInfo);
        }
    }
}
