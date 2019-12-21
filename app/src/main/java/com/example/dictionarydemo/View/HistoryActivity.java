package com.example.dictionarydemo.View;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionarydemo.Adapter.AdapterHistory;
import com.example.dictionarydemo.Model.Dictionary;
import com.example.dictionarydemo.R;
import com.example.dictionarydemo.SQLHelper;
import com.example.dictionarydemo.databinding.ActivityHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;
    SQLHelper sqlHelper;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_history);
        sqlHelper= new SQLHelper(getBaseContext());

        binding.btBackHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        content= binding.tvToolbarHis.getText().toString();
        binding.tvToolbarHis.setText(content+" ("+sqlHelper.getAllDictionary().size()+")");
        buildRCVHistory();
        buildBtDelete();

    }
    void buildRCVHistory(){
        List<Dictionary> historyWord= new ArrayList<>();
        List<Dictionary> aa= sqlHelper.getAllDictionary();
        for (int i=aa.size()-1; i>=0;i--){
            historyWord.add(aa.get(i));
        }
        Boolean check= getIntent().getExtras().getBoolean("check");
        AdapterHistory adapterHistory= new AdapterHistory(historyWord, getBaseContext(), check);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        binding.rcvHistory.setLayoutManager(layoutManager);
        binding.rcvHistory.setAdapter(adapterHistory);

    }
    void buildBtDelete(){
        binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sqlHelper.deleteAll();
                binding.tvToolbarHis.setText(content+" ("+sqlHelper.getAllDictionary().size()+")");
               buildRCVHistory();
            }
        });
    }

}
