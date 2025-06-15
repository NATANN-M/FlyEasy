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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation; // Import NavController

import com.example.flyeasy.MainActivity;
import com.example.flyeasy.R;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.UserDao;
import com.example.flyeasy.databinding.FragmentProfileBinding;
import com.example.flyeasy.model.UserEntity;
import com.example.flyeasy.util.SessionManager;

import java.util.concurrent.Executors;



public class adminProfileFragment extends Fragment {

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

        navController = Navigation.findNavController(view);

        updateProfileUI();


        binding.btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });



        binding.btnLoginRegister.setOnClickListener(v -> {

            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.profileFragment) { // Prevent double navigation
                navController.navigate(R.id.action_profileFragment_to_loginRegisterFragment); // Replace with your actual action ID
            }
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent =new Intent(getActivity(), MainActivity.class);
            // Add flags to clear the activity stack and start a new task
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish(); // Finish the current activity
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
        });

        binding.btnEditProfile.setOnClickListener(v -> {

            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.profileFragment) { // Prevent double navigation
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment); // Replace with your actual action ID
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

        // Show "Login/Register"
        binding.btnLoginRegister.setVisibility(View.VISIBLE);
        binding.txtUserName.setVisibility(View.GONE);
        binding.txtUserEmail.setVisibility(View.GONE);
        binding.txtUserPhone.setVisibility(View.GONE);
        if (binding.imgProfile != null) {
            binding.imgProfile.setVisibility(View.GONE);
        }
        binding.btnEditProfile.setVisibility(View.GONE);
        binding.btnLogout.setVisibility(View.GONE);
    }

    private void loadUser(int userId) {
        if (binding == null) return; // Guard against null binding

        LiveData<UserEntity> userLiveData = userDao.getUserById(userId);


        userLiveData.observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(@Nullable UserEntity user) {
                if (binding == null) return;

                if (user != null) {
                    // Show user details
                    binding.txtUserName.setText(user.fullName);
                    binding.txtUserEmail.setText(user.email);
                    binding.txtUserPhone.setText(user.phone);

                    binding.txtUserName.setVisibility(View.VISIBLE);
                    binding.txtUserEmail.setVisibility(View.VISIBLE);
                    binding.txtUserPhone.setVisibility(View.VISIBLE);
                    if (binding.imgProfile != null) {
                        binding.imgProfile.setVisibility(View.VISIBLE); // Show profile image
                    }
                    binding.btnEditProfile.setVisibility(View.VISIBLE);
                    binding.btnLogout.setVisibility(View.VISIBLE);

                    // Hide "Login/Register" button
                    binding.btnLoginRegister.setVisibility(View.GONE);
                    if (user != null && user.profileImageUri != null) {
                        binding.imgProfile.setImageURI(Uri.parse(user.profileImageUri));
                    }
                } else {
                    showLoginOption();
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}