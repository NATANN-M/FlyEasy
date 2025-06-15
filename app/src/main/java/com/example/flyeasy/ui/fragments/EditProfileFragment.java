package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Added for @Nullable
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData; // For LiveData
import androidx.lifecycle.Observer;  // For Observer
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.UserDao;
import com.example.flyeasy.databinding.FragmentEditProfileBinding;
import com.example.flyeasy.model.UserEntity;
import com.example.flyeasy.util.SessionManager;

import java.util.concurrent.ExecutorService; // For managing executor
import java.util.concurrent.Executors;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private UserDao userDao;
    private SessionManager sessionManager;
    private UserEntity currentUser;
    private NavController navController;
    private ExecutorService executorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(requireContext());
        userDao = db.userDao();
        sessionManager = new SessionManager(requireContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "User not logged in. Please login again.", Toast.LENGTH_LONG).show();
            //  navigate back if user must be logged in for this screen
            navController.popBackStack();
            return;
        }

        loadUserDetails(userId);

        binding.btnUpdate.setOnClickListener(v -> {
            if (currentUser != null) {
                updateProfile();
            } else {
                Toast.makeText(getContext(), "User data not loaded yet. Please wait.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserDetails(int userId) {

        LiveData<UserEntity> userLiveData = userDao.getUserById(userId);

        userLiveData.observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(@Nullable UserEntity user) {
                if (user != null) {
                    currentUser = user; // Store the loaded user
                    binding.edtFullName.setText(currentUser.fullName);
                    binding.edtEmail.setText(currentUser.email);
                    binding.edtPhone.setText(currentUser.phone);

                    userLiveData.removeObserver(this);
                } else {
                    Toast.makeText(getContext(), "Failed to load user details.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void updateProfile() {
        String fullName = binding.edtFullName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (currentUser == null) {
            Toast.makeText(getContext(), "Error: Current user data is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.fullName = fullName;
        currentUser.email = email;
        currentUser.phone = phone;

        executorService.execute(() -> {
            try {
                userDao.updateUser(currentUser); // Perform DB operation

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        navController.popBackStack(); // Navigate back after successful update
                    });
                }
            } catch (Exception e) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}