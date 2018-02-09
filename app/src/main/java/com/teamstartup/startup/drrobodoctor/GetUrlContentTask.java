package com.teamstartup.startup.drrobodoctor;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import android.widget.Toast;
/**
 * Created by Jinwei-PC on 1/10/2017.
 */

public class GetUrlContentTask extends AsyncTask<String, Integer, String> {

    public MainActivity delegate = null;
    public IssueImmediateMedication delegate2 = null;
    public PrescriptionIssue delegate3 = null;
    public PrescriptionIssue delegate4 = null;
    public UserHistory delegate5 = null;
    public InventoryActivity delegate6 = null;
    public MainActivity delegate7 = null;

    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        String content = "", line;
        try {
            URL url = new URL(urls[0]);
            //Log.e("ERROR", "URL: " + urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(urls[0] == "http://52.187.51.158:8082/mongodb/boschxdk04/latestdata" ? false : true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            //Log.e("ERROR", "BEFORE CONNECT");
            connection.connect();
            //Log.e("ERROR", "AFTER CONNECT: " + connection.getResponseCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            //Log.e("ERROR", content);
            return content;
        } catch (MalformedURLException e){
            //Log.e("ERROR", "MALFORMED: " + e.getMessage());
        } catch (IOException e){
            //Log.e("ERROR", "EXCEPTION: " + e.getMessage());
        } finally{
            connection.disconnect();
        }

        return content;
    }

    protected void onProgressUpdate(Integer... progress) {
    }


    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI he
        //displayMessage(result);
        // Toast.makeText(MainActivity.this, result,  Toast.LENGTH_SHORT).show();]
        if(delegate != null) {
            delegate.processFinish(result);
        } else if(delegate2 != null){
            delegate2.processFinish(result);
        } else if(delegate3 != null){
            delegate3.processFinish(result);
        } else if(delegate4 != null){
            delegate4.processFinish2(result);
        } else if(delegate5 != null){
            delegate5.processFinish(result);
        } else if(delegate6 != null){
            delegate6.processFinish(result);
        } else if(delegate7 != null){
            delegate7.processBosch(result);
        }
    }
}