package com.madrzak.androidtreeviewmanytypes;

import android.content.Context;
import android.util.Log;

import com.madrzak.androidtreeviewmanytypes.model.Account;
import com.madrzak.androidtreeviewmanytypes.model.Group;
import com.madrzak.androidtreeviewmanytypes.model.MyNodeType;
import com.madrzak.androidtreeviewmanytypes.model.MyTreeNodeViewModel;
import com.madrzak.androidtreeviewmanytypes.model.User;
import com.madrzak.androidtreeviewmanytypes.treeview.SelectableItemHolder;
import com.unnamed.b.atv.model.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static rx.Observable.from;

/**
 * Created by Łukasz on 05/05/2017.
 */

public class NodeTreeProvider {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context context;

    private static final NodeTreeProvider instance = new NodeTreeProvider();

    private Set<MyNodeType> filterOut;

    static NodeTreeProvider getInstance() {
        return instance;
    }

    void init(Context context) {
        this.context = context;
        generateDummyData();
    }

    private Map<Long, MyTreeNodeViewModel> nodeViews = new HashMap<>();


    public TreeNode generateTree(Set<MyNodeType> filter) {
        filterOut = filter;
        TreeNode root = TreeNode.root();

        from(nodeViews.values())
                .filter(MyTreeNodeViewModel::isRoot)
                .forEach(nodeViewModel -> {
                    if (nodeViewModel.getTreeNode().getNodeType().equals(MyNodeType.ACCOUNT)) {
                        buildBranch(root, nodeViewModel);
                    }
                });

        return root;
    }


    private void generateDummyData() {

        nodeViews.put(1L, new MyTreeNodeViewModel(new Account(1, "account 1"), null));
        nodeViews.put(2L, new MyTreeNodeViewModel(new Account(2, "account 2"), null));
        nodeViews.put(3L, new MyTreeNodeViewModel(new Account(3, "account 3"), 1L));
        nodeViews.put(4L, new MyTreeNodeViewModel(new Account(4, "account 4"), 1L));
        nodeViews.put(5L, new MyTreeNodeViewModel(new Account(5, "account 5"), 1L));
        nodeViews.put(6L, new MyTreeNodeViewModel(new Group(6, "group 6"), 2L));
        nodeViews.put(7L, new MyTreeNodeViewModel(new Group(7, "group 7"), 2L));
        nodeViews.put(8L, new MyTreeNodeViewModel(new User(8, "user 1"), 2L));
        nodeViews.put(9L, new MyTreeNodeViewModel(new Account(9, "account 3"), null));
        nodeViews.put(10L, new MyTreeNodeViewModel(new User(10, "user 2"), 7L));
        nodeViews.put(11L, new MyTreeNodeViewModel(new User(11, "user 3"), 7L));

        // assigning children id's to all nodes
        from(nodeViews.values())
                .doOnError(throwable -> Log.i(TAG, throwable.toString()))
                .forEach(item -> {
                    Log.i(TAG, "item " + item.getTreeNode().getName() + " " + item.getParentId() + " " + item.getTreeNode().getId());
                    if (item.getParentId() != null && nodeViews.get(item.getParentId()) != null) {
                        nodeViews.get(item.getParentId()).addChild(item.getTreeNode().getId());
                    }

                    nodeViews.put(item.getTreeNode().getId(), item);
                });
    }


    private void buildBranch(TreeNode treeNode, MyTreeNodeViewModel model) {
        if (filterOut != null && filterOut.contains(model.getTreeNode().getNodeType())) {
            return;
        }

        TreeNode newNode = new TreeNode(model.getTreeNode()).setViewHolder(new SelectableItemHolder(context));
        treeNode.addChild(newNode);
        for (Long nodeId : model.getChildren()) {
            buildBranch(newNode, nodeViews.get(nodeId));
        }
    }


}
