package com.example.dictionarydemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionarydemo.Interface.IOnClickWord;
import com.example.dictionarydemo.Model.Dictionary;
import com.example.dictionarydemo.R;

import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {
    List<Dictionary> dictionary;
    Context context;
    boolean checkTranslator;
    IOnClickWord onClickWord;

    public void setOnClickWord(IOnClickWord onClickWord) {
        this.onClickWord = onClickWord;
    }

    public AdapterSearch(List<Dictionary> dictionary, Context context, boolean checkTranslator) {
        this.dictionary = dictionary;
        this.context = context;
        this.checkTranslator= checkTranslator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_rcv_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Dictionary word= dictionary.get(position);
        if(checkTranslator==true)
            holder.tvSearch.setText(word.getEnglish());
        else
            holder.tvSearch.setText(word.getVietnamese());
        holder.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWord.onClick(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dictionary.size();
    }

    public void filterList(List<Dictionary> filterList) {
        dictionary= filterList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearch= itemView.findViewById(R.id.tvSearch);
        }
    }
}
