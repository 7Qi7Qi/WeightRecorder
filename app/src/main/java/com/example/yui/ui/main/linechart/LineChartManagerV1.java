package com.example.yui.ui.main.linechart;

import android.graphics.Color;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.*;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class LineChartManagerV1 {

    private LineChart lineChart;
    private LineDataSet dataSet;
    private boolean initial;

    public LineChartManagerV1(LineChart lineChart) {
        this.lineChart = lineChart;
        initial = true;
    }

    public LineChart getLineChart() {
        return lineChart;
    }

    public void showLineChart(List<Entry> entries) {
        if (initial) {
            initLineDataSet(entries);
            initLineData();
            initLineChart();
            initial = false;
        } else {
            dataSet.setValues(entries);
            lineChart.setData(lineChart.getLineData());
        }
    }

    private void initLineChart() {

        // **************************图表本身一般样式**************************** //
        lineChart.setBackgroundColor(Color.WHITE); // 整个图标的背景色
        lineChart.setContentDescription("××表");   // 右下角的描述文本,测试并不显示
        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText("变化表");
        description.setTextColor(Color.BLACK);
        lineChart.setDescription(description);
        lineChart.setNoDataText("暂无数据");   // 没有数据时样式
        lineChart.setDrawGridBackground(false);    // 绘制区域的背景（默认是一个灰色矩形背景）将绘制，默认false
        lineChart.setGridBackgroundColor(Color.WHITE);  // 绘制区域的背景色
        lineChart.setDrawBorders(false);    // 绘制区域边框绘制，默认false
        lineChart.setBorderColor(Color.GRAY); // 边框颜色
        lineChart.setBorderWidth(2);   // 边框宽度,dp
        lineChart.setMaxVisibleValueCount(14);  // 数据点上显示的标签，最大数量，默认100。且dataSet.setDrawValues(true);必须为true。只有当数据数量小于该值才会绘制标签


        // *********************滑动相关*************************** //
        lineChart.setTouchEnabled(true); // 所有触摸事件,默认true
        lineChart.setDragEnabled(true);    // 可拖动,默认true
        lineChart.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(true);  // Y轴上的缩放,默认true
        lineChart.setPinchZoom(true);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChart.setDoubleTapToZoomEnabled(true); // 双击缩放,默认true
        lineChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true
        lineChart.setDragDecelerationFrictionCoef(0.9f);   // 摩擦系数,[0-1]，较大值速度会缓慢下降，0，立即停止;1,无效值，并转换为0.9999.默认0.9f.
        lineChart.setOnChartGestureListener(getGestureListener());


        // ************************高亮*************************** //
        lineChart.setHighlightPerDragEnabled(true);    // 拖拽时能否高亮（十字瞄准触摸到的点），默认true
        lineChart.setHighlightPerTapEnabled(true); // 双击时能都高亮，默认true
        lineChart.setMaxHighlightDistance(500);    // 最大高亮距离（dp）,点击位置距离数据点的距离超过这个距离不会高亮，默认500dp

        lineChart.highlightValue(1, 0);    // 高亮指定值，可以指定数据集的值,还有其他几个重载方法
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { // 值选择监听器
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // 选中
            }

            @Override
            public void onNothingSelected() {
                // 未选中
            }
        });


        // *************************轴****************************** //
        // 由四个元素组成：
        // 标签：即刻度值。也可以自定义，比如时间，距离等等，下面会说一下；
        // 轴线：坐标轴；
        // 网格线：垂直于轴线对应每个值画的轴线；
        // 限制线：最值等线。
        XAxis xAxis = lineChart.getXAxis();    // 获取X轴
        YAxis yAxis = lineChart.getAxisLeft(); // 获取Y轴,lineChart.getAxis(YAxis.AxisDependency.LEFT);也可以获取Y轴
        lineChart.getAxisRight().setEnabled(false);    // 不绘制右侧的轴线
        xAxis.setEnabled(true); // 轴线是否可编辑,默认true
        xAxis.setDrawLabels(true);  // 是否绘制标签,默认true
        xAxis.setDrawAxisLine(true);    // 是否绘制坐标轴,默认true
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setAxisMaximum(10); // 此轴能显示的最大值；
        xAxis.resetAxisMaximum();   // 撤销最大值；
        xAxis.setAxisMinimum(1);    // 此轴显示的最小值；
        xAxis.resetAxisMinimum();   // 撤销最小值；
//        yAxis.setStartAtZero(true);  // 从0开始绘制。已弃用。使用setAxisMinimum(float)；
//        yAxis.setInverted(true); // 反转轴,默认false
        yAxis.setSpaceTop(10);   // 设置最大值到图标顶部的距离占所有数据范围的比例。默认10，y轴独有
        // 算法：比例 = （y轴最大值 - 数据最大值）/ (数据最大值 - 数据最小值) ；
        // 用途：可以通过设置该比例，使线最大最小值不会触碰到图标的边界。
        // 注意：设置一条线可能不太好看，lineChart.getAxisRight().setSpaceTop(34)也设置比较好;同时，如果设置最小值，最大值，会影响该效果
        yAxis.setSpaceBottom(10);   // 同上，只不过是最小值距离底部比例。默认10，y轴独有
        // yAxis.setShowOnlyMinMax(true);   // 没找到。。。，true, 轴上只显示最大最小标签忽略指定的数量（setLabelCount，如果forced = false).
        yAxis.setLabelCount(4, false); // 纵轴上标签显示的数量,数字不固定。如果force = true,将会画出明确数量，但是可能值导致不均匀，默认（6，false）
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);  // 标签绘制位置。默认再坐标轴外面
        xAxis.setGranularity(1); // 设置X轴值之间最小距离。正常放大到一定地步，标签变为小数值，到一定地步，相邻标签都是一样的。这里是指定相邻标签间最小差，防止重复。
        yAxis.setGranularity(1);    // 同上
        yAxis.setGranularityEnabled(false); // 是否禁用上面颗粒度限制。默认false
        // 轴颜色
        yAxis.setTextColor(Color.BLUE);  // 标签字体颜色
        yAxis.setTextSize(10);    // 标签字体大小，dp，6-24之间，默认为10dp
        yAxis.setTypeface(null);    // 标签字体
        yAxis.setGridColor(Color.GRAY);    // 网格线颜色，默认GRAY
        yAxis.setGridLineWidth(1);    // 网格线宽度，dp，默认1dp
        yAxis.setAxisLineColor(Color.GRAY);  // 坐标轴颜色，默认GRAY.测试到一个bug，假如左侧线只有1dp，
        // 那么如果x轴有线且有网格线，当刻度拉的正好位置时会覆盖到y轴的轴线，变为X轴网格线颜色，结局办法是，要么不画轴线，要么就是坐标轴稍微宽点
        xAxis.setAxisLineColor(Color.RED);
        yAxis.setAxisLineWidth(2);  // 坐标轴线宽度，dp，默认1dp
        yAxis.enableGridDashedLine(20, 10, 1);    // 网格线为虚线，lineLength，每段实线长度,spaceLength,虚线间隔长度，phase，起始点（进过测试，最后这个参数也没看出来干啥的）
        // 限制线
        LimitLine ll = new LimitLine(6.5f, "上限"); // 创建限制线, 这个线还有一些相关的绘制属性方法，自行看一下就行，没多少东西。
        yAxis.addLimitLine(ll); // 添加限制线到轴上
        yAxis.removeLimitLine(ll);  // 移除指定的限制线,还有其他的几个移除方法
        yAxis.setDrawLimitLinesBehindData(false); // 限制线在数据之后绘制。默认为false

        // X轴更多属性
        xAxis.setLabelRotationAngle(45);   // 标签倾斜
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // X轴绘制位置，默认是顶部

        yAxis.setDrawZeroLine(false);    // 绘制值为0的轴，默认false,其实比较有用的就是在柱形图，当有负数时，显示在0轴以下，其他的图这个可能会看到一些奇葩的效果
        yAxis.setZeroLineWidth(10);  // 0轴宽度
        yAxis.setZeroLineColor(Color.BLUE);   // 0轴颜色

        // 轴值转换显示
        xAxis.setValueFormatter(new ValueFormatter() { // 与上面值转换一样，这里就是转换出轴上label的显示。也有几个默认的，不多说了。
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "℃";
            }
        });


        // *********************图例***************************** //
        Legend legend = lineChart.getLegend(); // 获取图例，但是在数据设置给chart之前是不可获取的
        legend.setEnabled(true);    // 是否绘制图例
        legend.setTextColor(Color.GRAY);    // 图例标签字体颜色，默认BLACK
        legend.setTextSize(12); // 图例标签字体大小[6,24]dp,默认10dp
        legend.setTypeface(null);   // 图例标签字体
        legend.setWordWrapEnabled(false);    // 当图例超出时是否换行适配，这个配置会降低性能，且只有图例在底部时才可以适配。默认false
        legend.setMaxSizePercent(1f); // 设置，默认0.95f,图例最大尺寸区域占图表区域之外的比例
        legend.setForm(Legend.LegendForm.CIRCLE);   // 设置图例的形状，SQUARE, CIRCLE 或者 LINE
        legend.setFormSize(8); // 图例图形尺寸，dp，默认8dp
        legend.setXEntrySpace(6);  // 设置水平图例间间距，默认6dp
        legend.setYEntrySpace(0);  // 设置垂直图例间间距，默认0
        legend.setFormToTextSpace(5);    // 设置图例的标签与图形之间的距离，默认5dp
        legend.setWordWrapEnabled(true);   // 图标单词是否适配。只有在底部才会有效，
        legend.setCustom(new LegendEntry[]{new LegendEntry("label1", Legend.LegendForm.CIRCLE, 10, 5, null, Color.RED),
                new LegendEntry("label2", Legend.LegendForm.CIRCLE, 10, 5, null, Color.GRAY),
                new LegendEntry("label3", Legend.LegendForm.CIRCLE, 10, 5, null, Color.RED)}); // 这个应该是之前的setCustom(int[] colors, String[] labels)方法
        // 这个方法会把前面设置的图例都去掉，重置为指定的图例。
        legend.resetCustom();   // 去掉上面方法设置的图例，然后之前dataSet中设置的会重新显示。
//        legend.setExtra(new int[]{Color.RED, Color.GRAY, Color.GREEN}, new String[]{"label1", "label2", "label3"}); // 添加图例，颜色与label数量要一致。
        // 如果前面已经在dataSet中设置了颜色，那么之前的图例就存在，这个只是添加在后面的图例，并不一定有对应数据。


        lineChart.invalidate();    // 重绘
        // ********************其他******************************* //
        lineChart.setLogEnabled(false);    // 是否打印日志，默认false
//        lineChart.notifyDataSetChanged();  // 通知有值变化，重绘，一般动态添加数据时用到

        // ******************指定缩放显示范围************************* //
        // 这里要说一下，下面并不是指定其初始显示的范围，所以，很可能大家觉得没有效果。其实这几个方法目的是限制缩放时的可见范围最值。
//        lineChart.setVisibleXRangeMaximum(6); // X轴缩小可见最大范围，这里测试有点问题，范围不是指定的，可以缩小到更多范围。
//        lineChart.setVisibleXRangeMinimum(4);  // X轴放大最低可见范围，最小意思是，再怎么放大范围也至少要有4，但是一开始显示的时候范围可能很大。
//        lineChart.setVisibleYRangeMaximum(4, YAxis.AxisDependency.LEFT);   // Y缩小时可见最大范围，后面是其适用的轴。测试发现两边轴都是有效的
//        lineChart.setVisibleYRangeMinimum(2, YAxis.AxisDependency.LEFT);   // Y轴放大时可见最小范围。
//        lineChart.setVisibleYRange(3, 5, YAxis.AxisDependency.LEFT);   // y轴缩放时可见最小和最大范围。但是测试发现不能放大3的范围，但是也是符合这个限制的
//        lineChart.setVisibleXRange(3, 6);  // X轴缩放时可见最小和最大范围。测试也有点问题
//        lineChart.setViewPortOffsets(10, 0, 10, 0);    // 图表绘制区的偏移量设置,这个会忽略MP的自动计算偏移。
        // 比如，自动时，图例与绘制区是分开的，但是自己写就可能重和在一起。慎用
//        lineChart.resetViewPortOffsets();  // 重置上面的偏移量设置。
//        lineChart.setExtraOffsets(10, 0, 10, 0);   // 这个与上面的区别是不会忽略其自己计算的偏移。

        // **************************移动******************************** //
//        lineChart.fitScreen(); // 重置所有缩放与拖动，使图标完全符合其边界
//        lineChart.moveViewToX(30); // 想指定向偏移，比如原本显示前三个点，现在显示后三个，如果没有缩放其实看不出啥效果

//        lineChart.moveViewTo(30, 10, YAxis.AxisDependency.LEFT);    // 向指定方向偏移,如果没有缩放其实看不出啥效果,后面的轴没啥效果
//        lineChart.moveViewToAnimated(30, 10, YAxis.AxisDependency.LEFT, 2000); // 同上面那个，但是有动画效果
//        lineChart.centerViewTo(30, 10, YAxis.AxisDependency.LEFT); // 将视图中心移动到指定位置，也是要缩放才有效果
//        lineChart.centerViewToAnimated(30, 10, YAxis.AxisDependency.LEFT, 2000); // 同上面那个，但是有动画效果


        // ****************************自动缩放********************************** //
        // 这里的缩放效果会收到setVisibleXRangeMaximum等范围影响，
//        lineChart.zoomIn();    // 自动放大1.4倍，没看出效果
//        lineChart.zoomOut();   // 自动缩小0.7倍，没看出效果
//        lineChart.zoom(2f, 2f, 2, 3, YAxis.AxisDependency.LEFT);
//        lineChart.zoomAndCenterAnimated(1.4f, 1.4f, 2, 3, YAxis.AxisDependency.LEFT, 3000);    // 缩放，有动画，报了个空指针。。。


        // ************************动画************************************** //
        /*lineChart.animateX(3000);  // 数据从左到右动画依次显示
        lineChart.animateY(3000);  // 数据从下到上动画依次显示*/
//        lineChart.animateXY(3000, 3000);   // 上面两个的结合
//        lineChart.animateX(3000, Easing.EasingOption.EaseInQuad); // 动画播放随时间变化的速率，有点像插值器。后面这个有的不能用


        // **************************所有数据样式************************************ //
//        lineChart.setMarker(new ChartMarkerView(this, R.layout.item_chart_indicator, "温度:", "℃"));    // 点击数据点显示的pop，有俩默认的，MarkerImage：一张图片，MarkerView:一个layout布局,也可以自己定义.这里这个是我自定义的一个MarkerView。


        // **************************图表本身特殊样式******************************** //
        lineChart.setAutoScaleMinMaxEnabled(false);    // y轴是否自动缩放；当缩放时，y轴的显示会自动根据x轴范围内数据的最大最小值而调整。财务报表比较有用，默认false
        lineChart.setKeepPositionOnRotation(false); // 设置当屏幕方向变化时，是否保留之前的缩放与滚动位置，默认：false


        // *************************其他********************************* //
        // 上面介绍了MP的大部分常用的api，基本可以满足绝大部分的需求，还有部分不常用的暂时不说了，以后用的着再下面补充
//        lineChart.clear(); // 清空
    }

    private OnChartGestureListener getGestureListener() {
        return new OnChartGestureListener() { // 手势监听器
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                // 按下
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                // 抬起,取消
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                // 长按
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                // 双击
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                // 单击
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                // 甩动
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                // 缩放
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                // 移动
            }
        };
    }

    private void initLineDataSet(List<Entry> entries) {
        dataSet = new LineDataSet(entries, "体重变化线");
        dataSet.setHighlightEnabled(true);  // 能否高亮,默认true
        dataSet.setDrawHighlightIndicators(true);   // 画高亮指示器,默认true
        dataSet.setDrawHorizontalHighlightIndicator(true);  // 画水平高亮指示器，默认true
        dataSet.setDrawVerticalHighlightIndicator(true);    // 垂直方向高亮指示器,默认true
        dataSet.setHighLightColor(Color.BLACK); // 高亮颜色,默认RGB(255, 187, 115)
        // Y轴更多属性
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);  // 设置dataSet应绘制在Y轴的左轴还是右轴，默认LEFT
        dataSet.setColors(Color.RED);
    }

    private void initLineData() {
        LineData lineData = new LineData(dataSet);
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLACK);
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        lineData.setValueTextColors(colors); // 字体添加颜色，按顺序给数据上色，不足则重复使用,也可以在单个dataSet上添加
        lineData.setValueTextSize(12);   // 文字大小
        lineData.setValueTypeface(null); // 文字字体
        lineData.setValueFormatter(new ValueFormatter() {  // 所有数据显示的数据值
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value + "￥";
            }
        });
        lineData.setDrawValues(true);   // 绘制每个点的值
        // 上面这些都是data集合中的相关属性，也可以针对每个dataSet来设置
        lineData.setValueTextColor(Color.RED);   // 该条线的
        lineChart.setData(lineData);
    }
}
