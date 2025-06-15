package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.flyeasy.R;
import com.example.flyeasy.model.BookingEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TicketBottomSheetFragment extends BottomSheetDialogFragment {

    private BookingEntity booking;

    public TicketBottomSheetFragment(BookingEntity booking) {
        this.booking = booking;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_bottom_sheet, container, false);

        TextView tvTicketDetails = view.findViewById(R.id.tvTicketDetails);

        String ticketInfo = "Name: " + booking.getFullName() + "\n" +
                "Flight: " + booking.getFlightAirlineName() + "\n" +
                "From: " + booking.getFlightOrigin() + "\n" +
                "To: " + booking.getFlightDestination() + "\n" +
                "Departure: " + booking.getFlightDepartureTime() + "\n" ;

        tvTicketDetails.setText(ticketInfo);

        return view;
    }
}
