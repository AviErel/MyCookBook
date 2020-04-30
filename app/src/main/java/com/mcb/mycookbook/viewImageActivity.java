package com.mcb.mycookbook;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    int count=0, size = 0,position;
    List<Bitmap> images;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_activity);
        Bundle b = getIntent().getExtras();
        recipeData = (Recipe) b.getSerializable("recipe");

        for (int index = 0; index < Statics.showList.size(); index++) {
            if (Statics.showList.get(index).GetId().equals(recipeData.GetId())) {
                position = index;
                break;
            }
        }

        BuildLayout();
    }

    private void BuildLayout(){
        lst = findViewById(R.id.images_list);
        images = new ArrayList<>();

        loadGoogleAdd();

        LinearLayout imageLayout = findViewById(R.id.image_layout);
        LinearLayout webLayout = findViewById(R.id.webLayout);
        LinearLayout viewRecipeLayout = findViewById(R.id.manual_recipe_layout);

        if(recipeData != null){
            if(recipeData.GetUri() != null && !recipeData.GetUri().equals("")){
                imageLayout.setVisibility(View.GONE);
                viewRecipeLayout.setVisibility(View.GONE);
                webLayout.setVisibility(View.VISIBLE);
                WebView webView = findViewById(R.id.web_recipe_view);
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setDomStorageEnabled(true);
                webView.loadUrl(recipeData.GetUri());
            }
            else if(recipeData.GetImagesNames().size() > 0) {
                imageLayout.setVisibility(View.VISIBLE);
                viewRecipeLayout.setVisibility(View.GONE);
                webLayout.setVisibility(View.GONE);
                spinner = findViewById(R.id.viewProgressBar);
                spinner.setVisibility(View.VISIBLE);
                handleImage();
            }
            else{
                imageLayout.setVisibility(View.GONE);
                viewRecipeLayout.setVisibility(View.VISIBLE);
                webLayout.setVisibility(View.GONE);
                handleView();
            }
        }
    }

    public void moveFwd(View v){
        position++;
        position%=Statics.showList.size();
        recipeData=Statics.showList.get(position);
        BuildLayout();
    }

    public void moveBack(View v){
        position--;
        position=position<0? Statics.showList.size()-1: position;
        recipeData=Statics.showList.get(position);
        BuildLayout();
    }

    private void handleView() {
        List<String> ingredients= Arrays.asList(Statics.BuildArray(recipeData.GetIngredients()));
        List<String> stages=Arrays.asList(Statics.BuildArray(recipeData.GetPreparetions()));

        ((TextView)findViewById(R.id.viewHeader)).setText(recipeData.GetHeader());

        RowsAdapter adapter;
        ListView ings=(ListView)findViewById(R.id.ingredientsView);
        adapter=new RowsAdapter(viewImageActivity.this,ingredients);
        ings.setAdapter(adapter);

        ListView stgs=(ListView)findViewById(R.id.stagesView);
        adapter=new RowsAdapter(viewImageActivity.this,stages);
        stgs.setAdapter(adapter);
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
