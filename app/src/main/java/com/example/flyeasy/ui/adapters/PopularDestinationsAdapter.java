package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.Destination;

import java.util.List;

public  class PopularDestinationsAdapter extends RecyclerView.Adapter<PopularDestinationsAdapter.DestinationViewHolder> {

    private List<Destination> destinations;

    public PopularDestinationsAdapter(List<Destination> destinations) {
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.tvDestinationName.setText(destination.getName());
        holder.imgDestination.setImageResource(destination.getImageResId());
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDestination;
        TextView tvDestinationName;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDestination = itemView.findViewById(R.id.imgDestination);
            tvDestinationName = itemView.findViewById(R.id.tvDestinationName);
        }
    }
}
