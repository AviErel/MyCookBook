package com.example.mycookbook;

import Model.Statics;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }
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
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        Intent refresh=getIntent();
        finish();
        startActivity(refresh);
        conf.setLayoutDirection(myLocale);

        res.updateConfiguration(conf, dm);

    }
}
