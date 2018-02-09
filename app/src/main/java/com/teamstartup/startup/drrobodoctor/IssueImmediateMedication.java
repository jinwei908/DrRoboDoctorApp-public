package com.teamstartup.startup.drrobodoctor;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class IssueImmediateMedication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_immediate_medication);
    }

    public void OpenCom1(View view){
        Toast.makeText(this, "Opening Compartment 1",  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/opencompartment.php?com=0";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate2 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();
    }

    public void OpenCom2(View view){
        Toast.makeText(this, "Opening Compartment 2",  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/opencompartment.php?com=2";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate2 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();
    }

    public void OpenCom3(View view){
        Toast.makeText(this, "Opening Compartment 3",  Toast.LENGTH_SHORT).show();
        String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/opencompartment.php?com=1";
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate2 = this;
        URLTask.execute(new String[]{url});
        PlayVoice();
    }

    public void Back_Page(View view){
        finish();
    }

    private void PlayVoice(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.prescription_sent);
        mp.start();
    }

    protected void processFinish(String output){
       // Toast.makeText(this, "Opening... " + output,  Toast.LENGTH_SHORT).show();
    }


}
