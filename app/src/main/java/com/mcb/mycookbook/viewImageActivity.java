package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class viewImageActivity extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_activity);

        Bundle b = getIntent().getExtras();
        final Recipe recipeData=(Recipe)b.getSerializable("recipe");
        if(recipeData != null){
            ((TextView)findViewById(R.id.recipe_name)).setText(recipeData.GetHeader());
            handleImage(recipeData.GetImagesNames());
        }
    }

    private void handleImage(List<String> getImagesNames) {
        if(getImagesNames.size()>0){
            FireBaseModel.GetRecipeImage(getImagesNames.get(0), new Statics.GetImageListener() {
                @Override
                public void complete(byte[] image) {
                    Bitmap b = convertBytesToBitmap(image);
                    if(b != null){
                        ((ImageView)findViewById(R.id.recipe_image)).setImageBitmap(b);
                    }
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    private Bitmap convertBytesToBitmap(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }
}
