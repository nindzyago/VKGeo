package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.content.Intent;
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
 * MapFragment class holds Google Map, and shows cities on it
 *
 */


public class MapFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private HashMap<Marker, Integer> cityMarker = new HashMap<Marker, Integer>();

    private SortedSet<City> Cities;
    private User Profile;

    private AsyncFindCities asyncFindCities;
    private ProgressBar progressCities;


    private OnMapInteractionListener mListener;

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
        MyApplication myApp = (MyApplication) getActivity().getApplication();
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

            // Start async task to show cities
            asyncFindCities = new AsyncFindCities();
            asyncFindCities.execute();

/*            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
                @Override
                public void onInfoWindowClick(Marker marker){
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.putExtra("cityId", cityMarker.get(marker));
                    intent.putExtra("userId", Profile.getId());
                    startActivity(intent);

                }
            });*/

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
        mListener = null;
        asyncFindCities.cancel(true);
    }

    public interface OnMapInteractionListener {
        public void onMapInteraction();
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


    class AsyncFindCities extends AsyncTask<Void, Map<Integer, City>, Void> {

        // Async task to put markers on map
        @Override
        protected void onPreExecute() {
            // Get global array Cities and set progressbar properties
            progressCities.setMax(Cities.size());
            progressCities.setVisibility(View.VISIBLE);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Map<Integer, City>... values) {
            super.onProgressUpdate(values);
            for (Map.Entry entry : values[0].entrySet()) {
                int pos = (int) entry.getKey();
                City city = (City) entry.getValue();

                /*if (city.equals(userCity)) {
                    map.addMarker(new MarkerOptions()
                            .position(pos)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet("показать друзей")
                            .title(city));
                } else {*/
                /*
                Marker marker = map.addMarker(new MarkerOptions()
                            .position(city.getCoords())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.knobred))
                                    // .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet(getString(R.string.info_marker))
                            .title(city.getName() + " ("+city.getCountUsers() + ")"));


                //map.addMarker(marker);
                cityMarker.put(marker, city.getId());
                */
                progressCities.setProgress(pos);
            }

        }
        @Override
        protected void onPostExecute(Void result) {
            progressCities.setVisibility(View.GONE);
        }

    }

}
