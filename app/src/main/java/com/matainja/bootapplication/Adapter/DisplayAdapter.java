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

import com.matainja.bootapplication.Model.DisplayDataModel;
import com.matainja.bootapplication.Model.TerminalModel;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.activity.TerminalShowActivity;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder>{
    private List<DisplayDataModel> gridItemList;
    private Context context;

    public DisplayAdapter(Context context, List<DisplayDataModel> gridItemList) {
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
        DisplayDataModel item = gridItemList.get(position);

        holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
        holder.textView.setText(item.getQueue_id());
        holder.textView1.setTextColor(Color.parseColor("#FF0000"));
        holder.textView1.setText(item.getCounter_id());
        holder.textView2.setTextColor(Color.parseColor("#FF0000"));
        holder.textView2.setText(">>");

    }

    @Override
    public int getItemCount() {
        return gridItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView,textView1,textView2;
        RelativeLayout terminalCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            terminalCard= itemView.findViewById(R.id.terminalCard);
        }
    }
}
