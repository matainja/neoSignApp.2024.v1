package com.matainja.bootapplication.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.matainja.bootapplication.Model.TerminalModel;
import com.matainja.bootapplication.R;

import java.util.List;

public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.ViewHolder> {

    private List<TerminalModel> gridItemList;
    private Context context;

    public TerminalAdapter(Context context, List<TerminalModel> gridItemList) {
        this.context = context;
        this.gridItemList = gridItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.terminal_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TerminalModel item = gridItemList.get(position);

        holder.imageView.setImageResource(item.getImageResource());
        Log.e("Tag","item.getItemName()"+item.getItemName());
        holder.textView.setTextColor(Color.parseColor("#000000"));
        holder.textView.setText(item.getItemName());
        holder.terminalCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public int getItemCount() {
        return gridItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView terminalCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            terminalCard= itemView.findViewById(R.id.terminalCard);
        }
    }
}
