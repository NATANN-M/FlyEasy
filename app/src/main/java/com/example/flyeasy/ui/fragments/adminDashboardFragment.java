package com.example.flyeasy.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment; // Ensure this import is present

import android.util.Log; // Added for logging in setStat if needed
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flyeasy.R;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.databinding.FragmentAdminDashboardBinding;
import com.example.flyeasy.viewmodel.AdminDashboardViewModel;


public class adminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;
    private AdminDashboardViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);

//        CardView cardSendNotification = binding.getRoot().findViewById(R.id.cardSendNotification);
//        cardSendNotification.setOnClickListener(v -> {
//            Fragment sendNotifFragment = new SendNotificationFragment();
//            requireActivity()
//                    .getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.action_adminDashboardFragment_to_sendNotificationFragment, sendNotifFragment)
//                    .addToBackStack(null)
//                    .commit();
//        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(requireContext());


        AdminDashboardViewModel.Factory factory = new AdminDashboardViewModel.Factory(
                db.userDao(), db.bookingDao(), db.savedFlightDao());

        viewModel = new ViewModelProvider(this, factory).get(AdminDashboardViewModel.class);

        setupObservers();


        if (binding.cardManageUsers != null) {
            binding.cardManageUsers.setOnClickListener(v -> {
                NavHostFragment.findNavController(adminDashboardFragment.this)
                        .navigate(R.id.action_adminDashboardFragment_to_manageUsersFragment);
            });
        } else {

            Log.e("AdminDashboardFragment", "cardManageUsers view not found in binding. Check XML ID.");
        }

        if (binding.cardSendNotification != null) { // Check if cardSendNotification itself is found
            binding.cardSendNotification.setOnClickListener(v -> {

                NavHostFragment.findNavController(adminDashboardFragment.this)
                        .navigate(R.id.action_adminDashboardFragment_to_sendNotificationFragment);
            });
        }
    }

    private void setupObservers() {
        viewModel.getUserCount().observe(getViewLifecycleOwner(), count -> {

            if (binding.cardUsers != null) {
                setStat(binding.cardUsers.getRoot(), "Total Users", count);
            }
        });

        viewModel.getFlightCount().observe(getViewLifecycleOwner(), count -> {
            if (binding.cardFlights != null) {
                setStat(binding.cardFlights.getRoot(), "Total Flights", count);
            }
        });

        viewModel.getBookingCount().observe(getViewLifecycleOwner(), count -> {
            if (binding.cardBookings != null) {
                setStat(binding.cardBookings.getRoot(), "Total Bookings", count);
            }
        });

    }

    private void setStat(View cardView, String title, int count) {
        if (cardView == null) {
            Log.e("AdminDashboardFragment", "setStat: cardView is null for title: " + title);
            return;
        }
        TextView titleView = cardView.findViewById(R.id.statTitle);
        TextView countView = cardView.findViewById(R.id.statCount);

        if (titleView != null) {
            titleView.setText(title);
        } else {
            Log.e("AdminDashboardFragment", "setStat: statTitle TextView not found in cardView for title: " + title);
        }

        if (countView != null) {
            countView.setText(String.valueOf(count));
        } else {
            Log.e("AdminDashboardFragment", "setStat: statCount TextView not found in cardView for title: " + title);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}