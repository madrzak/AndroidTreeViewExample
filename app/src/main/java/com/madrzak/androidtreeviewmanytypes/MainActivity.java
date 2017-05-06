package com.madrzak.androidtreeviewmanytypes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.madrzak.androidtreeviewmanytypes.model.Account;
import com.madrzak.androidtreeviewmanytypes.model.Group;
import com.madrzak.androidtreeviewmanytypes.model.MyNodeType;
import com.madrzak.androidtreeviewmanytypes.model.MyTreeNodeViewModel;
import com.madrzak.androidtreeviewmanytypes.treeview.IconTreeItemHolder;
import com.madrzak.androidtreeviewmanytypes.treeview.SelectableHeaderHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static rx.Observable.from;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.container)
    RelativeLayout container;

    private AndroidTreeView tView;
    private Map<Long, MyTreeNodeViewModel> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.activity_main, null);

        ButterKnife.bind(this, view);

        setContentView(view);

        TreeNode root = generateTree(generateDummyData());
        tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        tView.setSelectionModeEnabled(true);

        container.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        generateDummyData();
    }


    private List<MyTreeNodeViewModel> generateDummyData() {
        List<MyTreeNodeViewModel> result = new ArrayList<>();

        result.add(new MyTreeNodeViewModel(new Account(1, "account 1"), null));
        result.add(new MyTreeNodeViewModel(new Account(2, "account 2"), null));
        result.add(new MyTreeNodeViewModel(new Account(3, "account 3"), 1L));
        result.add(new MyTreeNodeViewModel(new Account(4, "account 4"), 1L));
        result.add(new MyTreeNodeViewModel(new Account(5, "account 5"), 1L));
        result.add(new MyTreeNodeViewModel(new Group(6, "group 6"), 2L));
        result.add(new MyTreeNodeViewModel(new Group(7, "group 7"), 2L));

        // assigning children id's to all nodes
        from(result)
                .doOnError(throwable -> Log.i(TAG, throwable.toString()))
                .forEach(item -> {
                    Log.i(TAG, "item " + item.getTreeNode().getName() + " " + item.getParentId() + " " + item.getTreeNode().getId());
                    if (item.getParentId() != null) {
                        map.get(item.getParentId()).addChild(item.getTreeNode().getId());
                    }

                    map.put(item.getTreeNode().getId(), item);
                })
        ;

        return result;
    }


    private TreeNode generateTree(List<MyTreeNodeViewModel> data) {
        TreeNode root = TreeNode.root();

        from(data)
                .filter(MyTreeNodeViewModel::isRoot)
                .forEach(nodeViewModel -> {
                    if (nodeViewModel.getTreeNode().getNodeType().equals(MyNodeType.ACCOUNT)) {
                        TreeNode newNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, nodeViewModel.getTreeNode().getName(), nodeViewModel.getTreeNode().getId())).setViewHolder(new SelectableHeaderHolder(this));
                        root.addChild(newNode);
                        buildBranch(newNode,nodeViewModel);

                    }
                });

        return root;
    }

    private TreeNode buildBranch(TreeNode treeNode, MyTreeNodeViewModel model) {
        TreeNode newNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, model.getTreeNode().getName(), model.getTreeNode().getId())).setViewHolder(new SelectableHeaderHolder(this));
        treeNode.addChild(newNode);
        for (Long nodeId : model.getChildren()) {
            buildBranch(newNode, map.get(nodeId));
        }
        return newNode;
    }


}
