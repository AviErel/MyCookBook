package com.example.mycookbook;

import Model.FireBaseModel;
import Model.Statics;
import Model.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

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
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            saveUserToFB(account.getId(), "");
            handleUi(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.println(1,TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void saveUserToFB(String accountId, String langpre){
        FireBaseModel.SaveUser(new User(accountId, ""));
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
        }
    }

    @Override
    public void onCancled(String error, String accountId) {
        String langPre = Statics.lang_prefer.equals("")?"":Statics.lang_prefer;
        saveUserToFB(accountId, langPre);
    }
}
