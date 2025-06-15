// ui/adapters/SavedFlightAdapter.java
package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.SavedFlightEntity;

public class SavedFlightAdapter extends ListAdapter<SavedFlightEntity, SavedFlightAdapter.SavedFlightViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(SavedFlightEntity flight);
    }

    private final OnDeleteClickListener deleteClickListener;

    public SavedFlightAdapter(OnDeleteClickListener listener) {
        super(DIFF_CALLBACK);
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public SavedFlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedFlightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_flight, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedFlightViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class SavedFlightViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;
        Button btnDelete;

        public SavedFlightViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtFlightInfo);
            btnDelete = itemView.findViewById(R.id.btnDeleteFlight);
        }

        void bind(SavedFlightEntity flight) {
            txtInfo.setText(flight.getAirline() + ": " + flight.getOrigin() + " → " + flight.getDestination());
            btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(flight));
        }
    }

    private static final DiffUtil.ItemCallback<SavedFlightEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SavedFlightEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull SavedFlightEntity oldItem, @NonNull SavedFlightEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull SavedFlightEntity oldItem, @NonNull SavedFlightEntity newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
