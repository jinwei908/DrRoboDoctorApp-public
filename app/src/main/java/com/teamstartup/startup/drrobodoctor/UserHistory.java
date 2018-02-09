package com.teamstartup.startup.drrobodoctor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import 	java.text.DateFormat;
import 	java.text.SimpleDateFormat;
import 	java.text.Format;
import 	java.util.Date;

public class UserHistory extends AppCompatActivity {

    Button historyButton = null;
    TextView historyText = null;
    //user details
    String nric = "";
    String userName = "";
    String userImageURL = "";
    Double funds = 0.0;
    String dateModified = "";
    int userID = 0;
    String historyData = "";
    int totalMedication = 0;
    JSONArray responseArray;

    TextView[] userDetails = new TextView[5];
    ImageView userImage = null;
    private LinearLayout personDetailsHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
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

        historyText = (TextView)findViewById(R.id.history_text);
        historyButton = (Button)findViewById(R.id.history_button);

        Bundle bundles = getIntent().getExtras();
        if(bundles != null)
        {
            nric = bundles.getString("NRIC");
            userName = bundles.getString("USERNAME");
            funds = bundles.getDouble("FUNDS");
            dateModified = bundles.getString("DATE");
            userID = bundles.getInt("USERID");
            userImageURL = bundles.getString("IMAGE");
        }

        //Display data
        userImage = (ImageView)findViewById(R.id.person_image);
        userDetails[0] = (TextView)findViewById(R.id.person_name);
        userDetails[1] = (TextView)findViewById(R.id.person_nric);
        userDetails[2] = (TextView)findViewById(R.id.person_funds);
        userDetails[3] = (TextView)findViewById(R.id.date_modified);
        personDetailsHolder = (LinearLayout)findViewById(R.id.person_details_frame);

        userDetails[0].setText("Name: " + userName);
        userDetails[1].setText("NRIC: " + nric);
        userDetails[2].setText("Funds: " + funds);
        userDetails[3].setText("Date Modified: " + dateModified);

        new com.teamstartup.startup.drrobo.GetUrlImageTask(userImage).execute(new String[]{userImageURL});


        //get data
        GetData();
    }

    protected void GetData(){
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate5 = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getuserhistory.php?uID="+userID});
    }

    public void processFinish(String output){
        //Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
        JSONObject responseJSON = null;
        responseArray = null;
        totalMedication = 0;
        historyData = "<h1>Singapore Polytechnic Dr Robo</h1>\n\n";
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
        SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            responseArray = new JSONArray(output);
            for(int i=0; i<responseArray.length(); i++){
                historyData += "<h2>1x " + responseArray.getJSONObject(i).getString("dosage_name")+"</h2>\n";
                String date = format.format(informat.parse(responseArray.getJSONObject(i).getString("date_added")));
                historyData += "<b>Date Prescribed: </b>" + date + "\n";
                historyData += "-------------------------------------------------------------";
                totalMedication += 1;
            }
            historyData += "\n\n<h2>Total Prescriptions: " + totalMedication + "</h2>";
        } catch(Exception e) {
            Toast.makeText(this, "Error converting response to JSON: " + output, Toast.LENGTH_SHORT).show();
        }
        //convert to array of objects or strings
        Spanned htmlAsSpanned = Html.fromHtml(historyData);
        historyText.setText(htmlAsSpanned);
    }

    protected void onClick_Back(View view){
        finish();
    }
}
