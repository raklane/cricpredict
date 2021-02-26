package com.rakeshsdetautomation.cricpredict.matches;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PlayerName extends AsyncTask<String, String, String> {


    public static String playerName;

    public PlayerName() throws ClientProtocolException, IOException {

    }

    @Override
    protected String doInBackground(String... strings) {

        String argPlayerLink = strings[0];
        String profileString="";

        String url = "https://www.cricbuzz.com"+  argPlayerLink;

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(out);
                profileString = out.toString();
                out.close();
            }else{
                httpResponse.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        Document doc = (Document) Jsoup.parse(profileString);
        String title = doc.title();
        playerName = title.substring(0, title.indexOf("Profile")-1);
        return playerName;
    }

}
