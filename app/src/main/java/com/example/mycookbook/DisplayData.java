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

import java.util.List;

import Model.Recipe;

public class DisplayData extends AppCompatActivity {

    ListView lst;
    List<Recipe> recipesList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        lst=findViewById(R.id.recipesView);
        //get Data from Manor

        lst.setAdapter(new ReportAdapter(DisplayData.this,recipesList));

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipesList.get(position).uri));
                startActivity(browserIntent);
            }
        });

    }
}

class ReportAdapter extends BaseAdapter{

    private List<Recipe> recipes;
    LayoutInflater inf;

    ReportAdapter(Context con, List<Recipe>data){
        recipes=data;
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
        header.setText(recipes.get(position).header);
        description.setText(recipes.get(position).description);
        return convertView;
    }
}
