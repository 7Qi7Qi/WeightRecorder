package com.example.yui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yui.databinding.ActivityMainBinding;
import com.example.yui.server.YuiSQLiteHelper;
import com.example.yui.ui.main.SectionsPagerAdapter;
import com.example.yui.ui.main.fragment.*;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static YuiSQLiteHelper sqLiteHelper;
    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper = new YuiSQLiteHelper(this);
        mContext = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AbstractFragment[] fragments = new AbstractFragment[2];
        fragments[0] = new AddFragment(sqLiteHelper, mContext);
        fragments[1] = new StatisticsFragment(sqLiteHelper, mContext);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(adapter);

        binding.tabs.setupWithViewPager(viewPager);
        binding.fab.setOnClickListener(view -> adapter.doBusiness(view, binding.tabs.getSelectedTabPosition()));

    }

}