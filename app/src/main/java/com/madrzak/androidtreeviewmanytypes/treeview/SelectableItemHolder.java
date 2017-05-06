package com.madrzak.androidtreeviewmanytypes.treeview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.madrzak.androidtreeviewmanytypes.R;
import com.madrzak.androidtreeviewmanytypes.model.MyNodeType;
import com.madrzak.androidtreeviewmanytypes.model.MyTreeNode;
import com.unnamed.b.atv.model.TreeNode;


/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class SelectableItemHolder extends TreeNode.BaseNodeViewHolder<MyTreeNode> {
    private static final String TAG = SelectableItemHolder.class.getSimpleName();

    private TextView tvValue;
    private PrintView arrowView;
    private CheckBox nodeSelector;

    public SelectableItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, MyTreeNode value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_item, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.getName());

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        if (value.getNodeType().equals(MyNodeType.ACCOUNT)) {
            iconView.setIconText(context.getResources().getString(R.string.ic_location_city));
        } else if (value.getNodeType().equals(MyNodeType.GROUP)) {
            iconView.setIconText(context.getResources().getString(R.string.ic_folder));
        } else if (value.getNodeType().equals(MyNodeType.USER)){
            iconView.setIconText(context.getResources().getString(R.string.ic_person));
        }

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        if (node.getLevel() > 1) {
            arrowView.setPadding(44 * (node.getLevel()-1), 0, 0, 0);
            Log.i(TAG, value.getName() + " " + node.getLevel());
        }

        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, isChecked);
                }
            }
        });
        nodeSelector.setChecked(node.isSelected());

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }
}
