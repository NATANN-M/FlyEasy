package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flyeasy.R;
import com.example.flyeasy.databinding.FragmentFlightListBinding;
import com.example.flyeasy.model.FlightEntity;
import com.example.flyeasy.ui.adapters.FlightAdapter;
import com.example.flyeasy.viewmodel.FlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class FlightListFragment extends Fragment {

    private FragmentFlightListBinding binding;
    private FlightViewModel viewModel;
    private FlightAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFlightListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FlightViewModel.class);
        adapter = new FlightAdapter(new FlightAdapter.OnFlightClickListener() {
            @Override
            public void onFlightClick(FlightEntity flight) {
                Toast.makeText(getContext(),
                        "Selected: " + (flight.origin != null ? flight.origin : "Unknown")
                                + " ➡ " + (flight.destination != null ? flight.destination : "Unknown"),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBookClick(FlightEntity flight) {
                //

                Bundle bundle = new Bundle();
                bundle.putSerializable("flight", flight);  // FlightEntity implements Serializable

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_searchFragment_to_bookingFragment,bundle);

            }

        });

        binding.recyclerFlights.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFlights.setAdapter(adapter);

        loadAllFlights();

        binding.btnSearch.setOnClickListener(v -> {
            String origin = binding.editOrigin.getText().toString().trim();
            String destination = binding.editDestination.getText().toString().trim();
            String date = binding.editDate.getText().toString().trim();

            if (origin.isEmpty() || destination.isEmpty()) {
                Toast.makeText(getContext(), "Please enter origin and destination", Toast.LENGTH_SHORT).show();
            } else {
                searchFlights(origin, destination, date);
            }
        });
    }

    private void loadAllFlights() {
        showLoading(true);
        viewModel.getAllFlights().observe(getViewLifecycleOwner(), flights -> {
            showLoading(false);
            adapter.setFlightList(flights);
        });
    }

    private void searchFlights(String origin, String destination, String date) {
        showLoading(true);
        viewModel.getAllFlights().observe(getViewLifecycleOwner(), flights -> {
            List<FlightEntity> filtered = new ArrayList<>();
            for (FlightEntity f : flights) {
                boolean matchOrigin = origin != null && origin.equalsIgnoreCase(f.origin != null ? f.origin : "");
                boolean matchDestination = destination != null && destination.equalsIgnoreCase(f.destination != null ? f.destination : "");
                boolean matchDate = date == null || date.isEmpty() ||
                        (f.departureTime != null && f.departureTime.startsWith(date));

                if (matchOrigin && matchDestination && matchDate) {
                    filtered.add(f);
                }
            }

            adapter.setFlightList(filtered);
            showLoading(false);

            if (filtered.isEmpty()) {
                Toast.makeText(getContext(), "No matching flights found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
