package com.example.dictionarydemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionarydemo.Model.Dictionary;
import com.example.dictionarydemo.R;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    List<Dictionary> list;
    Context context;
    Boolean check;

    public AdapterHistory(List<Dictionary> list, Context context, Boolean check) {
        this.list = list;
        this.context = context;
        this.check= check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_rcv_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dictionary word= list.get(position);
        if(check==true){
            holder.tvAbove.setText(word.getEnglish());
            holder.tvBottom.setText(word.getVietnamese());
        }
        else {
            holder.tvAbove.setText(word.getVietnamese());
            holder.tvBottom.setText(word.getEnglish());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAbove;
        TextView tvBottom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAbove= itemView.findViewById(R.id.tvAbove);
            tvBottom= itemView.findViewById(R.id.tvBelow);
        }
    }
}
