package Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class FireBaseModel {

    public static void SaveRecipe(Recipe recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child(recipe.GetId().toString());
        myRef.setValue(recipeToMap(recipe));
    }

    public static void GetAllRecupesByUserId(String userId, final Statics.GetDataListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new LinkedList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    try {
                        Recipe newRecipe = snapshot.getValue(Recipe.class);
                        recipes.add(newRecipe);
                    }
                    catch (Exception e){
                        Log.println(1, "error",e.getMessage());
                    }
                }

                listener.onComplete(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onCancled(databaseError.getMessage());
            }
        });
    }

    private static Map<String, Object> recipeToMap(Recipe recipe){
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("id", recipe.GetId().toString());
        recipeMap.put("header", recipe.GetHeader());
        recipeMap.put("course", recipe.GetCourse());
        recipeMap.put("diet", recipe.GetDiet());
        recipeMap.put("uri", recipe.GetUri());
        recipeMap.put("description", recipe.GetDescription());
        recipeMap.put("userId", recipe.GetUserId());
        recipeMap.put("publicated", recipe.GetPublicated());

        return recipeMap;
    }
}
