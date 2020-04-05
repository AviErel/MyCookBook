package Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.UUID;

public class FireBaseModel {

    public static void SaveRecipe(Map<String,Object> recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child(UUID.randomUUID().toString());
        myRef.setValue(recipe);
    }

}
