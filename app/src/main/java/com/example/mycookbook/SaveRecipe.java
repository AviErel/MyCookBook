package com.example.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;

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

import java.util.UUID;

public class SaveRecipe extends Base {

    EditText header;
    EditText description;
    TextView uriText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Statics.userId.equals("")){
            Intent logginIntet = new Intent(this, LogginActivity.class);
            startActivity(logginIntet);
        }
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String uriString = b.getString("uri");
        final Recipe recipeData=(Recipe)b.getSerializable("recipe");

        setContentView(R.layout.activity_save_recipe);
        header=(EditText)findViewById(R.id.header);
        uriText=(TextView)findViewById(R.id.uriData);
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

        if(recipeData!=null){
            uriText.setText(recipeData.GetUri());
            header.setText(recipeData.GetHeader());
            description.setText(recipeData.GetDescription());
            int spinPos=cAdapter.getPosition(recipeData.GetCourse());
            courseSpin.setSelection(spinPos);
            spinPos=dAdapter.getPosition(recipeData.GetDiet());
            DietSpin.setSelection(spinPos);
        }else
        {
            uriText.setText(uriString);
        }


        Button acceptButton=(Button)findViewById(R.id.accept);
        if(recipeData!=null)
            acceptButton.setText(getText(R.string.saveUpdate));
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe;
                if(courseSpin.getSelectedItem().equals("") || DietSpin.getSelectedItem().equals("") ||
                header.getText().toString().equals("")){
                    Context context = getApplicationContext();
                    CharSequence text = "Need to select course, diet and enter header";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    try{
                        String uuid=(recipeData==null?UUID.randomUUID().toString():recipeData.GetId());

                        recipe=new Recipe(uuid,header.getText().toString(),
                                courseSpin.getSelectedItem().toString(),DietSpin.getSelectedItem().toString()
                                ,uriText.getText().toString(),description.getText().toString(),
                                Statics.userId,false);
                        if(recipeData==null){
                            FireBaseModel.SaveRecipe(recipe);
                        }
                        else
                        {
                            FireBaseModel.UpdateRecipe(recipe);
                        }
                    }catch (Exception e){}
                }
                finish();
            }
        });

        Button discardButton=(Button)findViewById(R.id.discard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    TextView tv=(TextView)findViewById(R.id.uriData);
                    tv.setText(receivedText);
                }
            }

        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

            Log.e("", "onSharedIntent: nothing shared" );
        }
    }


}
