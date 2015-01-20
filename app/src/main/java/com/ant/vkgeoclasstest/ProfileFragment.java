package com.ant.vkgeoclasstest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;


public class ProfileFragment extends Fragment  {

    private static final String ARG_PARAM1 = "profile";
    private static final String ARG_PARAM2 = "users";

    private ArrayList<User> Users;
    private SortedSet<City> Cities;
    private SortedSet<Country> Countries;
    private User Profile;
    ProgressDialog progress;
    ProgressBar progressBar;

    ExpandableListView lvUsers;
    SimpleExpandableListAdapter lvUsersAdapter;
    ImageTextExpandableListAdapter lvAdapter;
    View headerUsers;

    private ArrayList<Map<String,String>> groupcities = new ArrayList<Map<String,String>>();
    private ArrayList<Map<String,String>> groupfriends  = new ArrayList<Map<String,String>>();
    private ArrayList<ArrayList<Map<String,String>>> groupped  = new ArrayList<ArrayList<Map<String,String>>>();
    private HashMap m;

    //private String mParam1;
    //private String mParam2;

    TextView tvOut;

    private OnProfileInteractionListener mListener;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        final MyApplication myApp = (MyApplication) this.getActivity().getApplication();
        if (myApp.isLoaded()) {
            //progressBar.setVisibility(View.GONE);
            prepareAdapter();
            prepareHeader();
            Profile = myApp.getProfile();
            if (Profile.getPhoto()!="") {
                Picasso.with(getActivity().getApplicationContext()).load(Profile.getPhoto())
                        .transform(new CircleTransform())
                        .into((ImageView) v.findViewById(R.id.ivProfile));
            } else {
                String url = "http://vkontakte.ru/images/camera_b.gif";
                Picasso.with(getActivity().getApplicationContext()).load(url)
                        .transform(new CircleTransform())
                        .into((ImageView) v.findViewById(R.id.ivProfile));
            }


            ((TextView) v.findViewById(R.id.tvProfileUser)).setText(Profile.getName());
            ((TextView) v.findViewById(R.id.tvProfileCity)).setText(Profile.getCity().getName()
                    + ", " + Profile.getCountry().getName());

            String str = "" + Users.size();
            ((TextView) v.findViewById(R.id.tvProfileUsers)).setText("" + Users.size());
            ((TextView) v.findViewById(R.id.tvProfileCities)).setText("" + Cities.size());
            ((TextView) v.findViewById(R.id.tvProfileCountries)).setText("" + Countries.size());

            lvUsers = (ExpandableListView) v.findViewById(R.id.lvUsers);
            //lvUsers.addHeaderView(headerUsers,"tmp", false);
            lvUsers.setAdapter(lvAdapter);

            lvUsers.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    Map<String, String> child = (Map<String, String>) lvAdapter.getChild(groupPosition, childPosition);
                    String ids = "";
                    for (String key : child.keySet()) {
                        ids = key;
                    }
                    int userId = 0;
                    for (User user : Users) {
                        if (user.getName().equals(ids)) {
                            userId=user.getId();
                        }
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("userId", userId);
                    myApp.setLoaded(false);
                    getActivity().startActivity(intent);
                    return true;
                }
            });

        } else {
            //progressBar.setVisibility(View.VISIBLE);
        }


        //Activity act = (MainActivity) getActivity();
        //if (act .isProfileLoaded()) {}
        return v;
    }

    private void prepareHeader() {
        //MyApplication myApp = (MyApplication) getActivity().getApplication();
        //headerUsers = getActivity().getLayoutInflater().inflate(R.layout.header_users, null);
        }

    private void prepareAdapter() {

        MyApplication myApp = (MyApplication) getActivity().getApplication();
        Users = myApp.getUsers();
        Cities = myApp.getCities();
        Countries = myApp.getCountries();

        ArrayList<Map<String,String>>  mListDataHeader = new ArrayList<Map<String,String>>();
        Map<String, ArrayList<Map<String,String>>> mListDataChild = new HashMap<String, ArrayList<Map<String,String>>> ();
        ArrayList<Map<String,String>> tempDataChild = new ArrayList<Map<String,String>>();


        for (City city : Cities) {
            groupfriends = new ArrayList<Map<String,String>>();
            tempDataChild = new ArrayList<Map<String,String>>();
            for (User user : Users) {
                if (user.getCity() != null) {
                    if (user.getCity().equals(city)) {
                        m = new HashMap<String, String>();
                        m.put("user", user.getName());
                        groupfriends.add(m);
                        m = new HashMap<String, String>();
                        m.put(user.getName(), user.getPhoto());
                        tempDataChild.add(m);
                    }
                }
            }
            m = new HashMap<String,String>();
            String cityName = city.getName() + " (" + city.getCountUsers() + ")";
            m.put("city", cityName);
            groupcities.add(m);
            groupped.add(groupfriends);

            m = new HashMap<String,String>();
            m.put(cityName, null);
            mListDataHeader.add(m);

            mListDataChild.put(cityName, tempDataChild);

        }

        lvAdapter = new ImageTextExpandableListAdapter(
                getActivity().getApplicationContext(),
                mListDataHeader, R.layout.users_group,
                mListDataChild, R.layout.users_item);

    }

    public void update(String str) {

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
            mListener = (OnProfileInteractionListener) activity;
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
    public interface OnProfileInteractionListener {
        // TODO: Update argument type and name
        public void onProfileInteraction();
    }


}
