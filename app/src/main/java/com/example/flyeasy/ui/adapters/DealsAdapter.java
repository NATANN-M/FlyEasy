package com.example.flyeasy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.Deal;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<Deal> deals;

    public DealsAdapter(List<Deal> deals) {
        this.deals = deals;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal_banner, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.tvDealTitle.setText(deal.getTitle());
        holder.imgDeal.setImageResource(deal.getImageResId());
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDeal;
        TextView tvDealTitle;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDeal = itemView.findViewById(R.id.imgDeal);
            tvDealTitle = itemView.findViewById(R.id.tvDealTitle);
        }
    }
}

