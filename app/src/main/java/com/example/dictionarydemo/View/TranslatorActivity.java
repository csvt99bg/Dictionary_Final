package com.example.dictionarydemo.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dictionarydemo.R;
import com.example.dictionarydemo.databinding.ActivityTranslatorBinding;

public class TranslatorActivity extends AppCompatActivity {
    ActivityTranslatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        binding= DataBindingUtil.setContentView(TranslatorActivity.this, R.layout.activity_translator);
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });
        setData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
    void setData(){
        Intent getData= getIntent();
        String vietnamese= getData.getStringExtra("vietnamese");
        String english= getData.getStringExtra("english");
        Boolean check= getData.getExtras().getBoolean("checkTranslator");
        if(check==true){
            binding.tvTop.setText(english);
            binding.tvBot.setText(vietnamese);
        }
        else {
            binding.tvTop.setText(vietnamese);
            binding.tvBot.setText(english);
        }
    }
}
