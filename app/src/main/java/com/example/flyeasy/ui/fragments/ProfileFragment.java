package com.example.flyeasy.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation; // Import NavController

import com.bumptech.glide.Glide;
import com.example.flyeasy.R;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.UserDao;
import com.example.flyeasy.databinding.FragmentProfileBinding;
import com.example.flyeasy.model.UserEntity;
import com.example.flyeasy.util.SessionManager;

import java.util.concurrent.Executors;



public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SessionManager sessionManager;
    private UserDao userDao;
    private NavController navController; // For Jetpack Navigation

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(requireContext());
        userDao = db.userDao();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view); // Initialize NavController


        updateProfileUI();


        binding.btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });



        binding.btnLoginRegister.setOnClickListener(v -> {

            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.profileFragment) { // Prevent double navigation
                navController.navigate(R.id.action_profileFragment_to_loginRegisterFragment);
            }
        });


        binding.btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        // User clicked "Logout"
                        sessionManager.clearSessionData();

                        // Restart MainActivity
                        Intent intent = new Intent(requireActivity(), com.example.flyeasy.MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();


                        Toast.makeText(requireContext(), "Logged out....", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // User clicked "Cancel", dismiss the dialog
                        dialog.dismiss();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert) // Optional
                    .show();
        });

        binding.btnEditProfile.setOnClickListener(v -> {

            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.profileFragment) { // Prevent double navigation
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.imgProfile.setImageURI(selectedImageUri);

                // Save to Room
                int userId = new SessionManager(requireContext()).getUserId();
                Executors.newSingleThreadExecutor().execute(() -> {
                    FlyEasyDatabase.getDatabase(requireContext())
                            .userDao()
                            .updateProfileImage(userId, selectedImageUri.toString());
                });

                Executors.newSingleThreadExecutor().execute(() -> {
                    FlyEasyDatabase.getDatabase(requireContext())
                            .userDao()
                            .updateProfileImage(userId, selectedImageUri.toString());

                    // Save it to session
                    sessionManager.saveProfileImageUri(selectedImageUri.toString());
                });

            }
        }
    }

    private void updateProfileUI() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            showLoginOption();
        } else {
            loadUser(userId);
        }
    }


    private void showLoginOption() {
        if (binding == null) return;


        binding.btnLoginRegister.setVisibility(View.VISIBLE);

        // Hide user-specific details and action buttons
        binding.txtUserName.setVisibility(View.GONE);
        binding.txtUserEmail.setVisibility(View.GONE);
        binding.txtUserPhone.setVisibility(View.GONE);
        binding.btnChangeImage.setVisibility(View.GONE);
        if (binding.imgProfile != null) {
            binding.imgProfile.setVisibility(View.GONE);
        }
        binding.btnEditProfile.setVisibility(View.GONE);
        binding.btnLogout.setVisibility(View.GONE);
    }

    private void loadUser(int userId) {
        if (binding == null) return;

        LiveData<UserEntity> userLiveData = userDao.getUserById(userId);

        userLiveData.observe(getViewLifecycleOwner(), user -> {
            if (binding == null) return;

            if (user != null) {
                binding.txtUserName.setText(user.fullName);
                binding.txtUserEmail.setText(user.email);
                binding.txtUserPhone.setText(user.phone);

                binding.txtUserName.setVisibility(View.VISIBLE);
                binding.txtUserEmail.setVisibility(View.VISIBLE);
                binding.txtUserPhone.setVisibility(View.VISIBLE);
                binding.btnEditProfile.setVisibility(View.VISIBLE);
                binding.btnLogout.setVisibility(View.VISIBLE);
                binding.btnLoginRegister.setVisibility(View.GONE);

                if (user.profileImageUri != null && !user.profileImageUri.isEmpty()) {
                    Glide.with(requireContext())
                            .load(Uri.parse(user.profileImageUri))
                            .placeholder(R.drawable.ic_profile)
                            .into(binding.imgProfile);
                } else {
                    binding.imgProfile.setImageResource(R.drawable.ic_profile);
                }
                binding.imgProfile.setVisibility(View.VISIBLE);
            } else {
                showLoginOption();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // prevent memory leaks
    }
}