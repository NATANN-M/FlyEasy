package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<UserEntity> userList = new ArrayList<>();

    public interface OnUserActionListener {
        void onEditClicked(UserEntity user);
        void onDeleteClicked(UserEntity user);
        void onBookingHistoryClicked(UserEntity user);
    }

    private OnUserActionListener listener;

    public UserListAdapter(OnUserActionListener listener) {
        this.listener = listener;
    }

    public void setUserList(List<UserEntity> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserEntity user = userList.get(position);
        holder.name.setText(user.fullName);
        holder.email.setText(user.email);
        holder.role.setText("Role: " + user.role);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClicked(user));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClicked(user));
        holder.btnBookingHistory.setOnClickListener(v -> listener.onBookingHistoryClicked(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, role;
        Button btnEdit, btnDelete, btnBookingHistory;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            email = itemView.findViewById(R.id.text_email);
            role = itemView.findViewById(R.id.text_role);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnBookingHistory = itemView.findViewById(R.id.btn_booking_history);
        }
    }
}
