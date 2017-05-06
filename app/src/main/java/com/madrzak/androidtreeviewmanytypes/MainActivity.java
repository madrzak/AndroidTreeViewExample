package com.madrzak.androidtreeviewmanytypes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.container)
    RelativeLayout container;

    private AndroidTreeView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.activity_main, null);

        ButterKnife.bind(this, view);

        setContentView(view);

        NodeTreeProvider.getInstance().init(this);

        TreeNode root = NodeTreeProvider.getInstance().generateTree();

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

    }




}
