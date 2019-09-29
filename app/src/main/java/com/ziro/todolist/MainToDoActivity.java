package com.ziro.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainToDoActivity extends ActionBarActivity {

    int id;
    public static ListView mListView;
    public static List<Items1> items = new ArrayList<>();
    public static List<Items1> tmp = new ArrayList<>();
    public static ArrayList<Category> cat = new ArrayList<>();


    TextView nb_tasks;
    public static boolean aff_done, aff_todo, aff_passed, aff_ondate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        nb_tasks = (TextView) findViewById(R.id.nb_tasks);
        aff_done = true;
        aff_todo = true;
        aff_passed = true;
        aff_ondate = true;
        id = 0;

        CheckBox checkToDo = (CheckBox) findViewById(R.id.switch_todo);
        checkToDo.setChecked(true);
        checkToDo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    aff_todo = true;
                else
                    aff_todo = false;
                affListCorresponding();
            }
        });
        CheckBox checkDone = (CheckBox) findViewById(R.id.switch_done);
        checkDone.setChecked(true);
        checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_done = true;
                else
                    aff_done = false;
                affListCorresponding();
            }
        });
        CheckBox checkPassed = (CheckBox) findViewById(R.id.switch_passed);
        checkPassed.setChecked(true);
        checkPassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_passed = true;
                else
                    aff_passed = false;
                affListCorresponding();
            }
        });
        CheckBox checkOnDate = (CheckBox) findViewById(R.id.switch_ondate);
        checkOnDate.setChecked(true);
        checkOnDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_ondate = true;
                else
                    aff_ondate = false;
                affListCorresponding();
            }
        });
       getData();
        getCatData();
        if (cat.size() == 0)
            cat.add(new Category("none", Color.parseColor("#262D3B")));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentMain = new Intent(MainToDoActivity.this, EditItem.class);
                Items1 item = (Items1) mListView.getAdapter().getItem(position);
                String title = item.getTitle();
                String time = item.getTime();
                String txt = item.getText();
                String date = item.getDate();
                String categorie = item.getCategorie();
                intentMain.putExtra("position", String.valueOf(position));
                intentMain.putExtra("title", title);
                intentMain.putExtra("txt", txt);
                intentMain.putExtra("date", date);
                intentMain.putExtra("time", time);
                intentMain.putExtra("categorie", categorie);
                startActivityForResult(intentMain, 1);
            }
        });

        ItemsAdapter1 adapter = new ItemsAdapter1(MainToDoActivity.this, items);
        mListView.setAdapter(adapter);
        checkDate();
    }

    public void getData() {
        List<Items1> list = new ArrayList<>();
        Items1 tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(Titre VARCHAR, Date VARCHAR, Status VARCHAR, Txt VARCHAR, Cat VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from tasks", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount())
        {
            String title = resultSet.getString(resultSet.getColumnIndex("Titre"));
            String date = resultSet.getString(resultSet.getColumnIndex("Date"));
            String status = resultSet.getString(resultSet.getColumnIndex("Status"));
            String txt = resultSet.getString(resultSet.getColumnIndex("Txt"));
            String cat = resultSet.getString(resultSet.getColumnIndex("Cat"));
            Date d = new Date();
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyyHH:mm");
            try {
                d = newDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tmp = new Items1(title, txt, d);
            if (status.equals(Items1.Status.DONE.toString()))
                tmp.setStatus(Items1.Status.DONE);
            else
                tmp.setStatus(Items1.Status.TODO);
            tmp.setCategorie(cat);
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        items = list;
    }


    public void getCatData() {
        ArrayList<Category> list = new ArrayList<>();
        Category tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from cats", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount()) {
            String name = resultSet.getString(resultSet.getColumnIndex("Name"));
            String color = resultSet.getString(resultSet.getColumnIndex("Color"));
            tmp = new Category(name, Integer.parseInt(color));
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        cat = list;
    }

    /**
     * Returns value to insert in db
     *
     * @return
     */
    public String addToDataBase(int i) {
        Items1 tmp = items.get(i);
        String query = "'";
        query += tmp.getTitle() + "','";
        query += tmp.getDate() + tmp.getTime() + "','";
        query += tmp.getStatus().toString() + "','";
        query += tmp.getText() + "','";
        query += tmp.getCategorie() + "'";
        return query;
    }

    /**
     * Sauvegarde les tasks dans la db
     */
    public void saveData() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS tasks");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(Titre VARCHAR, Date VARCHAR, Status VARCHAR, Txt VARCHAR, Cat VARCHAR);");

        for (int i = 0; i < items.size(); i++) {
            query = addToDataBase(i);
            mydatabase.execSQL("INSERT INTO tasks VALUES(" + query + ");");
        }
    }


    public void saveCategory() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS cats");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        for (int i = 0; i < cat.size(); i++) {
            query = "'" + cat.get(i).getName() + "','" +  String.valueOf(cat.get(i).getColor()) + "'";
            mydatabase.execSQL("INSERT INTO cats VALUES(" + query + ");");
        }
    }

    /**
     *
     * @param V
     */
    public void settings(View V) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     *
     * @param v
     */
    public void add(View v) {
        Intent intentMain = new Intent(MainToDoActivity.this, ZAdditem1.class);
        startActivityForResult(intentMain, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String txt = data.getStringExtra("txt");
                String date = data.getStringExtra("date");
                String delete = data.getStringExtra("btn_delete");
                String category = data.getStringExtra("categorie");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyy k:m");
                Date d = null;
                try {
                    d = newDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (data.getStringExtra("edit").equals("true")) {
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    try {
                        modifyItem(position, title, txt, d, delete, category);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Items1 newItem = new Items1(title, txt, d);
                    newItem.setCategorie(category);
                    try {
                        addToList(newItem);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //here goes nothing
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                saveCategory();
                checkCategories();
                affListCorresponding();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                saveCategory();
                checkCategories();
                affListCorresponding();
            }
        }
    }


    public void checkDate() {
        int i = 0;
        Date d;

        d = new Date();
//        nb_tasks.setText(String.valueOf(items.size()) + " Tasks");
        while (i < items.size()) {
            if (!(items.get(i).getRealDate().after(d))) {
                items.get(i).setPassed(true);
                items.get(i).setDateColor("#FF0000");

            } else {
                items.get(i).setPassed(false);
                items.get(i).setDateColor("#121212");
            }
            i++;
        }
    }

    /**
     *
     * @param item
     * @throws ParseException
     */
    public void addToList(Items1 item) throws ParseException {
        items.add(item);
        checkDate();
        saveData();
        Date f = new Date();
        int c = 0;
        int color = Color.BLUE;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        int delay = (int) (item.getRealDate().getTime() - f.getTime());
        if (delay > 0)
            scheduleNotification(getNotification(item.getTitle(), item.getText(), color), delay);
        affListCorresponding();
    }

    /**
     *
     * @param position
     * @param title
     * @param txt
     * @param d
     * @param delete
     * @param cate
     * @throws ParseException
     */
    public void modifyItem(int position, String title, String txt, Date d, String delete, String cate) throws ParseException {
        Items1 item = items.get(position);
        if (delete.equals("false")) {
            item.setTitle(title);
            item.setText(txt);
            item.setDueDate(d);
            item.setCategorie(cate);
        } else
            items.remove(item);
        checkDate();
        saveData();
        Date f = new Date();
        int delay = (int) (d.getTime() - f.getTime());
        int color = Color.BLUE;
        int c = 0;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        if (delay > 0)
            scheduleNotification(getNotification(title, txt, color), delay);
        affListCorresponding();
    }


    /**
     *
     * @param item
     * @return
     */
    public boolean showCatForItem(Items1 item) {
        int i = 0;
        while (i < cat.size()) {
            if (cat.get(i).getName().equals(item.getCategorie())) {
                return cat.get(i).getShow();
            }
            i++;
        }
        return false;
    }


    public void affListCorresponding() {
        int nb_items = items.size();
        boolean t, p;
        int i = 0;
        tmp.clear();
        while (i < nb_items) {
            t = false;
            p = false;
            if (aff_done && items.get(i).getStatus() == Items1.Status.DONE)
                t = true;
            if (aff_todo && items.get(i).getStatus() == Items1.Status.TODO)
                t = true;
            if ((aff_passed && items.get(i).getPassed()))
                p = true;
            if ((aff_ondate && !items.get(i).getPassed()))
                p = true;
            if (t && p && showCatForItem(items.get(i)))
                tmp.add(items.get(i));
            i++;
        }
        ItemsAdapter1 adapter = new ItemsAdapter1(MainToDoActivity.this, tmp);
        mListView.setAdapter(adapter);
//        if (tmp.size() > 1)
//            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Tasks");
//        else
//            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Task");
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param v
     */
    public void todoClick(View v) {
        final int position = mListView.getPositionForView((View) v.getParent());
        SwipeLayout s = (SwipeLayout) mListView.getChildAt(position);
        Items1 a = items.get(position);
        a.setStatus(Items1.Status.TODO);
        affListCorresponding();
        saveData();
        s.close(true);
    }


    public void doneClick(View v) {
        final int position = mListView.getPositionForView((View) v.getParent());
        SwipeLayout s = (SwipeLayout) mListView.getChildAt(position);
        Items1 a = items.get(position);
        a.setStatus(Items1.Status.DONE);
        s.close(true);
        ItemsAdapter1 b = (ItemsAdapter1) mListView.getAdapter();
        affListCorresponding();
        b.notifyDataSetChanged();
        saveData();
    }

    /**
     *
     * @param notification
     * @param delay
     */
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, ToDoAppNotification.class);
        notificationIntent.putExtra(ToDoAppNotification.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(ToDoAppNotification.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification(String Title, String content, int color) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(Title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.btn_ic_notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(color);
        }
        affListCorresponding();
        return builder.build();
    }


    public static ArrayList<Category> getCatA() {
        ArrayList<Category> tmp = new ArrayList<Category>();
        int i = 0;
        while (i < cat.size())
            tmp.add(cat.get(i++));
        return tmp;
    }


    public void closeMenu(View v) {
        DrawerLayout d = ((DrawerLayout) findViewById(R.id.drawer_layout));
        d.closeDrawers();
    }

    public static ArrayList<Category> getCat() {
        return (getCatA());
    }


    public void checkCategories() {
        int i = 0;

        while (i < items.size()) {
            int c = 0;
            boolean found = false;
            while (c < cat.size()) {
                if (items.get(i).getCategorie().equals(cat.get(c).getName()))
                    found = true;
                c++;
            }
            if (!found)
                items.get(i).setCategorie("none");
            i++;
        }
        affListCorresponding();
    }

    /**
     *
     * @param v
     */
    public void addCategorie(View v) {
        Intent intentMain = new Intent(MainToDoActivity.this, ZaddCat.class);
        startActivityForResult(intentMain, 2);
        checkCategories();
    }
}

