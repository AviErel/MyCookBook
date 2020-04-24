package com.mcb.mycookbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class RowsIngredientsEditAdapter extends BaseAdapter {
    private List<String> ingredients;
    LayoutInflater inf;

    RowsIngredientsEditAdapter(Context con, List<String>data){
        ingredients=new LinkedList<>();
        if(data!=null){
            ingredients=data;
        }
        inf=LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inf.inflate(R.layout.row_ingredient_edit,null);
        EditText data=convertView.findViewById(R.id.data);
        data.setText(ingredients.get(position));
        data.setTag(position);

        ImageButton del=convertView.findViewById(R.id.deleteRow);
        del.setTag(position);

        return convertView;
    }
}
