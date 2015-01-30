package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.philjay.valuebar.ValueBar;
import com.philjay.valuebar.ValueBarSelectionListener;
import com.philjay.valuebar.colors.RedToGreenFormatter;

import java.util.ArrayList;
import java.util.SortedSet;

/**
 * Created by Mike Antipiev on 03.12.14.
 *
 * CitiesFragment class draws Cities chart
 *
 */


public class CitiesFragment extends Fragment {

    private SortedSet<City> Cities;
    private BarChart mChart;

//    Interaction with MainActivity
    private OnCitiesInteractionListener mListener;

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();
        return fragment;
    }

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cities, container, false);

        MyApplication myApp = (MyApplication) getActivity().getApplication();
        if (myApp.isLoaded()) {
            Cities = myApp.getCities();
            mChart = (BarChart) v.findViewById(R.id.chartCities);
            //FIXME:  Bar Shadow is not yet supported
            mChart.setDrawBarShadow(false);

            //mChart.setOnChartValueSelectedListener(this);

            // enable the drawing of values
            mChart.setDrawYValues(true);

            mChart.setDrawValueAboveBar(true);

            mChart.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChart.setMaxVisibleValueCount(60);

            // disable 3D
            mChart.set3DEnabled(false);

            // scaling can now only be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            // draw shadows for each bar that show the maximum value
            // mChart.setDrawBarShadow(true);

            //mChart.setUnit(" €");

            // mChart.setDrawXLabels(false);

            mChart.setDrawGridBackground(false);
 //           mChart.setDrawHorizontalGrid(true);
 //           mChart.setDrawVerticalGrid(false);
            // mChart.setDrawYLabels(false);

            // sets the text size of the values inside the chart
            mChart.setValueTextSize(10f);

            mChart.setDrawBorder(false);
            // mChart.setBorderPositions(new BorderPosition[] {BorderPosition.LEFT,
            // BorderPosition.RIGHT});

            //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

            /*XLabels xl = mChart.getXLabels();
            xl.setPosition(XLabels.XLabelPosition.BOTTOM);
            xl.setCenterXLabelText(true);
            //xl.setTypeface(tf);

            YLabels yl = mChart.getYLabels();
            //yl.setTypeface(tf);
            yl.setLabelCount(8);
            yl.setPosition(YLabels.YLabelPosition.BOTH_SIDED);
*/
            //mChart.setValueTypeface(tf);

            // setting data

            // change the color of the center-hole
            //mChart.setHoleColor(Color.rgb(235, 235, 235));

            //LayoutInflater vb_inflater = (LayoutInflater) v.getContext()
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

            int i=0;
            for (City city : Cities) {
                xVals.add(city.getName() + "(" + city.getCountUsers() + ")");
                yVals.add(new BarEntry(city.getCountUsers(), i));
                i++;
            }

            BarDataSet set1 = new BarDataSet(yVals, "DataSet");
            //set1.setBarSpacePercent(35f);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);

            mChart.setData(data);
            //mChart.setMinimumWidth(20);


            /*i=1;
            for (City city : Cities) {
                LinearLayout ltCitiesTexts = (LinearLayout) v.findViewById(R.id.ltCitiesTexts);
                LinearLayout ltCitiesBars = (LinearLayout) v.findViewById(R.id.ltCitiesBars);

                View convertView = inflater.inflate(R.layout.tv_cities, null);
                //TextView tvCitiesText = new TextView(ltCitiesTexts.getContext());
                TextView tvCitiesText = (TextView) convertView.findViewById(R.id.tvCitiesTexts);
                tvCitiesText.setText(city.getName());

                //View convertView = new View(v.getContext());
                convertView = inflater.inflate(R.layout.vb_cities, null);

                //ValueBar vbCitiesBars = new ValueBar(ltCitiesBars.getContext());
                //ValueBar vbCitiesBars = (ValueBar) convertView.findViewById(R.id.vbCitiesBars);
                //vbCitiesBars.setColor(10*i);i++;
                //vbCitiesBars.set;

                ProgressBar pbCities = (ProgressBar) convertView.findViewById(R.id.pbCities);
                pbCities.setProgress(city.getCountUsers());

                ltCitiesTexts.addView(tvCitiesText);
                ltCitiesBars.addView(pbCities);
                //ltCitiesBars.addView(vbCitiesBars);
            }
            //setup();*/

        }
        return (View) v;
    }

    private void setup() {

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

   /*     ValueBar bar = new ValueBar(getActivity().getApplicationContext());

            bar.setMinMax(0, 1000);
            bar.animate(0, 900, 1500);
            bar.setInterval(1f);
            bar.setDrawBorder(false);
            bar.setValueBarSelectionListener(this);
            bar.setValueTextSize(14f);
            bar.setMinMaxTextSize(16f);
            //bar.setValueTextTypeface(tf);
            //bar.setMinMaxTextTypeface(tf);
            //bar.setValueTextFormatter(new MyCustomValueTextFormatter());
            bar.setColorFormatter(new RedToGreenFormatter());
            bar.setOverlayColor(Color.BLACK);
            //bar.setDrawBorder(false);
            bar.setDrawMinMaxText(false);
            bar.setTouchEnabled(false);
            //bar.setDrawValueText(false);
            //bar.setColor(Color.BLUE);*/

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCitiesInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCitiesInteractionListener {
        public void onCitiesInteraction();
    }

}
