package com.madrzak.androidtreeviewmanytypes.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Łukasz on 05/05/2017.
 */

public class MyTreeNodeViewModel {


    private Long parentId;
    private MyTreeNode treeNode;
    private Set<Long> children = new HashSet<>();

    public MyTreeNodeViewModel(MyTreeNode treeNode, Long parentId) {
        this.treeNode = treeNode;
        this.parentId = parentId;
    }

    public MyTreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(MyTreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Set<Long> getChildren() {
        return children;
    }

    public void setChildren(Set<Long> children) {
        this.children = children;
    }

    public void addChild(Long child) {
        children.add(child);
    }

    public boolean isRoot() {
        return parentId == null;
    }
}
