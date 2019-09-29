package com.ziro.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class ItemsAdapter1 extends ArrayAdapter<Items1> {

    public ItemsAdapter1(Context context, List<Items1> Items) {
        super(context, 0, Items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.to_do_row_item1, parent, false);
        }
        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ItemViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.dateHour = (TextView) convertView.findViewById(R.id.dateHour);
            viewHolder.dateMonth = (TextView) convertView.findViewById(R.id.dateMonth);
            viewHolder.dateYear = (TextView) convertView.findViewById(R.id.dateYear);
            viewHolder.categorie = (TextView) convertView.findViewById(R.id.categorie);
            viewHolder.back = (LinearLayout) convertView.findViewById(R.id.back);
            viewHolder.done = (ImageView) convertView.findViewById(R.id.done);

            convertView.setTag(viewHolder);
        }
        Items1 Item = getItem(position);
        final ArrayList<Category> cat = MainToDoActivity.getCat();
        viewHolder.back.setBackgroundColor(Color.WHITE);
        boolean found = false;
        int i = 0;
        while (i < cat.size()) {
            if (Item.getCategorie().equals(cat.get(i).getName())) {
                found = true;
                int color = cat.get(i).getColor();
                String lighter = "#15" + Integer.toHexString(color).substring(2);
                viewHolder.categorie.setBackgroundColor(cat.get(i).getColor());
                if (Item.getStatus() == Items1.Status.DONE)
                    viewHolder.back.setBackgroundColor(Color.parseColor(lighter));
            }
            i++;
        }
        if (!found)
        {
            Item.setCategorie("none");
            int color = cat.get(0).getColor();
            String lighter = "#15" + Integer.toHexString(color).substring(2);
            if (Item.getStatus() == Items1.Status.DONE)
                viewHolder.back.setBackgroundColor(Color.parseColor(lighter));
            viewHolder.categorie.setBackgroundColor(cat.get(0).getColor());
        }
        viewHolder.title.setText(Item.getTitle());
        viewHolder.dateHour.setTextColor(Color.parseColor(Item.getDateColor()));
        if(Item.getDateColor().equals("#121212")){
           // Toast.makeText(getContext(),Items1.getTitle()+" : Done",Toast.LENGTH_SHORT).show();
            viewHolder.done.setVisibility(View.INVISIBLE);
        }else {
           // Toast.makeText(getContext(),Items1.getTitle()+" : !Done",Toast.LENGTH_SHORT).show();
            viewHolder.done.setVisibility(View.VISIBLE);
        }
        viewHolder.dateMonth.setTextColor(Color.parseColor(Item.getDateColor()));

        viewHolder.dateYear.setTextColor(Color.parseColor(Item.getDateColor()));
        viewHolder.text.setText(Item.getText());
        viewHolder.dateMonth.setText(Item.getMonth());
        viewHolder.dateYear.setText(Item.getYear());
        viewHolder.dateHour.setText(Item.getTime());

        return convertView;
    }

    private class ItemViewHolder {
        public TextView title;
        public TextView text;
        public TextView dateHour;
        public TextView dateYear;
        public TextView dateMonth;
        public TextView categorie;
        public LinearLayout back;
       public ImageView done;
    }


}

