package com.mcb.mycookbook;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends Base implements Statics.GetDataListener {

    Intent showMe;
    private AdView mAdView;
    ListView lst;
//    List<Recipe> topFiveRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isToolsLoaded()) {
            setRefreshSetting(true);
            refreshView();
        }
        loadUserTools();
        Intent logginIntet = new Intent(this, LogginActivity.class);
        startActivity(logginIntet);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            onSharedIntent();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        loadGoogleAdd();
        lst = findViewById(R.id.top_5_list);
    }

    private void handleFavoriteRecipe() {
        FireBaseModel.GetRecipesByUserId(Statics.userId, this);
    }

    @Override
    protected void onStart() {
        loadUserTools();
        super.onStart();

        if(Statics.isFirstEnter) {
            DialogFragment dialogFragment = new ExplainationDialog();
            dialogFragment.show(getSupportFragmentManager(), "explain");
            Statics.isFirstEnter = false;
        }

        if(!Statics.userId.equals(""))
            handleFavoriteRecipe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setRefreshSetting(false);
    }

    private void setRefreshSetting(Boolean isRefresh) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.is_refresh), isRefresh);
        editor.commit();
    }


    private void loadUserTools() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name), Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.user_name), "");
        String lang = sharedPref.getString(getString(R.string.user_lang), "");
        String userName = "    " + getResources().getString(R.string.hello) + " " + name + "    ";
        Statics.userName = name;
        getSupportActionBar().setTitle(userName);

        if (!lang.equals("")) {
            Statics.SetAppLanguage(lang, getResources());
        }
    }

    private void refreshView() {
        Intent refresh = getIntent();
        finish();
        startActivity(refresh);
    }

    private boolean isToolsLoaded() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.is_refresh), false);
    }

    private void loadGoogleAdd() {
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

    private void onSharedIntent() throws FileNotFoundException {
        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();

        if (receivedAction.equals(Intent.ACTION_SEND)) {

            // check mime type
            if (receivedType.startsWith("text/")) {

                String receivedText = receiverdIntent
                        .getStringExtra(Intent.EXTRA_TEXT);
                if (receivedText != null) {
                    openSaveIntent("uri", receivedText);
                }
            }
            else if (receivedType.startsWith("image/")) {

                Uri imageUri = (Uri) receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    ArrayList<Uri> imageUris = new ArrayList<>();
                    imageUris.add(imageUri);
                    openSaveIntent("imagesUri", imageUris);
                }
            }

        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(receivedAction) && receivedType != null) {
            if (receivedType.startsWith("image/")) {
                ArrayList<Uri> imageUris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if (imageUris != null) {
                    // Update UI to reflect multiple images being shared
                    openSaveIntent("imagesUri", imageUris);
                }
            }
        }else if (receivedAction.equals(Intent.ACTION_MAIN)) {

            Log.e("", "onSharedIntent: nothing shared");
        }
    }

    private void openSaveIntent(String key, ArrayList<Uri> objToSend){
        Intent saveActivity = new Intent(this, SaveRecipe.class);
        saveActivity.putExtra(key, objToSend);
        startActivity(saveActivity);
    }

    private void openSaveIntent(String key, String objToSend){
        Intent saveActivity = new Intent(this, SaveRecipe.class);
        saveActivity.putExtra(key, objToSend);
        startActivity(saveActivity);
    }

    @Override
    public void onComplete(List<Recipe> data) {
        Statics.showList = new ArrayList<>();
        Collections.sort(data, Recipe.StuRollno);

        for(Recipe recipe: data){
            if(recipe.GetCounter() >0)
                Statics.showList.add(recipe);

            if(Statics.showList.size() == 5)
                break;
        }
        TextView textView = findViewById(R.id.not_fav_recipe);

        if(Statics.showList.size() == 0){
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.not_fav_recipe);
        }else {
            lst.setAdapter(new FavoriteRecipesAdapter(MainActivity.this, Statics.showList));
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCancled(String error) {

    }

    public void showRow(View v){
        int position=Integer.parseInt(v.getTag().toString());
        FireBaseModel.UpdateCount(Statics.showList.get(position).GetId(),Statics.showList.get(position).GetCounter());

        Intent viewImageIntent = new Intent(this, viewImageActivity.class);
        viewImageIntent.putExtra("recipe", Statics.showList.get(position));
        startActivity(viewImageIntent);
    }
}
class FavoriteRecipesAdapter extends BaseAdapter{

    private List<Recipe> bestRecipes;
    LayoutInflater inf;

    FavoriteRecipesAdapter(Context con, List<Recipe>data){
        bestRecipes=new LinkedList<>();
        if(data!=null){
            bestRecipes=data;
        }
        inf=LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return bestRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return bestRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inf.inflate(R.layout.fav_recipe_row,null);
        TextView header=convertView.findViewById(R.id.fav_recipe_title);
        TextView description=convertView.findViewById(R.id.fav_recipe_description);
        header.setText(bestRecipes.get(position).GetHeader());
        header.setTag(position);
        description.setText(bestRecipes.get(position).GetDescription());
        description.setTag(position);

        return convertView;
    }
}
