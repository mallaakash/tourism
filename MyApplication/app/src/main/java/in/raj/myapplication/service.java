package in.raj.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static in.raj.myapplication.R.raw.a;

public class service extends AppCompatActivity implements View.OnClickListener {

    Button b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);



        b2 = (Button)findViewById(R.id.button_feedback);
        b3 = (Button)findViewById(R.id.button_culture);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }

    public void onClick(View v2){
        if (v2 == b2){

            Intent i = new Intent(service.this, feedback.class);
            startActivity(i);
        }

        if (v2 == b3){
            Intent i = new Intent(service.this, imageproc.class);
            startActivity(i);
        }


    }


    public void onbuy(View v2) {
        if (v2.getId() == R.id.button_payment){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bookrajmonuments.in/index.php"));
            startActivity(browserIntent);
        }
    }

}
