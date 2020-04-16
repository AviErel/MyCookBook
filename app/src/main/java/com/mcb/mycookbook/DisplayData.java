package com.mcb.mycookbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;
import androidx.appcompat.app.AlertDialog;

public class DisplayData extends Base implements Statics.GetDataListener {

    ListView lst;
    List<Recipe> recipesList;
    List<Recipe> showList;
    Spinner cSpin,
            dSpin;
    ImageButton searchButton,filterZone,searchZone;
    boolean flag;
    private ProgressBar spinner;
    private AdView mAdView;
    private View delView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Statics.lang_prefer.equals("")){
            Statics.SetAppLanguage(Statics.lang_prefer, getResources());
        }
        setContentView(R.layout.activity_display_data);

        loadGoogleAdd();

        flag=false;
        recipesList=new LinkedList<>();
        showList=new LinkedList<>();

        cSpin=findViewById(R.id.course);
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.saveCourse, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        cSpin.setAdapter(cAdapter);

        cSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                filterAway(dSpin.getSelectedItemPosition(),cSpin.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dSpin=findViewById(R.id.diet);
        ArrayAdapter<CharSequence> dAdapter = ArrayAdapter.createFromResource(this,
                R.array.saveDiet, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        dSpin.setAdapter(dAdapter);

        dSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                filterAway(dSpin.getSelectedItemPosition(),cSpin.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterZone=findViewById(R.id.filterButton);
        filterZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout fz=findViewById(R.id.filterZone);
                if(fz.getVisibility()==View.VISIBLE)
                {
                    fz.setVisibility(View.GONE);
                }else{
                    fz.setVisibility(View.VISIBLE);
                }
            }
        });

        searchZone=findViewById(R.id.searchOptions);
        searchZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout sz=findViewById(R.id.searchZone);
                if(sz.getVisibility()==View.VISIBLE){
                    sz.setVisibility(View.GONE);
                }else{
                    sz.setVisibility(View.VISIBLE);
                }
            }
        });
        searchButton=findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText serchPattern=(EditText)(findViewById(R.id.searchData));
                String[] array=Statics.BuildArray(serchPattern.getText().toString());
                CheckBox head=findViewById(R.id.headerOption);
                CheckBox tags=findViewById(R.id.tagsOption);
                searchAway(array,head.isChecked(),tags.isChecked());
            }
        });

        lst=findViewById(R.id.recipesView);

        loadData();
    }

    @Override
    protected void onStart() {

        super.onStart();
        loadData();
    }

    private void loadData(){
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        FireBaseModel.GetAllRecupesByUserId(Statics.userId,this);

        UpdateView();
    }

    private void loadGoogleAdd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onComplete(List<Recipe> data) {
        recipesList.clear();
        showList.clear();
        recipesList.addAll(data);
        showList.addAll(data);
        flag=true;
        UpdateView();
    }

    @Override
    public void onCancled(String error) {

    }

    private void UpdateView(){
        if(flag){
            spinner.setVisibility(View.GONE);
            lst.setAdapter(new ReportAdapter(DisplayData.this,showList));
        }
    }

    private void filterAway(int diet,int course){
        List<Recipe> temp=new LinkedList<>();
        temp.addAll(recipesList);
        showList.clear();
        if(course>0)
        {
            temp.clear();
            for(Recipe recipe:recipesList)
            {
                if(recipe.GetCourse().equals(String.valueOf(course))){
                    temp.add(recipe);
                }
            }
        }
        showList.addAll(temp);
        if(diet>0)
        {
            showList.clear();
            for(Recipe recipe:temp)
            {
                if(recipe.GetDiet().equals(String.valueOf(diet))){
                    showList.add(recipe);
                }
            }
        }
       UpdateView();
    }

    private void searchAway(String[] array,boolean head,boolean tag){
        List<Recipe>temp=new LinkedList<>();
        filterAway(dSpin.getSelectedItemPosition(),cSpin.getSelectedItemPosition());
        temp.addAll(showList);
        showList.clear();
        if(array.length==0){
            showList.addAll(temp);
        }
        else{
            if(head){
                for(Recipe r : temp) {
                    for (String d : array) {
                        if (r.GetHeader().contains(d)) {
                            showList.add(r);
                            r.SetTags(new String[0]);
                            break;
                        }
                    }
                }
            }
            if(tag){
                for(Recipe r:temp){
                    for(String d:array){
                        for(String t:r.GetTags()){
                            if(d.equals(t)){
                                showList.add(r);
                                break;
                            }
                        }
                    }
                }
            }
        }
        UpdateView();
    }

    public void showRow(View v){
        int position=Integer.parseInt(v.getTag().toString());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(showList.get(position).GetUri()));
        startActivity(browserIntent);
    }

    public void delRow(View v){
        this.delView = v;
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_alert_title)
                .setMessage(R.string.delete_alert_message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecipe();
                        // Continue with delete operation
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setPositiveButtonIcon(getResources().getDrawable(R.drawable.yes_icon))
                .setNegativeButton("", null)
                .setNegativeButtonIcon(getResources().getDrawable(R.drawable.no_icon))
                .setIcon(R.drawable.recipes)
                .show();
    }

    private void deleteRecipe(){
        int position=Integer.parseInt(delView.getTag().toString());
        FireBaseModel.DeleteRecipe(showList.get(position).GetId());
        FireBaseModel.GetAllRecupesByUserId(Statics.userId,this);
    }

    public void updateRow(View v){
        Recipe data=(Recipe)(v.getTag());
        Intent updateMe = new Intent(this,SaveRecipe.class);
        updateMe.putExtra("recipe", data);
        startActivity(updateMe);
        UpdateView();
    }
}

class ReportAdapter extends BaseAdapter{

    private List<Recipe> recipes;
    LayoutInflater inf;
    ImageButton del,update,view;
    ImageView del1, update1, view1;

    ReportAdapter(Context con, List<Recipe>data){
        recipes=new LinkedList<>();
        if(data!=null){
            recipes=data;
        }
        inf=LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inf.inflate(R.layout.recipesrow,null);
        TextView header=convertView.findViewById(R.id.rowHeader);
        TextView description=convertView.findViewById(R.id.rowDescription);

        del=convertView.findViewById(R.id.deleteRow);
        del.setTag(position);

        update=convertView.findViewById(R.id.updateRow);
        update.setTag(recipes.get(position));

        view=convertView.findViewById(R.id.viewRow);
        view.setTag(position);

        header.setText(recipes.get(position).GetHeader());
        description.setText(recipes.get(position).GetDescription());

        return convertView;
    }
}