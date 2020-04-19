package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Statics;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
            case R.id.help:{
                DialogFragment dialogFragment = new ExplainationDialog();
                dialogFragment.show(getSupportFragmentManager(), "explain");
                return true;
            }
            case R.id.eng:
                setLocale("en");
                return true;
            case R.id.heb:
                setLocale("he");
                return true;
            case R.id.recipe_add:
                Intent addMe=new Intent(this,ManualRecipe.class);
                startActivity(addMe);
                return true;
            case R.id.menu_show:
                Intent showMe=new Intent(this,DisplayData.class);
                startActivity(showMe);
                return true;
            case R.id.menu_logoff:
                if(!Statics.userId.equals("")) {
                    logoff();
                    item.setTitle(R.string.login);
                    getSupportActionBar().setTitle("    " +getResources().getString(R.string.hello)+" "+ "" + "    ");
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
                Statics.userName = "";
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.user_name), "");
                editor.putString(getString(R.string.user_lang), "");
                editor.commit();
            }
        });
    }

    private void loadUserTools(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name),Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.user_name), "");
        String lang = sharedPref.getString(getString(R.string.user_lang), "");
        String userName = "    " +getResources().getString(R.string.hello)+" "+ name + "    ";
        Statics.userName = name;
        getSupportActionBar().setTitle(userName);

        if(!lang.equals("")){
            Statics.SetAppLanguage(lang, getResources());
        }
    }

    private void refreshView(){
        Intent refresh=getIntent();
        finish();
        startActivity(refresh);
    }

    private boolean isToolsLoaded(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name),Context.MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.is_refresh), false);
    }

    public void setLocale(String lang) {
        Statics.SetAppLanguage(lang, getResources());
        FireBaseModel.SetUserLanguagePref();

        SharedPreferences sharedPref = getSharedPreferences("user_tools", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.user_lang), lang);
        editor.commit();

        //refresh
        refreshView();
    }
}
