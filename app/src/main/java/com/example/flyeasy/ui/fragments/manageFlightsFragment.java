package com.example.flyeasy.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flyeasy.R;
import com.example.flyeasy.databinding.FragmentManageFlightsBinding;
import com.example.flyeasy.model.FlightEntity;
import com.example.flyeasy.ui.adapters.FlightListAdapter;
import com.example.flyeasy.viewmodel.FlightViewModel;

import java.util.Calendar;
import java.util.List;

public class manageFlightsFragment extends Fragment {

    private FragmentManageFlightsBinding binding;
    private FlightViewModel flightViewModel;
    private FlightListAdapter flightListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageFlightsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);

        flightListAdapter = new FlightListAdapter(new FlightListAdapter.FlightClickListener() {
            @Override
            public void onSaveClicked(FlightEntity flight) {
                flightViewModel.insertFlight(flight);
                Toast.makeText(getContext(), "Flight saved locally", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditClicked(FlightEntity flight) {
                showEditFlightDialog(flight);
            }

            @Override
            public void onDeleteClicked(FlightEntity flight) {
                flightViewModel.deleteFlight(flight);
                Toast.makeText(getContext(), "Flight deleted", Toast.LENGTH_SHORT).show();
            }
        });

        binding.recyclerFlights.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFlights.setAdapter(flightListAdapter);

        // Handle button clicks
        binding.btnFetchApiFlights.setOnClickListener(v -> fetchApiFlights());
        binding.btnViewLocalFlights.setOnClickListener(v -> loadLocalFlights());
        binding.btnAddFlight.setOnClickListener(v -> showAddFlightDialog());
    }

    private void fetchApiFlights() {
        flightViewModel.getAllFlights().observe(getViewLifecycleOwner(), flights -> {
            flightListAdapter.setFlightList(flights, false); // false = not local
        });
    }

    private void loadLocalFlights() {
        flightViewModel.getAllLocalFlights().observe(getViewLifecycleOwner(), flights -> {
            flightListAdapter.setFlightList(flights, true); // true = local
        });
    }

    private void showAddFlightDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_flight, null);

        EditText etAirline = dialogView.findViewById(R.id.et_airline);
        EditText etOrigin = dialogView.findViewById(R.id.et_origin);
        EditText etDestination = dialogView.findViewById(R.id.et_destination);
        EditText etDeparture = dialogView.findViewById(R.id.et_departure);
        EditText etArrival = dialogView.findViewById(R.id.et_arrival);
        EditText etPrice = dialogView.findViewById(R.id.et_price);
        EditText etSeats = dialogView.findViewById(R.id.et_seats);
        EditText etImageUrl = dialogView.findViewById(R.id.et_image_url);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Add New Flight")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    FlightEntity flight = new FlightEntity();
                    flight.airline = etAirline.getText().toString().trim();
                    flight.origin = etOrigin.getText().toString().trim();
                    flight.destination = etDestination.getText().toString().trim();
                    flight.departureTime = etDeparture.getText().toString().trim();
                    flight.arrivalTime = etArrival.getText().toString().trim();
                    flight.imageUrl = etImageUrl.getText().toString().trim();

                    try {
                        flight.price = Double.parseDouble(etPrice.getText().toString().trim());
                        flight.seatsAvailable = Integer.parseInt(etSeats.getText().toString().trim());

                        flightViewModel.insertFlight(flight);
                        Toast.makeText(getContext(), "Flight added successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("insert", e.getMessage());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void showEditFlightDialog(FlightEntity flight) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_flight, null);

        EditText etAirline = dialogView.findViewById(R.id.et_airline);
        EditText etOrigin = dialogView.findViewById(R.id.et_origin);
        EditText etDestination = dialogView.findViewById(R.id.et_destination);
        EditText etDeparture = dialogView.findViewById(R.id.et_departure_time);
        EditText etArrival = dialogView.findViewById(R.id.et_arrival_time);
        EditText etPrice = dialogView.findViewById(R.id.et_price);
        EditText etSeats = dialogView.findViewById(R.id.et_seats);
        EditText etImage = dialogView.findViewById(R.id.et_image);

        // Pre-fill with existing data
        etAirline.setText(flight.airline);
        etOrigin.setText(flight.origin);
        etDestination.setText(flight.destination);
        etDeparture.setText(flight.departureTime);
        etArrival.setText(flight.arrivalTime);
        etPrice.setText(String.valueOf(flight.price));
        etSeats.setText(String.valueOf(flight.seatsAvailable));
        etImage.setText(flight.imageUrl);

        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Edit Flight")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    flight.airline = etAirline.getText().toString();
                    flight.origin = etOrigin.getText().toString();
                    flight.destination = etDestination.getText().toString();
                    flight.departureTime = etDeparture.getText().toString();
                    flight.arrivalTime = etArrival.getText().toString();
                    flight.price = Double.parseDouble(etPrice.getText().toString());
                    flight.seatsAvailable = Integer.parseInt(etSeats.getText().toString());
                    flight.imageUrl = etImage.getText().toString();

                    flightViewModel.updateFlight(flight);
                    Toast.makeText(getContext(), "Flight updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDateTimePicker(Context context, EditText targetEditText) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Now show TimePicker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                String formatted = DateFormat.format("MMM dd, yyyy - HH:mm", calendar).toString();
                                targetEditText.setText(formatted);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


}
