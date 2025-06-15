package com.example.flyeasy.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flyeasy.R;
import com.example.flyeasy.databinding.FragmentSavedTicketsBinding;
import com.example.flyeasy.ui.adapters.TicketAdapter;
import com.example.flyeasy.viewmodel.TicketViewModel;


public class SavedTicketsFragment extends Fragment {

    private FragmentSavedTicketsBinding binding;
    private TicketViewModel ticketViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        TicketAdapter adapter = new TicketAdapter(getContext(), ticketViewModel);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ticketViewModel.getAllTickets().observe(getViewLifecycleOwner(), tickets -> {
            adapter.setTicketList(tickets);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
