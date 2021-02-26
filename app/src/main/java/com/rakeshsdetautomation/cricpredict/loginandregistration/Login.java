package com.rakeshsdetautomation.cricpredict.loginandregistration;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public Login(MainActivity mainActivity) throws ClientProtocolException, IOException {
        progressDialog = new ProgressDialog(mainActivity);
    }

    @Override
    protected void onPreExecute(){
        progressDialog.setMessage("Logging in.");
        progressDialog.setTitle("Loading...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        String url = strings[0];
        String jsonBody = strings[1];

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected  void onPostExecute(String result){

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
