package com.example.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Model.Statics;

public class MainActivity extends AppCompatActivity {

    Intent showMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Statics.userId="Manor&Avi";
        onSharedIntent();
        showMe=new Intent(this,DisplayData.class);
        Button show=findViewById(R.id.showData);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(showMe);
            }
        });
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
