package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.NotificationEntity;
import com.example.flyeasy.viewmodel.NotificationViewModel;
import com.example.flyeasy.ui.adapters.NotificationAdapter;
import com.example.flyeasy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;
    private int currentUserId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification_list, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        SessionManager session = new SessionManager(getContext());
        currentUserId = session.getUserId();

        viewModel.getAllNotifications().observe(getViewLifecycleOwner(), new Observer<List<NotificationEntity>>() {
            @Override
            public void onChanged(List<NotificationEntity> allNotifications) {
                List<NotificationEntity> userNotifications = new ArrayList<>();
                for (NotificationEntity notification : allNotifications) {
                    if (notification.getUserId() == -1 || notification.getUserId() == currentUserId) {
                        userNotifications.add(notification);
                    }
                }
                adapter.setNotifications(userNotifications);
            }
        });

        //  Swipe-to-delete for user
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // We’re not moving anything
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NotificationEntity notification = adapter.getNotificationAt(viewHolder.getAdapterPosition());
                viewModel.deleteNotification(notification);
                Toast.makeText(getContext(), "Notification deleted", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }
}
