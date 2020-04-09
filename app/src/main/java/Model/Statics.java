package Model;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONArray;

import java.util.List;

public class Statics {
    public static String userId="";
    public static Uri photoUrl;
    public static String userName = "";
    public static GoogleSignInClient mGoogleSignInClient;

    public interface GetDataListener{
        void onComplete(List<Recipe> data);
        void onCancled(String error);
    }
}
