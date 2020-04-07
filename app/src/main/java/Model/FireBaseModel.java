package Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static void GetAllRecupesByUserId(final String userId, final Statics.GetDataListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new LinkedList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Map<String, Object> recipeMap = (Map<String, Object>) snapshot.getValue();

                    if((recipeMap.get("userId").toString()).equals(userId)) {
                        recipes.add(MapToRecipe(recipeMap));
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

    public static void DeleteRecipe(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("recipe").child(id).removeValue();
    }

    public static void UpdateRecipe(Recipe recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeToUpdate = database.getReference("recipe").child(recipe.GetId());

        if(recipeToUpdate != null){
            recipeToUpdate.child("header").setValue(recipe.GetHeader());
            recipeToUpdate.child("course").setValue(recipe.GetCourse());
            recipeToUpdate.child("diet").setValue(recipe.GetDiet());
            recipeToUpdate.child("uri").setValue(recipe.GetUri());
            recipeToUpdate.child("description").setValue(recipe.GetDescription());
            recipeToUpdate.child("userId").setValue(recipe.GetUserId());
            recipeToUpdate.child("publicated").setValue(recipe.GetPublicated());
        }
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

    private static Recipe MapToRecipe(Map<String, Object> recipeMap){
        Recipe recipe = new Recipe(recipeMap.get("id").toString(), recipeMap.get("header").toString(),
                recipeMap.get("course").toString(), recipeMap.get("diet").toString(),
                recipeMap.get("uri").toString(), recipeMap.get("description").toString(),
                recipeMap.get("userId").toString(), (recipeMap.get("publicated").toString().equals("true")));
        return recipe;
    }
}
