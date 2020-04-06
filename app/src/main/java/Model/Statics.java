package Model;

import org.json.JSONArray;

import java.util.List;

public class Statics {
    public static String userId;

    public interface GetDataListener{
        void onComplete(List<Recipe> data);
        void onCancled(String error);
    }
}
