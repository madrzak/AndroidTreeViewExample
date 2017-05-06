package com.madrzak.androidtreeviewmanytypes.model;

/**
 * Created by Łukasz on 05/05/2017.
 */

public class User implements MyTreeNode {

    private long id;
    private String name;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public MyNodeType getNodeType() {
        return MyNodeType.USER;
    }

    public void setName(String name) {
        this.name = name;
    }
}
