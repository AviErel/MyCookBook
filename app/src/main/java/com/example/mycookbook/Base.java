package com.example.mycookbook;

import Model.FireBaseModel;
import Model.Statics;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActionBar supportActionBar = this.getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(true);
        String userName = "    " +getResources().getString(R.string.hello)+" "+ Statics.userName + "    ";
        supportActionBar.setTitle(userName);

        supportActionBar.setLogo(R.drawable.app_icon_2);
        supportActionBar.setDisplayUseLogoEnabled(true);
        Drawable color;
        color = new ColorDrawable(getResources().getColor(R.color.blue2));
        supportActionBar.setBackgroundDrawable(color);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.eng:
                setLocale("en");
                return true;
            case R.id.heb:
                setLocale("he");
                return true;
            case R.id.menu_show:
                Intent showMe=new Intent(this,DisplayData.class);
                startActivity(showMe);
                return true;
            case R.id.menu_logoff:
                if(!Statics.userId.equals("")) {
                    logoff();
                    item.setTitle(R.string.login);
                }
                else {
                    item.setTitle(R.string.logOff);
                    startActivity(new Intent(this, LogginActivity.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoff(){
        Statics.mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Statics.userId="";
            }
        });
    }

    public void setLocale(String lang) {
        Statics.SetAppLanguage(lang, getResources());
        FireBaseModel.SetUserLanguagePref();

        //refresh
        Intent refresh=getIntent();
        finish();
        startActivity(refresh);
    }
}
