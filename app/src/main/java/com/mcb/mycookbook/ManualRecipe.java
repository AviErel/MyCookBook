package com.mcb.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;

public class ManualRecipe extends AppCompatActivity {

    EditText header;
    EditText description,tags;
    private AdView mAdView;
    List<String> ingredients,preparations;
    LinearLayout menuHeader,menuBuild;
    ImageButton addIngredients,addStage;
    Recipe recipeData;

    Button acceptButton,buildButton,discardButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Statics.userId.equals("")){
            finish();
        }

        if(!Statics.lang_prefer.equals("")){
            Statics.SetAppLanguage(Statics.lang_prefer, getResources());
        }

        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        if(b!=null){
            recipeData=(Recipe)b.getSerializable("recipe");
        }

        setContentView(R.layout.activity_manual_recipe);

        header=(EditText)findViewById(R.id.header);
        description=(EditText)findViewById(R.id.description);
        tags=(EditText)findViewById(R.id.tags);

        ingredients=new ArrayList<>();
        preparations=new ArrayList<>();

        menuHeader=findViewById(R.id.menuHeaderData);
        menuBuild=findViewById(R.id.menuBuild);

        buildButton=(Button)findViewById(R.id.build);
        acceptButton=(Button)findViewById(R.id.accept);
        acceptButton.setVisibility(View.GONE);
        discardButton=(Button)findViewById(R.id.discard);

        addIngredients=(ImageButton)findViewById(R.id.addIngredient);
        addStage=(ImageButton)findViewById(R.id.addStage);

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

        final Spinner measures = (Spinner) findViewById(R.id.measure);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this,
                R.array.manualMeasurment, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        measures.setAdapter(mAdapter);

        if(recipeData!=null){
            header.setText(recipeData.GetHeader());
            description.setText(recipeData.GetDescription());
            tags.setText(Statics.FlatArray(recipeData.GetTags()));

            ingredients.addAll(Arrays.asList(Statics.BuildArray(recipeData.GetIngredients())));
            preparations.addAll(Arrays.asList(Statics.BuildArray(recipeData.GetPreparetions())));

            int spinPos=Integer.parseInt(recipeData.GetCourse());
            courseSpin.setSelection(spinPos);
            spinPos=Integer.parseInt(recipeData.GetDiet());
            DietSpin.setSelection(spinPos);
        }

        buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(menuHeader.getVisibility()==View.VISIBLE);
            }
        });

        if(recipeData!=null)
            acceptButton.setText(getText(R.string.saveUpdate));

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe;
                if(courseSpin.getSelectedItemPosition()<1 || DietSpin.getSelectedItemPosition()<1 ||
                        header.getText().toString().equals("")){
                    Context context = getApplicationContext();
                    CharSequence text = "Need to select course, diet and enter header";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    try{
                        String uuid=(recipeData==null? UUID.randomUUID().toString():recipeData.GetId());

                        recipe=new Recipe(uuid,header.getText().toString(),
                                String.valueOf(courseSpin.getSelectedItemPosition()),String.valueOf(DietSpin.getSelectedItemPosition())
                                ,"",description.getText().toString(),
                                Statics.userId,Statics.BuildArray(tags.getText().toString()),
                                Statics.FlatArray(ingredients.toArray(new String[0])),
                                Statics.FlatArray(preparations.toArray(new String[0])),false);
                        if(recipeData==null){
                            FireBaseModel.SaveRecipe(recipe);
                            endSession(true);
                        }
                        else
                        {
                            FireBaseModel.UpdateRecipe(recipe);
                            endSession(false);
                        }
                    }catch (Exception e){}
                }
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient=((EditText)findViewById(R.id.ingredient)).getText().toString()+" ";
                ingredient+=((EditText)findViewById(R.id.quantity)).getText().toString()+" ";
                ingredient+=measures.getSelectedItem().toString();
                ingredients.add(ingredient);
                ((EditText)findViewById(R.id.ingredient)).setText("");
                ((EditText)findViewById(R.id.quantity)).setText("");
                measures.setSelection(-1);
            }
        });

        addStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preparations.add(((EditText)findViewById(R.id.preparation)).getText().toString());
            }
        });
    }

    private void endSession(boolean update){
        if(update){
            Intent list=new Intent(getApplicationContext(),DisplayData.class);
            startActivity(list);
        }

        finish();
    }

    private void changeView(boolean state){
        if(state)
        {
            menuHeader.setVisibility(View.GONE);
            menuBuild.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.VISIBLE);
            buildButton.setText(getResources().getString(R.string.buildButtonBack));
        }
        else
        {
            menuHeader.setVisibility(View.VISIBLE);
            menuBuild.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
            buildButton.setText(getResources().getString(R.string.saveBuild));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadGoogleAdd();
    }

    private String StringUtils(String[] data){
        String answer="";
        for(String n : data){
            answer+=n+",";
        }
        return answer.substring(0,answer.length()-1);
    }

    private void loadGoogleAdd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
