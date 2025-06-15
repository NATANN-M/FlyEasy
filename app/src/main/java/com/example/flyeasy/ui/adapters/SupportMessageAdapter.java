package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.SupportMessageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportMessageAdapter extends RecyclerView.Adapter<SupportMessageAdapter.SupportViewHolder> {

    private List<SupportMessageEntity> messages;

    public void setMessages(List<SupportMessageEntity> list) {
        this.messages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support_message, parent, false);
        return new SupportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportViewHolder holder, int position) {
        SupportMessageEntity item = messages.get(position);
        holder.messageText.setText("You: " + item.message);

        if (item.reply != null && !item.reply.isEmpty()) {
            holder.replyText.setText("Admin: " + item.reply);
            holder.replyText.setVisibility(View.VISIBLE);
        } else {
            holder.replyText.setVisibility(View.GONE);
        }

        holder.timeText.setText(formatTime(item.timestamp));
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    static class SupportViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, replyText, timeText;

        public SupportViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textMessage);
            replyText = itemView.findViewById(R.id.textReply);
            timeText = itemView.findViewById(R.id.textTimestamp);
        }
    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
