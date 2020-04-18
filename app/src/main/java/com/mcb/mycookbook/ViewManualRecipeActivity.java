package com.mcb.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.List;

import Model.Recipe;
import Model.Statics;

public class ViewManualRecipeActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_manual_recipe);

        Bundle b=getIntent().getExtras();

        Recipe recipeData=(Recipe)b.getSerializable("recipe");

        List<String> ingredients= Arrays.asList(Statics.BuildArray(recipeData.GetIngredients()));
        List<String> stages=Arrays.asList(Statics.BuildArray(recipeData.GetPreparetions()));

        ((TextView)findViewById(R.id.header)).setText(recipeData.GetHeader());

        RowsAdapter adapter;
        ListView ings=(ListView)findViewById(R.id.ingrediantsView);
        adapter=new RowsAdapter(ViewManualRecipeActivity.this,ingredients);
        ings.setAdapter(adapter);

        ListView stgs=(ListView)findViewById(R.id.stagesView);
        adapter=new RowsAdapter(ViewManualRecipeActivity.this,stages);
        stgs.setAdapter(adapter);
    }

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
