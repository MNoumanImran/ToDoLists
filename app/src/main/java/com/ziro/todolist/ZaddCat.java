package com.ziro.todolist;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;


public class ZaddCat extends AppCompatActivity {

    int fColor;
    ListView mListV;
    public static ArrayList<Category> todo = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_add_category);
        fColor = Color.parseColor("#000");
        mListV = (ListView) findViewById(R.id.listView);
        todo = MainToDoActivity.getCat();
        CattAdapter1 adapter = new CattAdapter1(ZaddCat.this, todo);
        ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(todo.size()) + ")");
        mListV.setAdapter(adapter);
    }

    /**
     * @param v
     * @param dialog
     */
    public void setColor(View v, final Dialog dialog) {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                if (color != 0)
                    fColor = color;
                else
                    fColor = Color.parseColor("#262D3B");
                //((View) findViewById(R.id.color)).setBackgroundColor(color);
                dialog.findViewById(R.id.color).setBackgroundColor(fColor);
            }

            @Override
            public void onCancel() {
                //here goes nothing
            }
        });
    }


    /**
     * @param v
     */
    public void delete(View v) {
        final int position = mListV.getPositionForView((View) v.getParent());
        if (!MainToDoActivity.cat.get(position).getName().equals("none")) {
            MainToDoActivity.cat.remove(position);
            todo.remove(position);
            CattAdapter1 a = (CattAdapter1) mListV.getAdapter();
            ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(todo.size()) + ")");
            a.notifyDataSetChanged();
        } else
            Toast.makeText(getApplicationContext(), "Error can't btn_delete \"none\" category", Toast.LENGTH_SHORT).show();
        SwipeLayout s = (SwipeLayout) mListV.getChildAt(position);
        s.close(true);
    }

    /**
     * @param view
     */
    public void dialog(View view) {

        // custom dialog
        //final View v = (View) this;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.to_do_add_cat_layout);
        dialog.setTitle("Title...");
        dialog.findViewById(R.id.color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColor(view, dialog);
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ok = true;
                int i =0;
                String name = ((TextView) dialog.findViewById(R.id.catName)).getText().toString();
                while (i < MainToDoActivity.getCat().size())
                {
                    if (name.equals(MainToDoActivity.getCat().get(i).getName()))
                        ok = false;
                    i++;
                }
                if (ok) {
                    MainToDoActivity.cat.add(new Category(name, fColor));
                    todo.add(new Category(name, fColor));
                    CattAdapter1 a = (CattAdapter1) mListV.getAdapter();
                    ((TextView) findViewById(R.id.nb_cat)).setText("Categories (" + String.valueOf(todo.size()) + ")");
                    a.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: Category " + name + " already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    /**
     * @param v
     */
    public void finish(View v)
    {
        finish();
    }
}
