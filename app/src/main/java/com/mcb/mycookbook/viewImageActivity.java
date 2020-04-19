package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class viewImageActivity extends Base {

    ProgressBar spinner;
    Recipe recipeData;
    ListView lst;
    int count=0, size = 0;
    List<Bitmap> images;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_activity);

        lst = findViewById(R.id.images_list);
        images = new ArrayList<>();

        loadGoogleAdd();

        Bundle b = getIntent().getExtras();
        recipeData=(Recipe)b.getSerializable("recipe");
        if(recipeData != null){
            handleImage();
            spinner = findViewById(R.id.viewProgressBar);
            spinner.setVisibility(View.VISIBLE);
        }
    }

    private void loadGoogleAdd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());
        mAdView = findViewById(R.id.view_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void handleImage() {
        if(recipeData.GetImagesNames().size()>0){
            count = 0;
            size = recipeData.GetImagesNames().size();
            for(String imgName:recipeData.GetImagesNames()) {
                FireBaseModel.GetRecipeImage(imgName, new Statics.GetImageListener() {
                    @Override
                    public void complete(byte[] image) {
                        count++;
                        Bitmap b = convertBytesToBitmap(image);
                        if (b != null) {
//                        ((ImageView)findViewById(R.id.recipe_image)).setImageBitmap(b);
                            images.add(b);

                        }
                        if(count == size){
                            lst.setAdapter(new ImagesAdapter(viewImageActivity.this, images));
                            ((TextView) findViewById(R.id.recipe_name)).setText(recipeData.GetHeader());
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
    }

    private Bitmap convertBytesToBitmap(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }
}


class ImagesAdapter extends BaseAdapter{
    private List<Bitmap>images;
    LayoutInflater inf;

    ImagesAdapter(Context con, List<Bitmap>data){
        images=new LinkedList<>();
        if(data!=null){
            images=data;
        }
        inf=LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inf.inflate(R.layout.img_row,null);
        ImageView imageView=convertView.findViewById(R.id.rec_img);
        imageView.setImageBitmap(images.get(position));
        return convertView;
    }
}
