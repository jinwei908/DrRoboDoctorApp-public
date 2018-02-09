package com.teamstartup.startup.drrobodoctor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
import 	android.widget.ArrayAdapter;
import 	android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException   ;
import org.json.JSONObject;

public class PrescriptionIssue extends AppCompatActivity {

    JSONArray responseArray = null;
    TextView[] userDetails = new TextView[5];
    ImageView userImage = null;
    private LinearLayout personDetailsHolder;

    //user details
    String userImageURL = "";
    String nric = "";
    String userName = "";
    Double funds = 0.0;
    String dateModified = "";
    int userID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_issue);
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

        //init user details
        userImage = (ImageView)findViewById(R.id.person_image);
        userDetails[0] = (TextView)findViewById(R.id.person_name);
        userDetails[1] = (TextView)findViewById(R.id.person_nric);
        userDetails[2] = (TextView)findViewById(R.id.person_funds);
        userDetails[3] = (TextView)findViewById(R.id.date_modified);

        personDetailsHolder = (LinearLayout)findViewById(R.id.person_details_frame);
        personDetailsHolder.setVisibility(View.GONE);

        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate3 = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getallusers.php"});
    }

    public void processFinish(String output) {
        //Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        responseArray = null;
        String items[] = null;
        try {
            responseArray = new JSONArray(output);
            items = new String[responseArray.length()];
            for (int i = 0; i < responseArray.length(); i++) {
                //medicationData += "<h1>1x " + responseArray.getJSONObject(i).getString("dosage_name")+"</h1>\n";
                //add to drop down
                items[i] = responseArray.getJSONObject(i).getString("personName");
            }
        } catch (Exception e) {


            Toast.makeText(this, "Error converting response to JSON: " + output, Toast.LENGTH_SHORT).show();
        }
        //convert to array of objects or strings
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set details
                SetPersonalDetails((int)id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void SetPersonalDetails(int id){
        personDetailsHolder.setVisibility(View.VISIBLE);
        userImage.setVisibility(View.VISIBLE);
        try {
            userName = responseArray.getJSONObject(id).getString("personName");
            funds = responseArray.getJSONObject(id).getDouble("funds");
            dateModified = responseArray.getJSONObject(id).getString("dateMod");
            userID = responseArray.getJSONObject(id).getInt("ID");
            userImageURL = responseArray.getJSONObject(id).getString("image");

            userDetails[0].setText("Name: " + userName);
            userDetails[1].setText("NRIC: " + responseArray.getJSONObject(id).getString("personNRIC"));
            userDetails[2].setText("Funds: " + funds);
            userDetails[3].setText("Date Modified: " + dateModified);

            if (!responseArray.getJSONObject(id).getString("image").equals("")) {
                new com.teamstartup.startup.drrobo.GetUrlImageTask(userImage).execute(new String[]{responseArray.getJSONObject(id).getString("image")});
                //Toast.makeText(this, "Set User Image", Toast.LENGTH_SHORT).show();
            } else {
                //Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image);
                //userImage.setImageBitmap(bImage);
                //userImage.invalidate();
                //Toast.makeText(this, "Set Default Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Error converting response to JSON",  Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenCom1(View view){
        Toast.makeText(this, "Sending Prescription (Cough Syrup) to: " + userName,  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/issueprescription.php?uID=" + userID + "&com=0";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate4 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();

    }

    public void OpenCom2(View view){
        Toast.makeText(this, "Sending Prescription (Aspirin) to: " + userName,  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/issueprescription.php?uID=" + userID + "&com=2";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate4 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();
    }

    public void OpenCom3(View view){
        Toast.makeText(this, "Sending Prescription (Birth Control) to: " + userName,  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/issueprescription.php?uID=" + userID + "&com=1";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate4 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();
    }

    public void OpenUserHistory(View view){
        Toast.makeText(this, "Going to User History",  Toast.LENGTH_SHORT).show();
        Intent uHistory = new Intent(this, UserHistory.class);
        uHistory.putExtra("NRIC", nric);
        uHistory.putExtra("USERNAME", userName);
        uHistory.putExtra("FUNDS", funds);
        uHistory.putExtra("DATE", dateModified);
        uHistory.putExtra("USERID", userID);
        uHistory.putExtra("IMAGE", userImageURL);
        startActivity(uHistory);
    }

    public void Back_Page(View view){
        finish();
    }

    private void PlayVoice(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.prescription_sent);
        mp.start();
    }

    public void processFinish2(String output){
        Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
    }


}
