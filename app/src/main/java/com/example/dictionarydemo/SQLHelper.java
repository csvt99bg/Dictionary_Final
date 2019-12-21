package com.example.dictionarydemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dictionarydemo.Model.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME_TABLE = "Dictionary";
    static final String DB_NAME = "Dictionary.db";
    static final int VERSION= 2;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;
    public SQLHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Dictionary = "CREATE TABLE Dictionary ( "+
                "vietnamese TEXT," +
                "english TEXT )";
        db.execSQL(Dictionary);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!= newVersion){
            db.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
    public void insertWords(String vietnamese, String english){
        sqLiteDatabase= getWritableDatabase();
        contentValues= new ContentValues();
        contentValues.put("vietnamese",vietnamese);
        contentValues.put("english",english);

        sqLiteDatabase.insert(DB_NAME_TABLE,null, contentValues);
        closeDB();
    }
    public List<Dictionary> getAllDictionary(){
        List<Dictionary> dictionaryList= new ArrayList<>();
        sqLiteDatabase= getReadableDatabase();
        cursor= sqLiteDatabase.query(false, DB_NAME_TABLE, null, null, null,
                null, null, null, null);
        while (cursor.moveToNext()){
            String vietnamese= cursor.getString(cursor.getColumnIndex("vietnamese"));
            String english= cursor.getString(cursor.getColumnIndex("english"));
            dictionaryList.add(new Dictionary(vietnamese, english));
        }
        return dictionaryList;
    }
    public int deleteItemSave(String vietnamese){
        sqLiteDatabase= getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE, "vietnamese=?", new String[]{vietnamese});
    }
    public boolean deleteAll() {
        int result;
        sqLiteDatabase = getWritableDatabase();
        result = sqLiteDatabase.delete(DB_NAME_TABLE, null, null);
        closeDB();
        if (result == 1)
            return true;
        else return false;
    }
    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
