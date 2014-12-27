package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesFragment extends Fragment implements ValueBarSelectionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private PieChart mChart;
    private SortedSet<City> Cities;
    private ValueBar[] mValueBars = new ValueBar[5];



    private OnCitiesInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitiesFragment newInstance() {
        //    public static CitiesFragment newInstance(String param1, String param2) {
        CitiesFragment fragment = new CitiesFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*     if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cities, container, false);

        MyApplication myApp = (MyApplication) getActivity().getApplication();
        if (myApp.isLoaded()) {

            mValueBars[0] = (ValueBar) v.findViewById(R.id.valueBar1);
            mValueBars[1] = (ValueBar) v.findViewById(R.id.valueBar2);
            mValueBars[2] = (ValueBar) v.findViewById(R.id.valueBar3);
            mValueBars[3] = (ValueBar) v.findViewById(R.id.valueBar4);
            mValueBars[4] = (ValueBar) v.findViewById(R.id.valueBar5);

            setup();


/*            Cities = myApp.getCities();
            mChart = (PieChart) v.findViewById(R.id.chart1);

            // change the color of the center-hole
            mChart.setHoleColor(Color.rgb(235, 235, 235));

            //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

            //mChart.setValueTypeface(tf);
            //mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));

            mChart.setHoleRadius(60f);

            mChart.setDescription("");

            mChart.setDrawYValues(true);
            mChart.setDrawCenterText(true);

            mChart.setDrawHoleEnabled(true);

            mChart.setRotationAngle(0);

            // draws the corresponding description value into the slice
            mChart.setDrawXValues(true);

            // enable rotation of the chart by touch
            mChart.setRotationEnabled(true);

            // display percentage values
            mChart.setUsePercentValues(true);
            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
            //mChart.setOnChartValueSelectedListener(this);
            // mChart.setTouchEnabled(false);

            mChart.setCenterText("MPAndroidChart\nLibrary");

            setData(3, 100);

            mChart.animateXY(1500, 1500);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(5f);
*/
        }
        return v;
    }

    private void setup() {

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        for (ValueBar bar : mValueBars) {

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
            bar.setValue(800f);
            bar.setValue(300f);
            // bar.setColor(Color.BLUE);
        }
    }

    private void animateUp() {

        for (ValueBar bar : mValueBars)
            bar.animateUp(800, 1500);
    }

    private void animateDown() {

        for (ValueBar bar : mValueBars)
            bar.animateDown(0, 1500);
    }

    private void toggleMinMaxLabel() {

        for (ValueBar bar : mValueBars) {
            bar.setDrawMinMaxText(bar.isDrawMinMaxTextEnabled() ? false : true);
            bar.invalidate();
        }
    }

    private void toggleValueLabel() {

        for (ValueBar bar : mValueBars) {
            bar.setDrawValueText(bar.isDrawValueTextEnabled() ? false : true);
            bar.invalidate();
        }
    }

    @Override
    public void onSelectionUpdate(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selection update: " + val);
    }

    @Override
    public void onValueSelected(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selected: " + val);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            //xVals.add(mParties[i % mParties.length]);
            xVals.add("test");

        PieDataSet set1 = new PieDataSet(yVals1, "Election Results");
        set1.setSliceSpace(3f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
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
    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }*/

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCitiesInteractionListener {
        // TODO: Update argument type and name
        public void onCitiesInteraction();
    }

}
