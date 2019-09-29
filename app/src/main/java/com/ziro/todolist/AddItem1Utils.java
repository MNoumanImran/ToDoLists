package com.ziro.todolist;

import android.widget.TextView;

public class AddItem1Utils {

    public boolean IsTitleEmpty(String s){

        if (s.length() < 0 || s.equals("")) {
            return false;

        }else {
            return true;
        }
    }
    public boolean isDiscriptionEmpty(String s){

        if (s.length() < 0 || s.equals("")) {
            return false;

        }else {
            return true;
        }
    }

}
