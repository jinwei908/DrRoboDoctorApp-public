package com.teamstartup.startup.drrobodoctor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import android.widget.TextView;

public class InventoryActivity extends AppCompatActivity {
    JSONArray responseArray = null;
    TextView inventoryText = null;
    String inventoryData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inventoryText = (TextView)findViewById(R.id.inventory_text);

        GetData();
    }

    protected void onClick_Back(View view){
        finish();
    }


    protected void GetData(){
        //get data
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate6 = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getinventory.php"});
    }
    //get inventory
    protected void processFinish(String output){
        //Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
        responseArray = null;
        inventoryData = "<h1>Singapore Polytechnic Dr Robo</h1>\n\n";
        try {
            responseArray = new JSONArray(output);
            for(int i=0; i<responseArray.length(); i++){
                int invent = responseArray.getJSONObject(i).getInt("inventory");
                inventoryData += "<h2>1x " + responseArray.getJSONObject(i).getString("dosage_name")+"</h2>\n";
                inventoryData += "<b>Inventory: </b>" + invent + (invent <= 20 ? " (LOW)" : "") + "\n" ;
            }
        } catch(Exception e) {
            Toast.makeText(this, "Error converting response to JSON: " + output, Toast.LENGTH_SHORT).show();
        }
        //convert to array of objects or strings
        Spanned htmlAsSpanned = Html.fromHtml(inventoryData);
        inventoryText.setText(htmlAsSpanned);
    }
}
