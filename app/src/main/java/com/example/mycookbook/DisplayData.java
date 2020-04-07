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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import Model.FireBaseModel;
import Model.Recipe;
import Model.Statics;

public class DisplayData extends AppCompatActivity implements Statics.GetDataListener {

    ListView lst;
    List<Recipe> recipesList;
    List<Recipe> showList;
    Spinner cSpin,
            dSpin;
    ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

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
                filterAway(dSpin.getSelectedItem().toString(),cSpin.getSelectedItem().toString());
/*                showList.clear();
                if(position>0){
                    for(Recipe recipe:recipesList)
                    {
                        if(recipe.GetCourse().equals(cSpin.getItemAtPosition(position).toString())){
                            showList.add(recipe);
                        }
                    }
                }
                else
                {
                    showList.addAll(recipesList);
                }
                UpdateView();
*/
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
                filterAway(dSpin.getSelectedItem().toString(),cSpin.getSelectedItem().toString());
/*                showList.clear();
                if (position > 0) {
                    for(Recipe recipe:recipesList)
                    {
                        if(recipe.GetDiet().equals(dSpin.getItemAtPosition(position).toString())){
                            showList.add(recipe);
                        }
                    }
                }
                else
                {
                    showList.addAll(recipesList);
                }
                UpdateView();
*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton=findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lst=findViewById(R.id.recipesView);
        FireBaseModel.GetAllRecupesByUserId("",this);


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(showList.get(position).GetUri()));
                startActivity(browserIntent);
            }
        });

        UpdateView();

    }

    @Override
    public void onComplete(List<Recipe> data) {
        recipesList.addAll(data);
        showList.addAll(data);
        UpdateView();
    }

    @Override
    public void onCancled(String error) {

    }

    private void UpdateView(){
        lst.setAdapter(new ReportAdapter(DisplayData.this,showList));
    }

    private void filterAway(String diet,String course){
        List<Recipe> temp=new LinkedList<>();
        temp.addAll(recipesList);
        showList.clear();
        if(!course.equals(""))
        {
            temp.clear();
            for(Recipe recipe:recipesList)
            {
                if(recipe.GetCourse().equals(course)){
                    temp.add(recipe);
                }
            }
        }
        showList.addAll(temp);
        if(!diet.equals(""))
        {
            showList.clear();
            for(Recipe recipe:temp)
            {
                if(recipe.GetDiet().equals(diet)){
                    showList.add(recipe);
                }
            }
        }
       UpdateView();
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
