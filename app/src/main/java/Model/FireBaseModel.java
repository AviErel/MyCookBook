package Model;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class FireBaseModel {

    public static void SaveRecipe(Recipe recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child(recipe.GetId().toString());
        myRef.setValue(recipeToMap(recipe)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public static void SaveUser(User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(user.GetId());
        myRef.setValue(UserToMap(user));
    }

    public static void SaveImage(final String imageName, Bitmap image, final Statics.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.complete(imageName);
            }
        });
    }

    public static void GetRecipeImage(String imageName,final Statics.GetImageListener listener ){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("images/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(5*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                listener.complete(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.fail();
            }
        });
    }

    public static void DeleteRecipeImage(String imageName, final Statics.RemoveListener listener){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("images/" + imageName);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                listener.complete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                listener.complete(false);
            }
        });
    }

    public static void GetUserLanguagePre(final String userId, final Statics.GetUserListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userMap = (HashMap<String, Object>)dataSnapshot.getValue();
                if(userMap != null) {
                    User user = MapToUser(userMap);
                    listener.onComplete(user);
                }
                else{
                    listener.onCancled("This user not sign in our db", userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onCancled(databaseError.getMessage(), userId);
            }
        });
    }

    public static void SetUserLanguagePref(){
        User user = new User(Statics.userId, Statics.lang_prefer);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(user.GetId());
        myRef.setValue(UserToMap(user));
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

                    if((recipeMap.get("userId")!= null)&&(recipeMap.get("userId").toString()).equals(userId)) {
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

    public static void DeleteRecipe(String id, final Statics.RemoveListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("recipe").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.complete(true);
            }
        });
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
            recipeToUpdate.child("tags").setValue(Statics.FlatArray(recipe.GetTags()));
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
        recipeMap.put("tags",Statics.FlatArray(recipe.GetTags()));
        recipeMap.put("ingredients",recipe.GetIngredients());
        recipeMap.put("preparations",recipe.GetPreparetions());
        recipeMap.put("publicated", recipe.GetPublicated());
        recipeMap.put("imagesNames",recipe.GetImagesNames());

        return recipeMap;
    }

    private static Recipe MapToRecipe(Map<String, Object> recipeMap){
        Recipe recipe = new Recipe(recipeMap.get("id").toString(), recipeMap.get("header").toString(),
                recipeMap.get("course").toString(), recipeMap.get("diet").toString(),
                recipeMap.get("uri")!= null? recipeMap.get("uri").toString(): "", recipeMap.get("description").toString(),
                recipeMap.get("userId")!= null? recipeMap.get("userId").toString():"",
                recipeMap.get("tags")!=null? Statics.BuildArray(recipeMap.get("tags").toString()): Statics.BuildArray(""),
                (recipeMap.get("publicated").toString().equals("true")),
                 recipeMap.get("imagesNames")!= null?(List<String>) recipeMap.get("imagesNames") : new ArrayList<String>());

//        recipeMap.get("imagesNames") != null?Statics.BuildList(recipeMap.get("imagesNames").toString()): Statics.BuildList("")
                recipeMap.get("ingredients")!=null? recipeMap.get("ingredients").toString() : "",
                recipeMap.get("preparations")!=null? recipeMap.get("preparations").toString() : "",
                (recipeMap.get("publicated").toString().equals("true")));
        return recipe;
    }

    private static Map<String, Object> UserToMap(User user){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.GetId());
        userMap.put("languagePreference", user.GetLanguagePreference());

        return userMap;
    }

    private static User MapToUser(Map<String, Object> userMap){
        User user = new User(userMap.get("id").toString(),
                userMap.get("languagePreference")!= null?userMap.get("languagePreference").toString():"");
        return user;
    }
}
