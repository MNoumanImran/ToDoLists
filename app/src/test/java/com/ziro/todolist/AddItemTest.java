package com.ziro.todolist;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddItemTest {

    @Test
    public void isTitleEmpty1() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.IsTitleEmpty("hello");
        assertEquals(true,chk );
    }
    @Test
    public void isTitleEmpty2() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.IsTitleEmpty("hello");
        assertTrue(chk );
    }
    @Test
    public void isTitleEmpty3() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.IsTitleEmpty("");
        assertFalse(chk );
    }



    @Test
    public void isDiscriptionEmpty1() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.isDiscriptionEmpty("hello");
        assertEquals(true,chk );
    }
    @Test
    public void isDiscriptionEmpty2() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.isDiscriptionEmpty("hello");
        assertTrue(chk );
    }
    @Test
    public void isDiscriptionEmpty3() throws Exception {
        AddItem1Utils n=new AddItem1Utils();
        boolean chk=n.isDiscriptionEmpty("");
        assertFalse(chk );
    }


}