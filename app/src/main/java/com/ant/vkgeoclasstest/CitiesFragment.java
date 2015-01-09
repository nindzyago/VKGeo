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

import com.github.mikephil.charting.charts.PieChart;
import com.philjay.valuebar.ValueBar;
import com.philjay.valuebar.ValueBarSelectionListener;
import com.philjay.valuebar.colors.RedToGreenFormatter;

import java.util.ArrayList;
import java.util.SortedSet;


public class CitiesFragment extends Fragment implements ValueBarSelectionListener {

    private ValueBar valueBar;
    private SortedSet<City> Cities;

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
            //LayoutInflater vb_inflater = (LayoutInflater) v.getContext()
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int i=1;
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
            //setup();

        }
        return (View) v;
    }

    private void setup() {

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        ValueBar bar = new ValueBar(getActivity().getApplicationContext());

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
            //bar.setColor(Color.BLUE);

    }

    @Override
    public void onSelectionUpdate(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selection update: " + val);
    }

    @Override
    public void onValueSelected(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selected: " + val);
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
