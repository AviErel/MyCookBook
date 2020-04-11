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

    public static String FlatArray(String[] data){
        String answer="";
        for(String n : data){
            answer+=n+",";
        }
        return answer.length()>0? answer.substring(0,answer.length()-1):answer;
    }

    public static String[] BuildArray(String data){
        if(data!=null && data.length()>0)
        {
            return data.split(",");
        }
        else
        {
            return new String[0];
        }
    }

}
