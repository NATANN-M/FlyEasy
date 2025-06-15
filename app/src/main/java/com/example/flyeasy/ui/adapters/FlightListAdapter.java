package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.FlightEntity;

import java.util.ArrayList;
import java.util.List;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {

    private List<FlightEntity> flightList = new ArrayList<>();
    private boolean isLocalList = false;
    private FlightClickListener listener;

    public interface FlightClickListener {
        void onSaveClicked(FlightEntity flight);
        void onEditClicked(FlightEntity flight);
        void onDeleteClicked(FlightEntity flight);
    }

    public FlightListAdapter(FlightClickListener listener) {
        this.listener = listener;
    }

    public void setFlightList(List<FlightEntity> flights, boolean isLocal) {
        this.flightList = flights;
        this.isLocalList = isLocal;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightEntity flight = flightList.get(position);
        holder.bind(flight);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlightInfo;
        Button btnSave, btnEdit, btnDelete;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlightInfo = itemView.findViewById(R.id.tv_flight_info);
            btnSave = itemView.findViewById(R.id.btn_save);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(FlightEntity flight) {
            String info = "From: " + flight.origin +
                    "\nTo: " + flight.destination +
                    "\nDeparture: " + flight.departureTime +
                    "\nArrival: " + flight.arrivalTime +
                    "\nPrice: $" + flight.price +
                    "\nSeats: " + flight.seatsAvailable;

            tvFlightInfo.setText(info);

            if (isLocalList) {
                btnSave.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);

                btnEdit.setOnClickListener(v -> listener.onEditClicked(flight));
                btnDelete.setOnClickListener(v -> listener.onDeleteClicked(flight));
            } else {
                btnSave.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);

                btnSave.setOnClickListener(v -> listener.onSaveClicked(flight));
            }
        }
    }
}
