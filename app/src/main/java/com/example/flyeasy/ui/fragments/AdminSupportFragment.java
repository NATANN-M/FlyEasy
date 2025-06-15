package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.ui.adapters.AdminSupportAdapter;
import com.example.flyeasy.viewmodel.SupportViewModel;

public class AdminSupportFragment extends Fragment {

    private AdminSupportAdapter adapter;
    private SupportViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_support, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAdminSupport);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        adapter = new AdminSupportAdapter(requireContext(), viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getAllMessages().observe(getViewLifecycleOwner(), adapter::setMessages);

        return view;
    }
}
