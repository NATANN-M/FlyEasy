package com.example.flyeasy.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.databinding.FragmentManageUsersBinding;
import com.example.flyeasy.model.UserEntity;
import com.example.flyeasy.ui.adapters.BookingHistoryAdapter;
import com.example.flyeasy.ui.adapters.UserListAdapter;
import com.example.flyeasy.viewmodel.BookingViewModel;
import com.example.flyeasy.viewmodel.UserViewModel;

import java.util.List;

public class manageUsersFragment extends Fragment {

    private FragmentManageUsersBinding binding;
    private UserViewModel userViewModel;
    private UserListAdapter adapter;

    private BookingViewModel bookingViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        adapter = new UserListAdapter(new UserListAdapter.OnUserActionListener() {
            @Override
            public void onEditClicked(UserEntity user) {
                showEditUserDialog(user);
            }

            @Override
            public void onDeleteClicked(UserEntity user) {
                showDeleteConfirmation(user);
            }

            @Override
            public void onBookingHistoryClicked(UserEntity user) {
                bookingViewModel.getBookingsByUserId(user.userId).observe(getViewLifecycleOwner(), bookings -> {
                    if (bookings == null || bookings.isEmpty()) {
                        Toast.makeText(getContext(), "No bookings found for " + user.fullName, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_booking_history, null);
                    RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_booking_history);
                    Button btnClose = dialogView.findViewById(R.id.btn_close);

                    BookingHistoryAdapter adapter = new BookingHistoryAdapter();
                    adapter.setBookingList(bookings);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);

                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setView(dialogView)
                            .setCancelable(false)
                            .create();

                    btnClose.setOnClickListener(v -> dialog.dismiss());

                    dialog.show();
                });
            }

        });

        binding.recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerUsers.setAdapter(adapter);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            adapter.setUserList(users);
        });
    }

    private void showEditUserDialog(UserEntity user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit User");

        // Container
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Name input
        EditText inputName = new EditText(getContext());
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputName.setHint("Full Name");
        inputName.setText(user.fullName);
        layout.addView(inputName);

        // Role input
        EditText inputRole = new EditText(getContext());
        inputRole.setInputType(InputType.TYPE_CLASS_TEXT);
        inputRole.setHint("Role (user/admin)");
        inputRole.setText(user.role);
        layout.addView(inputRole);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newRole = inputRole.getText().toString().trim();

            if (!newName.isEmpty() && !newRole.isEmpty()) {
                user.fullName = newName;
                user.role = newRole;
                userViewModel.updateUser(user);
                Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showDeleteConfirmation(UserEntity user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    userViewModel.deleteUser(user);
                    Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
