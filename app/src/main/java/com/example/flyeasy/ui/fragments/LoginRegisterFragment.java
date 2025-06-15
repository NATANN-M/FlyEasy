package com.example.flyeasy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.flyeasy.MainActivity;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.UserDao;
import com.example.flyeasy.databinding.FragmentLoginRegisterBinding;
import com.example.flyeasy.model.UserEntity;
import com.example.flyeasy.util.SessionManager;

import java.util.concurrent.Executors;

public class LoginRegisterFragment extends Fragment {

    private FragmentLoginRegisterBinding binding;
    private boolean isLogin = true;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(requireContext());
        userDao = db.userDao();
        sessionManager = new SessionManager(requireContext());

        updateUIForMode();

        binding.txtToggleLoginRegister.setOnClickListener(v -> {
            isLogin = !isLogin;
            updateUIForMode();
        });

        binding.btnLoginRegister.setOnClickListener(v -> {
            if (isLogin) {
                loginUser();
            } else {
                registerUser();
            }
        });

        return binding.getRoot();
    }

    private void updateUIForMode() {
        if (isLogin) {
            binding.fullNameLayout.setVisibility(View.GONE); //  layout, not edt
            binding.phoneLayout.setVisibility(View.GONE);
            binding.btnLoginRegister.setText("Login");
            binding.txtToggleLoginRegister.setText("Don't have an account? Register");
        } else {
            binding.fullNameLayout.setVisibility(View.VISIBLE);
            binding.phoneLayout.setVisibility(View.VISIBLE);
            binding.btnLoginRegister.setText("Register");
            binding.txtToggleLoginRegister.setText("Already have an account? Login");
        }
    }


    private void registerUser() {
        String name = binding.edtFullName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity existing = userDao.getUserByEmail(email).getValue();
            if (existing != null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show());
            } else {
                UserEntity user = new UserEntity();
                user.fullName = name;
                user.email = email;
                user.phone = phone;
                user.password = password;
                user.role = "user";  // Default role
                long id = userDao.insertUser(user);

                sessionManager.saveUserId((int) id);
                sessionManager.saveUserRole("user");
sessionManager.saveUserName(name);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // go back to profile
                });
            }
        });
    }

    private void loginUser() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LiveData<UserEntity> userLiveData = userDao.getUserByEmail(email);

        userLiveData.observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {
                if (user != null || !userLiveData.hasActiveObservers() || userLiveData.getValue() != null) {
                    userLiveData.removeObserver(this);
                }

                if (user == null) {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                } else if (!user.password.equals(password)) {
                    Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                } else {
                    sessionManager.saveUserId(user.userId);
                    sessionManager.saveUserRole(user.role); // Save role: "admin" or "user"

                    Toast.makeText(getContext(), user.role.equals("admin") ?
                            "Admin login successful" : "Login successful", Toast.LENGTH_SHORT).show();

                    if (isAdded() && getActivity() != null) {
                        // Restart MainActivity so bottom nav updates correctly
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish(); // Finish current activity
                    }
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
