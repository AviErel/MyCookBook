package com.example.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import Model.Statics;

public class MainActivity extends AppCompatActivity {

    Intent showMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent logginIntet = new Intent(this, LogginActivity.class);
        startActivity(logginIntet);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onSharedIntent();

        while (Statics.userId.equals("")){}

        showMe=new Intent(this,DisplayData.class);
        startActivity(showMe);
    }

    private void onSharedIntent() {
        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();

        if (receivedAction.equals(Intent.ACTION_SEND)) {

            // check mime type
            if (receivedType.startsWith("text/")) {

                String receivedText = receiverdIntent
                        .getStringExtra(Intent.EXTRA_TEXT);
                if (receivedText != null) {
                    Intent saveActivity=new Intent(this,SaveRecipe.class);
                    saveActivity.putExtra("uri",receivedText);
                    startActivity(saveActivity);
                }
            }

        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

            Log.e("", "onSharedIntent: nothing shared" );
        }
    }

}
