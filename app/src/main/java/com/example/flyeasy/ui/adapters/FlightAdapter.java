package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flyeasy.databinding.ItemFlightBinding;
import com.example.flyeasy.model.FlightEntity;

import java.util.ArrayList;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    public interface OnFlightClickListener {
        void onFlightClick(FlightEntity flight);
        void onBookClick(FlightEntity flight);
    }

    private final OnFlightClickListener listener;
    private List<FlightEntity> flightList = new ArrayList<>();

    public FlightAdapter(OnFlightClickListener listener) {
        this.listener = listener;
    }

    public void setFlightList(List<FlightEntity> flights) {
        this.flightList = (flights != null) ? flights : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFlightBinding binding = ItemFlightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FlightViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        holder.bind(flightList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        private final ItemFlightBinding binding;

        public FlightViewHolder(ItemFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FlightEntity flight, OnFlightClickListener listener) {
            binding.textAirline.setText(flight.airline);
            binding.textRoute.setText(flight.origin + " ➡ " +"   " + flight.destination);
            binding.textDeparture.setText("Departure: " + "   " +flight.departureTime);
            binding.textArrival.setText("Arrival: " +"   " + flight.arrivalTime);
            binding.textPrice.setText("ETB " +"   " + flight.price);

            String imageUrl = (flight.imageUrl != null && !flight.imageUrl.isEmpty())
                    ? flight.imageUrl
                    : "https://cdn.pixabay.com/photo/2017/03/13/13/41/airplane-2132610_1280.jpg";


            Glide.with(binding.imageAirplane.getContext())
                    .load(imageUrl)
                    .into(binding.imageAirplane);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onFlightClick(flight);
            });

            binding.btnBookNow.setOnClickListener(v -> {
                if (listener != null) listener.onBookClick(flight);
            });
        }
    }
}
