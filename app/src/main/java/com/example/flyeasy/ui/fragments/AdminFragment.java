package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.flyeasy.databinding.FragmentAdminBinding;

public class AdminFragment extends Fragment {

    private FragmentAdminBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
