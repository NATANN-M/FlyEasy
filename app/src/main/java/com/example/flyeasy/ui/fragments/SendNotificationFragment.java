package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flyeasy.R;
import com.example.flyeasy.model.NotificationEntity;
import com.example.flyeasy.util.NotificationUtils;
import com.example.flyeasy.viewmodel.NotificationViewModel;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SendNotificationFragment extends Fragment {

    private EditText etTitle, etMessage, etUserId;
    private NotificationViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_notification, container, false);

        etTitle = root.findViewById(R.id.etNotificationTitle);
        etMessage = root.findViewById(R.id.etNotificationMessage);
        etUserId = root.findViewById(R.id.etUserId);
        Button btnSend = root.findViewById(R.id.btnSendNotification);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        btnSend.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            String userIdInput = etUserId.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
                Toast.makeText(getContext(), "Please fill in title and message", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = -1; // -1 means "send to all users"
            if (!TextUtils.isEmpty(userIdInput)) {
                try {
                    userId = Integer.parseInt(userIdInput);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "User ID must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            NotificationEntity notification = new NotificationEntity(
                    title,
                    message,
                    currentTime,
                    false,
                    userId// -1 = ALL


            );



            viewModel.insertNotification(notification);


            //notification on phone notification bar

            NotificationUtils.showNotification(requireContext(), title, message);


            Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etMessage.setText("");
            etUserId.setText("");
        });


        return root;
    }
}
