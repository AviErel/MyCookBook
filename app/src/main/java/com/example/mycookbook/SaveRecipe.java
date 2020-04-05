package com.example.mycookbook;

import Model.FireBaseModel;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        final Spinner courseSpin = (Spinner) findViewById(R.id.Courses);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.saveCourse, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        courseSpin.setAdapter(cAdapter);



        final Spinner DietSpin = (Spinner) findViewById(R.id.Diets);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> dAdapter = ArrayAdapter.createFromResource(this,
                R.array.saveDiet, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        DietSpin.setAdapter(dAdapter);




        Button acceptButton=(Button)findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> recipe=new HashMap<>();
                if(courseSpin.getSelectedItem().equals("") || DietSpin.getSelectedItem().equals("") ||
                header.getText().toString().equals("")){
                    Context context = getApplicationContext();
                    CharSequence text = "Need to select course, diet and enter header";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    try{
//                        recipe.put("id", UUID.randomUUID());
                        recipe.put("name",header.getText().toString());
                        recipe.put("Course",courseSpin.getSelectedItem().toString());
                        recipe.put("Diet",DietSpin.getSelectedItem().toString());
                        recipe.put("uri",uriText.getText().toString());
                        recipe.put("description",description.getText().toString());
                        //send to Manor
                        FireBaseModel.SaveRecipe(recipe);
                    }catch (Exception e){}
                }

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
