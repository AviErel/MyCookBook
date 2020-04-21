package Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Statics {
    public static String userId="";
    public static Uri photoUrl;
    public static String userName = "";
    public static GoogleSignInClient mGoogleSignInClient;
    public static String lang_prefer = "";
    public static Boolean isFirstEnter = false;
    public static List<Recipe> showList;

    public interface GetDataListener{
        void onComplete(List<Recipe> data);
        void onCancled(String error);
    }

    public interface GetUserListener{
        void onComplete(User user);
        void onCancled(String error, String accountId);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener{
        void complete(byte[] image);
        void fail();
    }

    public interface RemoveListener {
        void complete(Boolean isSuccess);
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

    public static void SetAppLanguage(String lang, Resources res){
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(myLocale);
        res.updateConfiguration(conf, dm);
        lang_prefer = lang;
    }
}
