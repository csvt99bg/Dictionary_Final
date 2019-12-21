package com.example.dictionarydemo.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionarydemo.Adapter.AdapterSearch;
import com.example.dictionarydemo.Interface.IOnClickWord;
import com.example.dictionarydemo.Model.Dictionary;
import com.example.dictionarydemo.R;
import com.example.dictionarydemo.SQLHelper;
import com.example.dictionarydemo.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Dictionary> dictionary;
    SQLHelper sqlHelper;
    AdapterSearch adapter;
    boolean doubleBackToExitPressedOnce = false;
    int size;
    boolean changeTranslator= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

        //loadData
        readData2();
        size= dictionary.size();
        binding.etSearch.setInputType(InputType.TYPE_NULL);
        sqlHelper= new SQLHelper(getBaseContext());
        editText();
        randomWords();
        setChangeTranslator();
        binding.btCancel.setVisibility(View.INVISIBLE);
        binding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.etSearch.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(binding.etSearch, InputMethodManager.SHOW_FORCED);
                buildRcv();
                hideViewHome();
                binding.rcvSearch.setVisibility(View.INVISIBLE);
                binding.btCancel.setVisibility(View.VISIBLE);
            }
        });
        binding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewHome();
                binding.btCancel.setVisibility(View.INVISIBLE);
                binding.rcvSearch.setVisibility(View.INVISIBLE);
                binding.etSearch.setText("");
                binding.etSearch.setInputType(InputType.TYPE_NULL);
                hideKeyboard(MainActivity.this);
            }
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                    binding.rcvSearch.setVisibility(View.INVISIBLE);
                else {
                    hideViewHome();
                    binding.rcvSearch.setVisibility(View.VISIBLE);
                    filter(s.toString());
                }
            }
        });
        binding.btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), HistoryActivity.class);
                intent.putExtra("check", changeTranslator);
                startActivity(intent);
            }
        });
    }
    // gợi ý từ điển
    private void filter(String text){
        List<Dictionary> filterList= new ArrayList<>();
        if(changeTranslator==true) {
            for (Dictionary item : dictionary) {
                if (item.getEnglish().toLowerCase().indexOf(text.toLowerCase()) == 0) {
                    filterList.add(item);
                }
            }
        }else {
            for (Dictionary item : dictionary) {
                if (item.getVietnamese().toLowerCase().indexOf(text.toLowerCase()) == 0) {
                    filterList.add(item);
                }
            }

        }
        if(filterList.size()==0)
            filterList.add(new Dictionary("", ""));
        adapter.filterList(filterList);
    }
    private void editText(){
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    binding.etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    String str= binding.etSearch.getText().toString().toLowerCase();
                    Boolean check= false;
                    Dictionary WordIntent = null;
                    if(changeTranslator==false){
                        for (Dictionary word:dictionary){
                            if(word.getVietnamese().toLowerCase().trim().equals(str)){
                                check=true;
                                WordIntent= word;
                            }
                        }

                    }
                    else{
                        for (Dictionary word:dictionary){
                            if(word.getEnglish().toLowerCase().trim().equals(str)){
                                check=true;
                                WordIntent= word;
                            }
                        }
                    }
                    if(check==true)
                        intentActivity(WordIntent.getVietnamese(), WordIntent.getEnglish(), changeTranslator);
                    else
                        Toast.makeText(getBaseContext(), "Không có trong từ điển", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }
    private void randomWords(){
        final Random rd= new Random();
        final int[] index = {rd.nextInt(size)};
        binding.tvEnglish.setText(dictionary.get(index[0]).getEnglish());
        binding.tvVietnamese.setText(dictionary.get(index[0]).getVietnamese());
        binding.btRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index[0] = rd.nextInt(size);
                binding.tvEnglish.setText(dictionary.get(index[0]).getEnglish());
                binding.tvVietnamese.setText(dictionary.get(index[0]).getVietnamese());
            }
        });
        binding.layoutRandomWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentActivity(dictionary.get(index[0]).getVietnamese(),
                        dictionary.get(index[0]).getEnglish(), changeTranslator);
            }
        });
    }
    void hideViewHome(){
        binding.layoutRandomWord.setVisibility(View.INVISIBLE);
        binding.layoutButton.setVisibility(View.INVISIBLE);
    }
    void showViewHome(){
        binding.layoutRandomWord.setVisibility(View.VISIBLE);
        binding.layoutButton.setVisibility(View.VISIBLE);
    }
    void buildRcv(){
        binding.rcvSearch.setVisibility(View.VISIBLE);
        adapter= new AdapterSearch(dictionary, getBaseContext(), changeTranslator);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        binding.rcvSearch.setHasFixedSize(true);
        binding.rcvSearch.setLayoutManager(layoutManager);
        binding.rcvSearch.setAdapter(adapter);
        adapter.setOnClickWord(new IOnClickWord() {
            @Override
            public void onClick(Dictionary in4Word) {
                intentActivity(in4Word.getVietnamese(), in4Word.getEnglish(), changeTranslator);
            }
        });
    }
    void setChangeTranslator(){
        binding.changeTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeTranslator= false;
         binding.TranslatorToEngLish.setBackgroundResource(R.drawable.bo_tron_xam);
         binding.changeTranslator.setBackgroundResource(R.drawable.background_random_words);
            }
        });
        binding.TranslatorToEngLish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTranslator = true;
                binding.changeTranslator.setBackgroundResource(R.drawable.bo_tron_xam);
                binding.TranslatorToEngLish.setBackgroundResource(R.drawable.background_random_words);
            }
        });
    }
    public void readData2()
    {
        dictionary= new ArrayList<>();
        String data;
        InputStream in= getResources().openRawResource(R.raw.file);
        InputStreamReader inreader=new InputStreamReader(in);
        BufferedReader bufreader=new BufferedReader(inreader);
        int indexToCut;
        if(in!=null)
        {
            try
            {
                while((data=bufreader.readLine())!=null)
                {
                    indexToCut= data.indexOf(":");
                    if(indexToCut!=-1){
                        String vietnamese= data.substring(0, indexToCut);
                        String english= data.substring(indexToCut+2);
                        dictionary.add(new Dictionary(vietnamese, english));
                    }
                }
                in.close();
            }
            catch(IOException ex){
                Log.e("ERROR", ex.getMessage());
            }
        }

    }
    void intentActivity(String vietnamese, String english, Boolean check){
        List<Dictionary>history= sqlHelper.getAllDictionary();
        if(vietnamese.equals("")==false){
            Intent translator= new Intent(getBaseContext(), TranslatorActivity.class);
            translator.putExtra("vietnamese", vietnamese);
            translator.putExtra("english", english);
            translator.putExtra("checkTranslator", check);

            startActivity(translator);
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

            for (Dictionary word:history){
                if(vietnamese.equals(word.getVietnamese()))
                    sqlHelper.deleteItemSave(vietnamese);
            }
            sqlHelper.insertWords(vietnamese, english);
        }
    }

    // ẩn bàn phím khi click ra ngoài edittext
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()){
                hideKeyboard(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);

        }
    }
    // ấn 2 lần trong 2s để thoát :v
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.mess, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
