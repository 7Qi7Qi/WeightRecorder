package com.example.yui.ui.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;

import com.example.yui.databinding.FragmentStatisticsBinding;
import com.example.yui.server.YuiSQLiteHelper;
import com.example.yui.server.model.Record;
import com.example.yui.server.model.StatisticsType;
import com.example.yui.server.util.SystemUtils;
import com.example.yui.ui.main.linechart.LineChartManagerV2;
import com.example.yui.ui.main.linechart.LineChartManagerV1;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;

import java.sql.Timestamp;
import java.util.*;

public class StatisticsFragment extends AbstractFragment {

    private static final Integer TARGET_WEIGHT = 70;

    private StatisticsType activeStatType = StatisticsType.LIST;
    private FragmentStatisticsBinding binding;

    private TableLayout tableList = null;
    private LineChartManagerV1 chartSimpleManager = null;
    private LineChartManagerV2 chartManager = null;


    public StatisticsFragment(YuiSQLiteHelper sqLiteHelper, Context mContext) {
        super(sqLiteHelper, mContext);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        binding.bList.setOnClickListener(view -> {
            clickListener(view, StatisticsType.LIST);
        });
        binding.bLineChart.setOnClickListener(view -> {
            clickListener(view, StatisticsType.LINE_CHART);
        });
        binding.bLineChart1.setOnClickListener(view -> {
            clickListener(view, StatisticsType.LINE_CHART_SIMPLE);
        });

        return binding.getRoot();
    }

    private void clickListener(View view, StatisticsType curType) {
        if (activeStatType != curType) {
            activeStatType = curType;
            this.doBusiness(view);
        }
    }

    @Override
    public void doBusiness(View view) {
        try {
            List<Record> records = sqLiteHelper.queryRecords();
            View chartView;
            if (activeStatType == StatisticsType.LIST) {
                chartView = setTableView(records);
            } else {
                chartView = setLineChart(records);
            }
            LinearLayout layout = binding.linearLayout;
            layout.removeAllViews();
            layout.addView(chartView);
            showMessage("Statistics Success: " + activeStatType);
        } catch (Exception ex) {
            showMessage("Query error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private View setTableView(List<Record> records) {
        if (tableList == null) {
            tableList = new TableLayout(getContext());
        }
        tableList.removeAllViews();
        TableRow title = createTableRow();
        title.addView(createTableView("序号"));
        title.addView(createTableView("记录时间"));
        title.addView(createTableView("体重"));

        tableList.addView(title);
        tableList.addView(createLineView(2, Color.BLUE));

        Collections.reverse(records);
        for (Record record : records) {
            TableRow data = createTableRow();
            data.addView(createTableView(record.getId()));
            data.addView(createTableView(SystemUtils.formatDate(record.dateTime())));
            data.addView(createTableView(record.getWeight() + "kg"));
            tableList.addView(data);
            tableList.addView(createLineView(1, Color.BLACK));
        }
        return tableList;
    }

    private View setLineChart(List<Record> records) {
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        if (!records.isEmpty()) {
            startTime = records.get(0).dateTime();
        }

        List<Entry> entries = new ArrayList<>();
        for (Record record : records) {
            long x = (SystemUtils.getDateDiff(record.dateTime(), startTime)) / (1000 * 60 * 60 * 12);
            float y = record.getWeight().floatValue();
            Entry entry = new Entry(x, y);
            entries.add(entry);
        }

//        float[] dataObjects = {1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1};
//        List<Entry> entries2 = new ArrayList<>();
//        for (int i = 0; i < dataObjects.length; i++) {
//            float data = dataObjects[i];
//            entries2.add(new Entry(i, data));
//        }

        if (activeStatType == StatisticsType.LINE_CHART) {
            return setLineChartBetter(entries, startTime);
        } else if (activeStatType == StatisticsType.LINE_CHART_SIMPLE) {
            return setLineChartSimple(entries);
        } else {
            showMessage("None stat type: " + activeStatType);
            return null;
        }
    }

    private LineChart createDefLineChart() {
        LineChart lineChart = new LineChart(getContext());
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(
                1080, 1280));
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return lineChart;
    }

    private LineChart setLineChartSimple(List<Entry> entries) {
        LineChart lineChart;
        if (chartSimpleManager == null) {
            lineChart = createDefLineChart();
            chartSimpleManager = new LineChartManagerV1(lineChart);
        } else {
            lineChart = chartSimpleManager.getLineChart();
        }
        chartSimpleManager.showLineChart(entries);
        return lineChart;
    }

    private LineChart setLineChartBetter(List<Entry> entries, Timestamp startTime) {
        LineChart lineChart;
        if (chartManager == null) {
            lineChart = createDefLineChart();
            chartManager = new LineChartManagerV2(lineChart);
        } else {
            lineChart = chartManager.getLineChart();
        }
        chartManager.showLineChart(entries, "体重变化线", Color.BLUE);
        chartManager.setDescription("天数: 从" + SystemUtils.formatDateDay(startTime) +"起");
        return lineChart;
    }


}
