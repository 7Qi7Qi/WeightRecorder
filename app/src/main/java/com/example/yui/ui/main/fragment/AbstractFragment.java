package com.example.yui.ui.main.fragment;

import android.content.Context;
import android.view.*;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yui.server.YuiSQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

public abstract class AbstractFragment extends Fragment {

    protected YuiSQLiteHelper sqLiteHelper;
    private Context mContext;

    public AbstractFragment(YuiSQLiteHelper sqLiteHelper, Context mContext) {
        this.sqLiteHelper = sqLiteHelper;
        this.mContext = mContext;
    }

    public abstract void doBusiness(View view);

    protected TextView createTableView(Object text) {
        TextView textView = new TextView(mContext);
        textView.setText(text.toString());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setPadding(20, 5, 5, 5);
        return textView;
    }

    protected TableRow createTableRow() {
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return tableRow;
    }

    protected View createLineView(int h, int color) {
        View view = new View(mContext);
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, h));
        view.setBackgroundColor(color);
        return view;
    }

    protected void showMessage(String text) {
        Snackbar.make( getView(), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
