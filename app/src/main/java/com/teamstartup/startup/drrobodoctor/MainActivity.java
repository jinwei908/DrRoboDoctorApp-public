package com.teamstartup.startup.drrobodoctor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import android.os.Handler;
import android.widget.TextView;

import org.json.JSONObject;
import 	java.text.DateFormat;
import 	java.text.SimpleDateFormat;
import 	java.text.Format;
import 	java.util.Date;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    TextView boschText = null;
    TextView temperatureText = null;
    TextView humidityText = null;
    TextView noiseText = null;

    String boschData = "";
    int lastUpdated = 0;

    String dateString = "";
    double temperature = 0;
    double humidity = 0;
    double noiseLevel = 0;

    boolean temperatureUp = false;
    boolean humidityUp = false;
    boolean noiseLevelUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boschText = (TextView)findViewById(R.id.bosch_text);

        temperatureText = (TextView)findViewById(R.id.temperature_text);
        humidityText = (TextView)findViewById(R.id.humidity_text);
        noiseText = (TextView)findViewById(R.id.noise_text);

        GetBoschXDKData();
        UpdateLastUpdated();
    }


    public void onClick_ImmediateMeds(View view){
        Toast.makeText(this, "Going to Immediate Meds",  Toast.LENGTH_SHORT).show();
        Intent immediateMeds = new Intent(this, IssueImmediateMedication.class);
        startActivity(immediateMeds);
    }

    public void onClick_Prescription(View view){
        Toast.makeText(this, "Going to Prescriptions",  Toast.LENGTH_SHORT).show();
        Intent prescriptionMeds = new Intent(this, PrescriptionIssue.class);
        startActivity(prescriptionMeds);
    }

    public void onClick_Inventory(View view){
        Toast.makeText(this, "Going to Prescriptions",  Toast.LENGTH_SHORT).show();
        Intent inventoryView = new Intent(this, InventoryActivity.class);
        startActivity(inventoryView);
    }

    public void onClick_Listen(View view){
        //Toast.makeText(this, "Going to Prescriptions",  Toast.LENGTH_SHORT).show();
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sinch.android.rtc.sample.video");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    protected void processFinish(String output){
        Toast.makeText(this, "Opening... " + output,  Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GetBoschXDKData();

        }
    };

    private Runnable runLastUpdated = new Runnable() {
        @Override
        public void run() {
            UpdateLastUpdated();

        }
    };

    private void GetBoschXDKData(){
        handler.postDelayed(runnable, 5000);
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate7 = this;
        URLTask.execute(new String[]{"http://52.187.51.158:8082/mongodb/boschxdk04/latestdata"});
    }

    private void UpdateLastUpdated(){
        handler2.postDelayed(runLastUpdated, 1000);
        lastUpdated += 1;
        boschData = "";
        boschData += "<h1>Venue: Singapore Polytechnic</h1><br/>";
        boschData += "<h2>Date: " + dateString + "</h2><br/>";
        boschData += "<p>Last Updated: " + lastUpdated + " seconds ago.</p>";

        temperatureText.setText(String.valueOf(temperature));
        humidityText.setText(String.valueOf(humidity));
        noiseText.setText(String.valueOf(noiseLevel));
//        boschData += "<b>Temperature: </b>" + temperature + "<br/>";
//        boschData += "<b>Humidity: </b>" + humidity + "<br/>";
//        boschData += "<b>Noise Level: </b>" + noiseLevel + "<br/>";

        //Process Finish
        Spanned htmlAsSpanned = Html.fromHtml(boschData);
        boschText.setText(htmlAsSpanned);
    }

    public void processBosch(String output){
        //personDetails.setText(output);
        //Toast.makeText(this, "Processed: " + output,  Toast.LENGTH_SHORT).show();
        JSONObject responseJSON = null;
        boschData = "";
        try {
            responseJSON = new JSONObject(output);
            Date date = new Date(responseJSON.getLong("updatetime"));
            SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
            //change image
            if(temperature < responseJSON.getDouble("temperature")){
                temperatureText.setBackgroundResource(R.drawable.temperature_down);
            } else {
                temperatureText.setBackgroundResource(R.drawable.temperature_up);
            }
            if(humidity < responseJSON.getDouble("humidity")){
                humidityText.setBackgroundResource(R.drawable.humidty_down);
            } else {
                humidityText.setBackgroundResource(R.drawable.humidty_up);
            }
            if(noiseLevel < responseJSON.getDouble("noiselevel")){
                noiseText.setBackgroundResource(R.drawable.noise_down);
            } else {
                noiseText.setBackgroundResource(R.drawable.noise_up);
            }

            temperature = responseJSON.getDouble("temperature");
            humidity = responseJSON.getDouble("humidity");
            noiseLevel = responseJSON.getDouble("noiselevel");
            dateString = format.format(date);
            lastUpdated = 0;

            boschData += "<h1>Venue: Singapore Polytechnic</h1><br/>";
            boschData += "<h2>Date: " + dateString + "</h2><br/>";
            boschData += "<p>Last Updated: " + lastUpdated + " seconds ago.</p>";
            temperatureText.setText(String.valueOf(temperature));
            humidityText.setText(String.valueOf(humidity));
            noiseText.setText(String.valueOf(noiseLevel));
//        boschData += "<b>Temperature: </b>" + temperature + "<br/>";
//        boschData += "<b>Humidity: </b>" + humidity + "<br/>";
//        boschData += "<b>Noise Level: </b>" + noiseLevel + "<br/>";

        } catch(Exception e){
            Toast.makeText(this, "Error converting response to JSON: " + output,  Toast.LENGTH_SHORT).show();
        }
        //Process Finish
        Spanned htmlAsSpanned = Html.fromHtml(boschData);
        boschText.setText(htmlAsSpanned);

    }



}
