package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Mike Antipiev on 03.12.14.
 *
 * MapFragment class shows Google Map, and cities on it
 *
 */

public class MapFragment extends Fragment {


    private SupportMapFragment mapFragment;
    private GoogleMap map;
    MyApplication myApp;

    // Marker to City binding
    private HashMap<Marker, City> cityMarker = new HashMap<Marker, City>();

    private SortedSet<City> Cities;
    private User Profile;

    private AsyncFindCities asyncFindCities;
    private ProgressBar progressCities;

    ThreadControl tControl = new ThreadControl();

    private OnMapInteractionListener mListener;
    private OnCitySelectionListener csListener;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Getting Tag name of a Fragment by position
    private String getFragmentTag(int pos){
        return "android:switcher:"+R.id.pager+":"+pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        myApp = (MyApplication) getActivity().getApplication();
        if (myApp.isLoaded()) {
            Cities = myApp.getCities();
            Profile = myApp.getProfile();
            // Show map and progressBar
            progressCities = (ProgressBar) v.findViewById(R.id.progressCities);
            mapFragment = (SupportMapFragment)
                    getParentFragment().getChildFragmentManager()
                    .findFragmentByTag("fragment1")
                    .getChildFragmentManager()
                    .findFragmentById(R.id.map);
            map = mapFragment.getMap();
            if (map == null) {
                getActivity().finish();
            }

            //if (!myApp.isFounded()) {
                // Start async task to show cities
                asyncFindCities = new AsyncFindCities();
                asyncFindCities.execute();
            //} else {
                //tControl.resume();
            //}

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
                @Override
                public void onInfoWindowClick(Marker marker){
                    //TODO: Remove this
                    //Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    //intent.putExtra("cityId", cityMarker.get(marker));
                    //intent.putExtra("userId", Profile.getId());
                    //startActivity(intent);

                    // Select Friends Tab in MainActivity
                    mListener.onMapInteraction();

                    // Call ProfileFragment listener to show selected city
                    // TODO: try / catch this section
                    csListener = (OnCitySelectionListener) getActivity()
                            .getSupportFragmentManager().findFragmentByTag(getFragmentTag(0))
                            .getChildFragmentManager()
                            .findFragmentByTag("fragment0");

                    csListener.onCitySelection(cityMarker.get(marker));

                }
            });

        } else
        {}

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMapInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //tControl.pause();
        mListener = null;
        csListener = null;
        if (asyncFindCities != null) {
            asyncFindCities.cancel(true);
        }
    }


    public LatLng getLocationFromAddress(String strAddress){
        // Find location of city with GeoCoder

        Geocoder coder = new Geocoder(getActivity().getApplicationContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // Make search request
            address = coder.getFromLocationName(strAddress,5);
            p1 = new LatLng(0,0);

            // Return 0,0 when not founded
            if (address == null) {
                return p1;
            }

            if (!address.isEmpty()) {
                // Get first founded address
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }

        }
        catch (IOException e) {
            // In case of exception return 30,30 location
            p1 = new LatLng(30,30);
            return p1;
        }

        return p1;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    class AsyncFindCities extends AsyncTask<Void, Map<Integer, City>, Void> {


        // Async task to put markers on map
        @Override
        protected void onPreExecute() {
            progressCities.setMax(Cities.size());
            progressCities.setVisibility(View.VISIBLE);
            myApp.setFounded(true);
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                int i=0;
                for (City city : Cities) {
                    if (city.getCoords()==null) {
                        city.setCoords(getLocationFromAddress(city.getName()));
                    }
                    Map<Integer, City> currentCity = new HashMap<Integer, City>();
                    currentCity.put(i++, city);
                    // Show founded city in progress
                    publishProgress(currentCity);
                }
                /*while (true) {
                    //Pause work if control is paused.
                    tControl.waitIfPaused();
                    //Stop work if control is cancelled.
                    if (tControl.isCancelled()) {
                        break;
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Map<Integer, City>... values) {
            super.onProgressUpdate(values);
            // Extract city information from values
            for (Map.Entry entry : values[0].entrySet()) {
                int pos = (int) entry.getKey();
                City city = (City) entry.getValue();
                Marker marker;
                // If city equals Users's city draw a home marker
                if (city.equals(Profile.getCity())) {
                    marker = map.addMarker(new MarkerOptions()
                            .position(city.getCoords())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet(getString(R.string.info_marker))
                            .title(city.getName() + " ("+city.getCountUsers() + ")"));
                } else {
                    // Else draw a regular marker

                    // Resize marker
                    // Resize
                    int resMarker=0;

                    if (isBetween(city.getCountUsers(), 0, 2)) {  resMarker = R.drawable.knob_red_24; } else
                    if (isBetween(city.getCountUsers(), 3, 5)) { resMarker = R.drawable.knob_red_32; } else
                    if (isBetween(city.getCountUsers(), 6, 10)) { resMarker = R.drawable.knob_red_32; } else
                    if (isBetween(city.getCountUsers(), 11, 20)) { resMarker = R.drawable.knob_red_48; } else
                    if (city.getCountUsers() > 20) { resMarker = R.drawable.knob_red_64; }

                    Bitmap b = BitmapFactory.decodeResource(getResources(), resMarker);
                    Bitmap bhalfsize = Bitmap.createScaledBitmap(b, b.getWidth() / 2, b.getHeight() / 2, false);
                    marker = map.addMarker(new MarkerOptions()
                            .position(city.getCoords())
                            .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize))
                                    // .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet(getString(R.string.info_marker))

                            .title(city.getName() + " (" + city.getCountUsers() + ")"));
                }
                // Bind marker to city
                cityMarker.put(marker, city);
                progressCities.setProgress(pos);
            }

        }
        @Override
        protected void onPostExecute(Void result) {
            progressCities.setVisibility(View.GONE);
        }

    }

    public interface OnMapInteractionListener {
        public void onMapInteraction();
    }

    public interface OnCitySelectionListener {
        public void onCitySelection(City city);
    }

}
