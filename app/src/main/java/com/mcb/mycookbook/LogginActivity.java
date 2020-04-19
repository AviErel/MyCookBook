package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Statics;
import Model.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LogginActivity extends Base implements View.OnClickListener, Statics.GetUserListener {

    private static final int RC_SIGN_IN = 497;
    private static final String TAG = "SignInGoogle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        Statics.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!= null){
            FireBaseModel.GetUserLanguagePre(account.getId(), this);
            handleUi(account);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn(){
        Intent signInIntent = Statics.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            Statics.isFirstEnter = true;
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FireBaseModel.GetUserLanguagePre(account.getId(), this);
//            saveUserToFB(account.getId());
            saveUserTools("name", account.getGivenName());
            handleUi(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.println(1,TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void saveUserToFB(String accountId){
        FireBaseModel.SaveUser(new User(accountId, getUserLang()));
    }

    private void saveUserTools(String key, String value){
        SharedPreferences sharedPref = getSharedPreferences("user_tools",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getUserLang(){
        SharedPreferences sharedPref = getSharedPreferences("user_tools",Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.user_lang),"");
    }

    private void handleUi(GoogleSignInAccount account){
        if(account!=null){
            Statics.userId = account.getId();
            Statics.photoUrl = account.getPhotoUrl();
            Statics.userName = account.getGivenName();
            finish();
        }
    }

    @Override
    public void onComplete(User user) {
        if(!user.GetLanguagePreference().equals("")){
            Statics.SetAppLanguage(user.GetLanguagePreference(), getResources());
            String lang = getUserLang();

            if(!lang.equals("") || !lang.equals(user.GetLanguagePreference()))
                saveUserTools(getString(R.string.user_lang), user.GetLanguagePreference());
        }

        setRefreshSetting(false);
    }

    private void setRefreshSetting(Boolean isRefresh){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tools_file_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.is_refresh), isRefresh);
        editor.commit();
    }

    @Override
    public void onCancled(String error, String accountId) {
        saveUserToFB(accountId);
    }
}
