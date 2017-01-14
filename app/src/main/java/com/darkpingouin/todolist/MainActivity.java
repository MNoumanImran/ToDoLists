package com.darkpingouin.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ListView mListView;
    List<Item> items = new ArrayList<>();
    List<Item> tmp = new ArrayList<>();

    TextView nb_tasks;
    boolean aff_done, aff_todo, aff_passed, aff_ondate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        nb_tasks = (TextView) findViewById(R.id.nb_tasks);
        aff_done = true;
        aff_todo = true;
        aff_passed = true;
        aff_ondate = true;
        Switch switchTodo = (Switch) findViewById(R.id.switch_todo);
        switchTodo.setChecked(true);
        switchTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_todo = true;
                else
                    aff_todo = false;
                affListCorresponding();
            }
        });
        Switch switchDone = (Switch) findViewById(R.id.switch_done);

        switchDone.setChecked(true);
        switchDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_done = true;
                else
                    aff_done = false;
                affListCorresponding();
            }
        });
        Switch switchPassed = (Switch) findViewById(R.id.switch_passed);
        switchPassed.setChecked(true);
        switchPassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_passed = true;
                else
                    aff_passed = false;
                affListCorresponding();
            }
        });
        Switch switchOnDate = (Switch) findViewById(R.id.switch_ondate);
        switchOnDate.setChecked(true);
        switchOnDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_ondate = true;
                else
                    aff_ondate = false;
                affListCorresponding();
            }
        });
        try {
            getData();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentMain = new Intent(MainActivity.this, EditItem.class);
                Item item = (Item) mListView.getAdapter().getItem(position);
                String title = item.getTitle();
                String time = item.getTime();
                String txt = item.getText();
                String date = item.getDate();
                intentMain.putExtra("position", String.valueOf(position));
                intentMain.putExtra("title", title);
                intentMain.putExtra("txt", txt);
                intentMain.putExtra("date", date);
                intentMain.putExtra("time", time);
                startActivityForResult(intentMain, 1);
            }
        });
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, items);
        mListView.setAdapter(adapter);
        checkDate();
    }

    public List<Item> dataToItems(String data) throws ParseException, XmlPullParserException, IOException {
        List<Item> list = new ArrayList<>();
        Item tmp;
        String title = "";
        String txt = "";
        Date date = new Date();
        int f = 0;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(data));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.TEXT) {
                if (f == 0)
                    title = xpp.getText();
                else if (f == 1) {
                    String d = xpp.getText();
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyyHH:mm");
                    date = newDateFormat.parse(d);
                } else if (f == 3)
                    txt = xpp.getText();
                f++;
            }
            if (f == 4) {
                tmp = new Item(title, txt, date);
                list.add(tmp);
                f = 0;
            }
            eventType = xpp.next();
        }
        return list;
    }

    public void getData() throws ParseException, IOException, XmlPullParserException {
        int c;
        String temp = "";
        FileInputStream fin = null;
        try {
            fin = openFileInput("tasksSave");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(temp);
        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        items = dataToItems(temp);
    }

    public void saveData() {
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput("tasksSave", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //ItemAdapter a = (ItemAdapter) mListView.getAdapter();
        Item tmp;

        for (int i = 0; i < items.size(); i++) {
            tmp = items.get(i);
            try {
                fOut.write(("<t>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(tmp.getTitle().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("</t>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("<d>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write((tmp.getDate() + tmp.getTime()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("</d>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("<s>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write((tmp.getStatus().toString()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("</s>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("<x>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write((tmp.getText()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.write(("</x>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Date getDate(int day, int month, int year, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void settings(View V) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void add(View v) {
        Intent intentMain = new Intent(MainActivity.this, AddItem.class);
        startActivityForResult(intentMain, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String txt = data.getStringExtra("txt");
                String date = data.getStringExtra("date");
                String delete = data.getStringExtra("delete");
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
                        modifyItem(position, title, txt, d, delete);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Item newItem = new Item(title, txt, d);
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
    }

    public void checkDate() {
        int i = 0;
        Date d;

        d = new Date();
        nb_tasks.setText(String.valueOf(items.size()) + " Tasks");
        while (i < items.size()) {
            if (!(items.get(i).getRealDate().after(d))) {
                items.get(i).setPassed(true);
                items.get(i).setDateColor("#FF0000");
            } else {
                items.get(i).setPassed(false);
                items.get(i).setDateColor("#DFDFDF");
            }
            i++;
        }
    }

    public void addToList(Item item) throws ParseException {
        items.add(item);
        checkDate();
        saveData();
        affListCorresponding();
    }

    public void modifyItem(int position, String title, String txt, Date d, String delete) throws ParseException {
        Item item = items.get(position);
        if (delete.equals("false")) {
            item.setTitle(title);
            item.setText(txt);
            item.setDueDate(d);
        } else
            items.remove(item);
        checkDate();
        saveData();
        affListCorresponding();
    }

    public void affListCorresponding() {
        int nb_items = items.size();
        boolean t, d, p;
        int i = 0;
        tmp.clear();
        while (i < nb_items) {
            t = false;
            p = false;
            if (aff_done && items.get(i).getStatus() == Item.Status.DONE)
                t = true;
            if (aff_todo && items.get(i).getStatus() == Item.Status.TODO)
                t = true;
            if ((aff_passed && items.get(i).getPassed()))
                p = true;
            if ((aff_ondate && !items.get(i).getPassed()))
                p = true;
            if (t && p)
                tmp.add(items.get(i));
            i++;
        }
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, tmp);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void refresh(View v)
    {
        affListCorresponding();
    }
}
