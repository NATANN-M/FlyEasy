// ui/fragments/SavedFlightsFragment.java
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flyeasy.databinding.FragmentSavedFlightsBinding;
import com.example.flyeasy.model.SavedFlightEntity;
import com.example.flyeasy.ui.adapters.SavedFlightAdapter;
import com.example.flyeasy.viewmodel.SavedFlightViewModel;

public class SavedFlightsFragment extends Fragment {

    private FragmentSavedFlightsBinding binding;
    private SavedFlightViewModel savedFlightViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedFlightsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedFlightViewModel = new ViewModelProvider(this).get(SavedFlightViewModel.class);
        SavedFlightAdapter adapter = new SavedFlightAdapter(flight -> {
            savedFlightViewModel.delete(flight);
            Toast.makeText(requireContext(), "Flight deleted", Toast.LENGTH_SHORT).show();
        });

        binding.recyclerViewSavedFlights.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewSavedFlights.setAdapter(adapter);

        savedFlightViewModel.getAllSavedFlights().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
