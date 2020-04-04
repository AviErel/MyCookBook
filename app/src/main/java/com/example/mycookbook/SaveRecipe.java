package com.example.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class SaveRecipe extends AppCompatActivity {

    EditText header;
    EditText description;
    TextView uriText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String uriString = b.getString("uri");
        setContentView(R.layout.activity_save_recipe);
        header=(EditText)findViewById(R.id.header);
        uriText=(TextView)findViewById(R.id.uriData);
        uriText.setText(uriString);
        description=(EditText)findViewById(R.id.description);
        Button acceptButton=(Button)findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject recipe=new JSONObject();
                try{
                    recipe.put("header",header.getText());
                    recipe.put("uri",uriText.getText());
                    recipe.put("description",description.getText());
                    //send to Manor
                }catch (Exception e){}
            }
        });

        Button discardButton=(Button)findViewById(R.id.discard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //onSharedIntent();
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
                    TextView tv=(TextView)findViewById(R.id.uriData);
                    tv.setText(receivedText);
                }
            }

        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

            Log.e("", "onSharedIntent: nothing shared" );
        }
    }


}
