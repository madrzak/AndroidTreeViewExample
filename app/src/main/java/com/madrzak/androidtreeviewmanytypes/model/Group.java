package com.madrzak.androidtreeviewmanytypes.model;

/**
 * Created by Łukasz on 05/05/2017.
 */

public class Group implements MyTreeNode {

    private long id;
    private String name;

    public Group(long id, String name) {
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
        return MyNodeType.GROUP;
    }

    public void setName(String name) {
        this.name = name;
    }
}
