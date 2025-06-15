package com.example.flyeasy.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.SupportMessageEntity;
import com.example.flyeasy.viewmodel.SupportViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminSupportAdapter extends RecyclerView.Adapter<AdminSupportAdapter.AdminViewHolder> {

    private List<SupportMessageEntity> messages;
    private SupportViewModel supportViewModel;
    private Context context;

    public AdminSupportAdapter(Context context, SupportViewModel viewModel) {
        this.context = context;
        this.supportViewModel = viewModel;
    }

    public void setMessages(List<SupportMessageEntity> list) {
        this.messages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_support_message, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        SupportMessageEntity item = messages.get(position);

        holder.userText.setText("User ID: " + item.userId);
        holder.messageText.setText("Message: " + item.message);
        holder.timestampText.setText(formatTime(item.timestamp));

        if (item.reply != null && !item.reply.isEmpty()) {
            holder.replyText.setText("Reply: " + item.reply);
            holder.replyText.setVisibility(View.VISIBLE);
        } else {
            holder.replyText.setText("No reply yet");
            holder.replyText.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            showReplyDialog(item);
        });
    }

    private void showReplyDialog(SupportMessageEntity item) {
        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter reply");
        input.setText(item.reply);

        new AlertDialog.Builder(context)
                .setTitle("Reply to User " + item.userId)
                .setView(input)
                .setPositiveButton("Send Reply", (dialog, which) -> {
                    String reply = input.getText().toString().trim();
                    if (!reply.isEmpty()) {
                        item.reply = reply;
                        supportViewModel.updateReply(item);
                        Toast.makeText(context, "Reply sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView userText, messageText, replyText, timestampText;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.textUserId);
            messageText = itemView.findViewById(R.id.textMessage);
            replyText = itemView.findViewById(R.id.textReply);
            timestampText = itemView.findViewById(R.id.textTimestamp);
        }
    }
}
