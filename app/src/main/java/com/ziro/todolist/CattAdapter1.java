package com.ziro.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class CattAdapter1 extends ArrayAdapter<Category> {
    public CattAdapter1(Context context, List<Category> Categorie) {
        super(context, 0, Categorie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.to_do_row_category_layout, parent, false);
        }
        CattAdapter1.CatHolder viewHolder = (CatHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new CattAdapter1.CatHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.title);
            viewHolder.color = (TextView) convertView.findViewById(R.id.color);
            convertView.setTag(viewHolder);
        }
        Category cat = getItem(position);
        if (cat != null) {
            viewHolder.name.setText(cat.getName());
            viewHolder.color.setBackgroundColor(cat.getColor());
        }
        return convertView;
    }

    private class CatHolder {
        public TextView name;
        public TextView color;
    }

}
