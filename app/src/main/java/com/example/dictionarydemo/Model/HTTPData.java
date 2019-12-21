package com.example.dictionarydemo.Model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPData{
    public HTTPData() {
    }

    public List<String> getHTTPData(String urlString){
        List<String> lines = new ArrayList<>();
        try {
            URL url= new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in= new BufferedInputStream(connection.getInputStream());
                BufferedReader r= new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line=r.readLine())!=null){
                    if(line.indexOf(":")!=-1)
                        lines.add(line);
                }
                connection.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
}
