package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class viewImageActivity extends Base {

    ProgressBar spinner;
    Recipe recipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_activity);

        Bundle b = getIntent().getExtras();
        recipeData=(Recipe)b.getSerializable("recipe");
        if(recipeData != null){
            handleImage();
            spinner = findViewById(R.id.viewProgressBar);
            spinner.setVisibility(View.VISIBLE);
        }
    }

    private void handleImage() {
        if(recipeData.GetImagesNames().size()>0){
            FireBaseModel.GetRecipeImage(recipeData.GetImagesNames().get(0), new Statics.GetImageListener() {
                @Override
                public void complete(byte[] image) {
                    Bitmap b = convertBytesToBitmap(image);
                    if(b != null){
                        ((ImageView)findViewById(R.id.recipe_image)).setImageBitmap(b);
                        ((TextView)findViewById(R.id.recipe_name)).setText(recipeData.GetHeader());
                        spinner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void fail() {
                    spinner.setVisibility(View.GONE);
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, getString(R.string.error_view_image), duration);
                    toast.show();
                    finish();

                }
            });
        }
    }

    private Bitmap convertBytesToBitmap(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }
}
