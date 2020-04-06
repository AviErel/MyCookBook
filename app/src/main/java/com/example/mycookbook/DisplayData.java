package com.example.mycookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;

public class DisplayData extends AppCompatActivity implements Statics.GetDataListener {

    ListView lst;
    List<Recipe> recipesList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        lst=findViewById(R.id.recipesView);
        FireBaseModel.GetAllRecupesByUserId("",this);


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipesList.get(position).GetUri()));
                startActivity(browserIntent);
            }
        });

    }

    @Override
    public void onComplete(List<Recipe> data) {
        recipesList=data;
        lst.setAdapter(new ReportAdapter(DisplayData.this,recipesList));
    }

    @Override
    public void onCancled(String error) {

    }
}

class ReportAdapter extends BaseAdapter{

    private List<Recipe> recipes;
    LayoutInflater inf;

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
        header.setText(recipes.get(position).GetHeader());
        description.setText(recipes.get(position).GetDescription());
        return convertView;
    }
}
