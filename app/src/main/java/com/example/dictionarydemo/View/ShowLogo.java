package com.example.dictionarydemo.View;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.dictionarydemo.Model.Dictionary;
import com.example.dictionarydemo.Model.HTTPData;
import com.example.dictionarydemo.R;
import com.example.dictionarydemo.SQLHelper;
import com.example.dictionarydemo.databinding.ShowLogoBinding;

import java.util.List;

public class ShowLogo extends Activity {
    ShowLogoBinding binding;
    SQLHelper sqlHelper;
    List<Dictionary> dictionaryList;
    // Set Time Waitting
    private final int TimeWait = 3000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.show_logo);
        binding= DataBindingUtil.setContentView(this, R.layout.show_logo);
        /*sqlHelper= new SQLHelper(getBaseContext());
        dictionaryList= sqlHelper.getAllDictionary();
        if(dictionaryList.size()!=0){
            binding.layoutLoad.setVisibility(View.INVISIBLE);
                startAct(TimeWait);
        }
        else {
            binding.layoutLoad.setVisibility(View.VISIBLE);
            new LoadData().execute(Define.LINK_API);
            startAct(TimeWait);
        }*/
        startAct(TimeWait);
    }

    private class LoadData extends AsyncTask<String, Void, List<String>>{

        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> result;
            HTTPData httpData= new HTTPData();
            result= httpData.getHTTPData(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            if(strings!=null){
                binding.layoutLoad.setVisibility(View.INVISIBLE);
                for (String str:strings){
                    int indexToCut= str.indexOf(":");
                    String vietnamese= str.substring(0, indexToCut);
                    String english= str.substring(indexToCut+2);
                    sqlHelper.insertWords(vietnamese, english);
                }
            }
        }
    }


    private void startAct(int time){
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(ShowLogo.this, MainActivity.class);
                ShowLogo.this.startActivity(intent);
                ShowLogo.this.finish();
            }
        }, time);
    }
}
