package com.matainja.bootapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.matainja.bootapplication.Model.TerminalModel;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.activity.TerminalShowActivity;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder>{
    private List<TerminalModel> gridItemList;
    private Context context;

    public DisplayAdapter(Context context, List<TerminalModel> gridItemList) {
        this.context = context;
        this.gridItemList = gridItemList;
    }

    @NonNull
    @Override
    public DisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_list_row, parent, false);
        return new DisplayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TerminalModel item = gridItemList.get(position);

        holder.imageView.setImageResource(item.getImageResource());
        Log.e("Tag","item.getItemName()"+item.getItemName());
        holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
        holder.textView.setText(item.getItemName());
        holder.terminalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("queue_terminal","app_id>>>"+gridItemList.get(position).getAppId());
                Intent intent = new Intent(context, TerminalShowActivity.class);
                // You can also pass data to the new activity if needed
                intent.putExtra("item", gridItemList.get(position).getItemName());
                intent.putExtra("appId", gridItemList.get(position).getAppId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gridItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        RelativeLayout terminalCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            terminalCard= itemView.findViewById(R.id.terminalCard);
        }
    }
}
