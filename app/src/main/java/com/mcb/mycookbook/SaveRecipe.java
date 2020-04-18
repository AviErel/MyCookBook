package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SaveRecipe extends Base {

    EditText header;
    EditText description,tags;
    TextView uriText;
    private AdView mAdView;
    String uriString;
    String imageName;
    Bitmap image;
    String uriImage;
    Recipe recipeToSave;
    ProgressBar spinner;


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
        uriString = b.getString("uri");
        final Recipe recipeData=(Recipe)b.getSerializable("recipe");
        uriImage = b.getString("imageUrl");

        if(uriImage!= null)
            handleImageRecipe();

        setContentView(R.layout.activity_save_recipe);
        header=(EditText)findViewById(R.id.header);
//        uriText=(TextView)findViewById(R.id.uriData);
        description=(EditText)findViewById(R.id.description);
        tags=(EditText)findViewById(R.id.tags);
        spinner = findViewById(R.id.saveProgressBar);
        spinner.setVisibility(View.GONE);

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
//            uriText.setText(recipeData.GetUri());
            uriString=recipeData.GetUri();
            header.setText(recipeData.GetHeader());
            description.setText(recipeData.GetDescription());
            tags.setText(Statics.FlatArray(recipeData.GetTags()));

            int spinPos=Integer.parseInt(recipeData.GetCourse());
            courseSpin.setSelection(spinPos);
            spinPos=Integer.parseInt(recipeData.GetDiet());
            DietSpin.setSelection(spinPos);
        }
/*        else
        {
            uriText.setText(uriString);
        }
*/

        Button acceptButton=(Button)findViewById(R.id.accept);
        if(recipeData!=null)
            acceptButton.setText(getText(R.string.saveUpdate));
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe;
                if(courseSpin.getSelectedItemPosition()<1 || DietSpin.getSelectedItemPosition()<1 ||
                header.getText().toString().equals("")){
                    showToast("Need to select course, diet and enter header");
                }else{
                    try{
                        String uuid=(recipeData==null?UUID.randomUUID().toString():recipeData.GetId());
                        List<String> imagesNames = new ArrayList<>();
                        spinner.setVisibility(View.VISIBLE);
                        findViewById(R.id.save_layout).setVisibility(View.GONE);
                        findViewById(R.id.buttonLayout).setVisibility(View.GONE);
                        if(image != null){
                            imageName = uuid + "image" + imagesNames.size();
                            imagesNames.add(imageName);
                        }

                        recipeToSave=new Recipe(uuid,header.getText().toString(),
                                String.valueOf(courseSpin.getSelectedItemPosition()),String.valueOf(DietSpin.getSelectedItemPosition())
                                ,uriString,description.getText().toString(),
                                Statics.userId,Statics.BuildArray(tags.getText().toString()),false, imagesNames);
                        if(recipeData==null){
                            if(image != null ) {
                                FireBaseModel.SaveImage(imageName, image, new Statics.SaveImageListener() {
                                    @Override
                                    public void complete(String url) {
                                        FireBaseModel.SaveRecipe(recipeToSave);
                                        showToast(getString(R.string.created_recipe));
                                        endSession(true);

                                    }

                                    @Override
                                    public void fail() {
                                        endSession(true);
                                        showToast(getString(R.string.error_created_recipe));

                                    }
                                });
                            }
                            else{
                                FireBaseModel.SaveRecipe(recipeToSave);
                                showToast(getString(R.string.created_recipe));
                                endSession(true);
                            }


                        }
                        else
                        {
                            FireBaseModel.UpdateRecipe(recipeToSave);
                            endSession(false);
                        }
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
    }

    private void showToast(String stringToShow){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, stringToShow, duration);
        toast.show();
    }

    private void handleImageRecipe(){
        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(Uri.parse(uriImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = BitmapFactory.decodeStream(input);
    }

    private void endSession(boolean update){
        if(update){
            Intent list=new Intent(getApplicationContext(),DisplayData.class);
            startActivity(list);
        }

        finish();
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

//    private void onSharedIntent() {
//        Intent receiverdIntent = getIntent();
//        String receivedAction = receiverdIntent.getAction();
//        String receivedType = receiverdIntent.getType();
//
//        if (receivedAction.equals(Intent.ACTION_SEND)) {
//
//            // check mime type
//            if (receivedType.startsWith("text/")) {
//
//                String receivedText = receiverdIntent
//                        .getStringExtra(Intent.EXTRA_TEXT);
//                if (receivedText != null) {
////                    TextView tv=(TextView)findViewById(R.id.uriData);
////                    tv.setText(receivedText);
//                    uriString=receivedText;
//                }
//            }

//        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
//
//            Log.e("", "onSharedIntent: nothing shared" );
//        }
//    }


}
