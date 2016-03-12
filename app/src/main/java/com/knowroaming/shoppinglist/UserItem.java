package com.knowroaming.shoppinglist;

/**
 * Created by Daddy on 2016-03-11.
 */
public class UserItem {

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;

    public UserItem(int id, String name) {
        this.setId(id);
        this.setName(name);
    }
}
