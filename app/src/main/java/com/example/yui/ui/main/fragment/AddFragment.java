package com.example.yui.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;

import com.example.yui.databinding.FragmentAddBinding;
import com.example.yui.server.YuiSQLiteHelper;
import com.example.yui.server.model.Record;

import java.sql.Timestamp;


public class AddFragment extends AbstractFragment {

    private FragmentAddBinding binding;

    public AddFragment(YuiSQLiteHelper sqLiteHelper, Context mContext) {
        super(sqLiteHelper, mContext);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.eWeight;

        // 限定数字
        textView.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

        TextClock textClock = binding.tDate;
        textClock.setFormat12Hour("yyyy年MM月dd日 HH:mm:ss");

        Button bDelete = binding.bDelete;
        bDelete.setOnClickListener(view -> {
            CharSequence text = binding.eId.getText();
            if (text == null || text.length() == 0) {
                showMessage("序号为空，删除失败");
                return;
            }
            try {
                boolean result = sqLiteHelper.deleteById(String.valueOf(text));
                showMessage(result ? "delete success. " : "delete fail");
            } catch (Exception ex) {
                showMessage("Occur error: " + ex.getMessage());
            }
        });
        return root;
    }

    @Override
    public void doBusiness(View view) {
        EditText eWeight = binding.eWeight;
        CharSequence text = eWeight.getText();
        if (text == null || text.length() == 0) {
            showMessage("Pls input weight first !!!");
            return;
        }

        try {
            Record record = new Record(Double.parseDouble(String.valueOf(text)), new Timestamp(System.currentTimeMillis()));
            sqLiteHelper.add(record);
            showMessage("Success Record");
            eWeight.setText(null);

        } catch (Exception ex) {
            showMessage("Error Input, check again !!!");
        }
    }
}
