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
import android.widget.ImageView;

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
import java.util.Arrays;

import Model.Statics;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Base {

    Intent showMe;
    private AdView mAdView;

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
    }

    @Override
    protected void onStart() {
        loadUserTools();
        super.onStart();
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
                    Intent saveActivity = new Intent(this, SaveRecipe.class);
                    saveActivity.putExtra("uri", receivedText);
                    startActivity(saveActivity);
                }
            } else if (receivedType.startsWith("image/")) {

                Uri imageUri = (Uri) receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                        Intent saveActivity = new Intent(this, SaveRecipe.class);
                        saveActivity.putExtra("imageUrl", imageUri.toString());
                        startActivity(saveActivity);
                }
            }
        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

            Log.e("", "onSharedIntent: nothing shared");
        }
    }
}
