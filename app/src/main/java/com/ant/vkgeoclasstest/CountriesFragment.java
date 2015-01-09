package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.philjay.valuebar.ValueBar;
import com.philjay.valuebar.ValueBarSelectionListener;
import com.philjay.valuebar.colors.RedToGreenFormatter;

import java.util.ArrayList;
import java.util.SortedSet;

/**
 * Created by apple on 27.12.14.
 */
public class CountriesFragment extends Fragment  {

    private PieChart mChart;
    private SortedSet<Country> Countries;

    private OnCountriesInteractionListener mListener;

    public static CountriesFragment newInstance() {
        CountriesFragment fragment = new CountriesFragment();
        return fragment;
    }

    public CountriesFragment() {
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

        View v = inflater.inflate(R.layout.fragment_countries, container, false);

        MyApplication myApp = (MyApplication) getActivity().getApplication();
        if (myApp.isLoaded()) {

            Countries = myApp.getCountries();
            mChart = (PieChart) v.findViewById(R.id.chart1);

            // change the color of the center-hole
            mChart.setHoleColor(Color.rgb(235, 235, 235));

            //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

            //mChart.setValueTypeface(tf);
            //mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));

            mChart.setHoleRadius(30f);
            mChart.setDescription(" ");
            mChart.setDrawYValues(false);
            mChart.setDrawCenterText(true);
            mChart.setDrawHoleEnabled(true);
            mChart.setRotationAngle(0);

            // draws the corresponding description value into the slice
            mChart.setDrawXValues(false);

            // enable rotation of the chart by touch
            mChart.setRotationEnabled(false);

            // display percentage values
            mChart.setUsePercentValues(true);
            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
            //mChart.setOnChartValueSelectedListener(this);
            // mChart.setTouchEnabled(false);

            mChart.setCenterText("% друзей\nв странах");

            setData(3, 100);

            mChart.animateXY(1500, 1500);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(5f);

        }
        return v;
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        //for (int i = 0; i < count + 1; i++) {
        //    yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        //}

        ArrayList<String> xVals = new ArrayList<String>();

        int i=0;
        for (Country country : Countries) {
            xVals.add(country.getName()+"("+country.getCountUsers()+")");
            yVals1.add(new Entry( (float) country.getCountUsers(), i));
            i++;
        }
        //for (int i = 0; i < count + 1; i++)
            //xVals.add(mParties[i % mParties.length]);
        //    xVals.add("test");

        PieDataSet set1 = new PieDataSet(yVals1, "Страны");
        set1.setSliceSpace(3f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCountriesInteractionListener) activity;
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

    public interface OnCountriesInteractionListener {
        public void onCountriesInteraction();
    }

}


