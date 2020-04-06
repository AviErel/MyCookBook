package Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;

public class FireBaseModel {

    public static void SaveRecipe(Map<String,Object> recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child(UUID.randomUUID().toString());
        myRef.setValue(recipe);
    }

    public static void GetAllRecupesByUserId(String userId, final Statics.GetDataListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new LinkedList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Recipe newRecipe = snapshot.getValue(Recipe.class);
                    recipes.add(newRecipe);
                }

                listener.onComplete(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onCancled(databaseError.getMessage());
            }
        });
    }
}
