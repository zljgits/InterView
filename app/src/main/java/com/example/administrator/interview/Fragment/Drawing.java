package com.example.administrator.interview.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.ColoursXYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import com.example.administrator.interview.R;
/**
 * Created by Administrator on 2018/3/8.
 */

public class Drawing extends Fragment {
    private ViewPager ViewPager;
    private LinearLayout viewGroup;
    private TextView textView;
    private TextView[] textViews;
    private DrawingAdapter adapter;
    private ArrayList<DrawingBean> arrayList;
    private DrawingBean bean1, bean2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewPager = (android.support.v4.view.ViewPager) getView().findViewById(R.id.ViewPager);
        viewGroup = (LinearLayout) getView().findViewById(R.id.viewGroup);
        adapter = new DrawingAdapter();
        arrayList = new ArrayList<>();
        bean1 = new DrawingBean("pm2.5指数", "pm2.5");
        bean2 = new DrawingBean("温度指数", "温度");

        arrayList.add(bean1);
        arrayList.add(bean2);

        String name[] = {"pm2.5", "温度"};
        textViews = new TextView[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            textView = new TextView(getActivity());
            textView.setPadding(10, 0, 10, 0);
            textViews[i] = textView;
            if (i == 0) {
                textViews[i].setText(name[i]);
                textViews[i].setBackgroundColor(Color.RED);
            } else {
                textViews[i].setText(name[i]);
                textViews[i].setBackgroundColor(Color.GREEN);
            }
            viewGroup.addView(textViews[i]);
        }
        ViewPager.setAdapter(adapter);
        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < textViews.length; i++) {
                    textViews[position].setBackgroundColor(Color.RED);
                    if (position != i) {
                        textViews[i].setBackgroundColor(Color.GREEN);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        runnable.run();

    }

    public class DrawingBean {
        private String title;
        private String tablename;

        public DrawingBean(String title, String tablename) {
            this.title = title;
            this.tablename = tablename;
        }

        LinkedList<Integer> linkedList = new LinkedList<>();
    }

    public class DrawingAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.drawing_adapter, null);
            DrawingBean bean = arrayList.get(position);
            XYMultipleSeriesRenderer xyMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
            ColoursXYSeriesRenderer coloursXYSeriesRenderer = new ColoursXYSeriesRenderer();
            xyMultipleSeriesRenderer.setPointSize(10);
            xyMultipleSeriesRenderer.setLabelsTextSize(20);
            xyMultipleSeriesRenderer.setZoomButtonsVisible(true);
            xyMultipleSeriesRenderer.setXLabels(6);
            xyMultipleSeriesRenderer.setYLabels(6);
            xyMultipleSeriesRenderer.setXAxisMin(0);
            xyMultipleSeriesRenderer.setXAxisMax(6);
            xyMultipleSeriesRenderer.setYAxisMin(0);

            coloursXYSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
            coloursXYSeriesRenderer.setChartValuesTextSize(20);
            coloursXYSeriesRenderer.setChartValuesSpacing(20);
            coloursXYSeriesRenderer.setDisplayChartValues(true);
            coloursXYSeriesRenderer.setFillPoints(true);
            coloursXYSeriesRenderer.setColor(Color.BLACK);
            //pm2.5
            if (position == 0) {
                xyMultipleSeriesRenderer.setYAxisMax(300);
                coloursXYSeriesRenderer.setWarningMaxValue(200);
                coloursXYSeriesRenderer.setChartValueTextColor(Color.GREEN);
                coloursXYSeriesRenderer.setUseColor(true);
                //湿度
            } else if (position == 1) {
                xyMultipleSeriesRenderer.setYAxisMax(60);
                coloursXYSeriesRenderer.setWarningMaxValue(30);
                coloursXYSeriesRenderer.setChartValueTextColor(Color.GREEN);
                coloursXYSeriesRenderer.setUseColor(true);
                //光强
            }
            xyMultipleSeriesRenderer.addSeriesRenderer(coloursXYSeriesRenderer);
            XYSeries xySeries = new XYSeries(bean.tablename);
            for (int i = 1; i < bean.linkedList.size(); i++) {
                xySeries.add(i, bean.linkedList.get(i - 1));
            }
            XYMultipleSeriesDataset xyMultipleSeriesDataset = new XYMultipleSeriesDataset();
            xyMultipleSeriesDataset.addSeries(xySeries);
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.FrameLayout);
            TextView title = (TextView) view.findViewById(R.id.ViewPagerTitle);
            title.setText(bean.title);
            GraphicalView graphicalView = ChartFactory.getLineChartView(getActivity(), xyMultipleSeriesDataset, xyMultipleSeriesRenderer);
            xyMultipleSeriesRenderer.setMarginsColor(Color.TRANSPARENT);
            frameLayout.addView(graphicalView);
            graphicalView.repaint();
            container.addView(view);
            return view;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(s);
                int pm255 = jsonObject.getInt("pm2.5");
                int humidityy = jsonObject.getInt("wd");//湿度
                bean1.linkedList.add(pm255);
                bean2.linkedList.add(humidityy);
                if (bean1.linkedList.size() > 7) {
                    bean1.linkedList.pollFirst();
                }
                if (bean2.linkedList.size() > 7) {
                    bean2.linkedList.pollFirst();
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    public void startThread() {
        String URL1 = "http://"+ IP.IP+":8080/AndroidServer/drawing";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("hj","hj");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpThread http = new HttpThread(URL1, jsonObject.toString(), handler);
        http.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startThread();
            handler.postDelayed(runnable, 3000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
