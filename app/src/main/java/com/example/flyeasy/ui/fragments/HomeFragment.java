package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.model.BookingEntity;
import com.example.flyeasy.model.Deal;
import com.example.flyeasy.model.Destination;
import com.example.flyeasy.ui.adapters.DealsAdapter;
import com.example.flyeasy.ui.adapters.PopularDestinationsAdapter;
import com.example.flyeasy.util.SessionManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private CardView cardUpcomingBooking;
    private TextView tvBookingDetails;
    private Button btnViewTicket;

    private SessionManager sessionManager;
    private FlyEasyDatabase db;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private RecyclerView rvPopularDestinations;

    private RecyclerView rvDeals;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(requireContext());
        db = FlyEasyDatabase.getDatabase(requireContext());

        View includedView = root.findViewById(R.id.cardUpcomingBooking);

        if (includedView != null) {
            cardUpcomingBooking = includedView.findViewById(R.id.cardUpcomingBooking);
            tvBookingDetails = includedView.findViewById(R.id.tvBookingDetails);
            btnViewTicket = includedView.findViewById(R.id.btnViewTicket);

            cardUpcomingBooking.setVisibility(View.GONE);
        }

        loadUpcomingBooking();



        rvPopularDestinations = root.findViewById(R.id.rvPopularDestinations);
        rvPopularDestinations.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

// static data
        List<Destination> popularList = Arrays.asList(
                new Destination("Paris", R.drawable.img_paris),
                new Destination("Tokyo", R.drawable.img_tokyo),
                new Destination("Dubai", R.drawable.img_dubai),
                new Destination("New York", R.drawable.img_newyork),
                new Destination("Addis Ababa", R.drawable.img_addisababa)
        );

        PopularDestinationsAdapter adapter = new PopularDestinationsAdapter(popularList);
        rvPopularDestinations.setAdapter(adapter);

        //deals static data

        rvDeals = root.findViewById(R.id.rvDeals);
        rvDeals.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Deal> dealList = Arrays.asList(
                new Deal("30% Off Tokyo Flights", R.drawable.img_tokyo),
                new Deal("New York Summer Sale", R.drawable.img_newyork),
                new Deal("Fly to Paris at Half Price!", R.drawable.img_paris)
        );

        DealsAdapter dealsAdapter = new DealsAdapter(dealList);
        rvDeals.setAdapter(dealsAdapter);




        return root;
    }

    private void loadUpcomingBooking() {
        int userId = sessionManager.getUserId();
        executor.execute(() -> {
            BookingEntity booking = db.bookingDao().getNextBooking(userId);
            if (booking != null) {
                requireActivity().runOnUiThread(() -> {
                    if (cardUpcomingBooking != null && tvBookingDetails != null && btnViewTicket != null) {
                        cardUpcomingBooking.setVisibility(View.VISIBLE);

                        String bookingText = "Flight " + booking.getFlightAirlineName() +
                                " from " + booking.getFlightOrigin() +
                                " to " + booking.getFlightDestination() + "\n" +
                                "Departs at: " + booking.getFlightDepartureTime();

                        tvBookingDetails.setText(bookingText);

                        btnViewTicket.setOnClickListener(v -> {
                            TicketBottomSheetFragment bottomSheet = new TicketBottomSheetFragment(booking);
                            bottomSheet.show(getParentFragmentManager(), "TicketBottomSheet");
                        });

                    }
                });
            }
        });
    }
}
