package com.ziro.todolist;



public class Categorie {
    private String name;
    private int color;
    private boolean show;

    /**
     * @param n nom
     * @param c couleur
     */
    public Categorie(String n, int c)
    {
        this.name = n;
        this.color = c;
        show = true;
    }


    public String getName()
    {
        return (this.name);
    }


    public int getColor()
    {
        return (this.color);
    }

    /**
     * @param n nom
     */
    public void setName(String n)
    {
        this.name = n;
    }

    /**
     * @param c couleur
     */
    public void setColor(int c)
    {
        this.color = c;
    }


    public boolean getShow() {
        return this.show;
    }

    /**
     * @param show1 true ou false
     */
    public void setShow(boolean show1) {
        this.show = show1;
    }
}
