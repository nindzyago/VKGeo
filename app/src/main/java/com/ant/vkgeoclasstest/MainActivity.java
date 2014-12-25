package com.ant.vkgeoclasstest;

import java.util.ArrayList;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ant.vkgeoclasstest.ProfileFragment.OnProfileInteractionListener;
import com.ant.vkgeoclasstest.CitiesFragment.OnCitiesInteractionListener;
import com.ant.vkgeoclasstest.MapFragment.OnMapInteractionListener;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
//import android.app.ActionBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        OnProfileInteractionListener, OnCitiesInteractionListener, OnMapInteractionListener {

    private ArrayList<User> Users;
    private SortedSet<City> Cities;
    private SortedSet<Country> Countries;
    private User Profile;

    String out = "";

    AsyncVKInfo asyncVKInfo;
    AsyncFindCities asyncFindCities;
    //boolean ProfileLoaded=false;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // LOGIN VK !!!

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
           }

        // Getting VK information in AsyncTask
        asyncVKInfo = new AsyncVKInfo();
        asyncVKInfo.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Fragment frag1 = getFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getCurrentItem()));
        //((TextView) frag1.getView().findViewById(R.id.tvOut))

        if (id == R.id.action_settings) {
            MyApplication myApp = (MyApplication) this.getApplication();
            myApp.setLoaded(true);

            Fragment currFragment = getSupportFragmentManager().findFragmentByTag(getFragmentTag(0));
            Fragment newFragment = new ProfileFragment().newInstance();
            FragmentTransaction transaction = currFragment.getChildFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentMain, newFragment, "fragmentProfile").commit();

            ((ProgressBar) currFragment.getView().findViewById(R.id.progressBar)).setVisibility(View.GONE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

  /**  @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    //@Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }*/

    private String getFragmentTag(int pos){
        return "android:switcher:"+R.id.pager+":"+pos;
    }

    @Override
    public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*switch (position) {
                case 0:
                    return ProfileFragment.newInstance();
                case 1:
                    return MapFragment.newInstance();
                case 2:
                    return CitiesFragment.newInstance();
                case 3:
                    return CitiesFragment.newInstance();
                default:
                    return null;
            }*/
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    public void onProfileInteraction() {
    }

    public void onCitiesInteraction() {
    }

    public void onMapInteraction() {
    }

    public void initClasses () {

        MyApplication myApp = (MyApplication) this.getApplication();

        Cities = myApp.getCities();
        Users = myApp.getUsers();
        Countries = myApp.getCountries();
        //Profile = myApp.getProfile();

        Country Russia = new Country(1, "Russia");
        Country USA = new Country(2, "USA");
        City Abakan = new City(1, "Abakan");
        City Moscow = new City(2, "Moscow");
        City NewYork = new City(3, "New York");
        Abakan.setCountry(Russia);
        NewYork.setCountry(USA);
        Cities.add(Abakan);
        Cities.add(Moscow);
        Cities.add(NewYork);
        Countries.add(Russia);
        Countries.add(USA);

        User Vasya = new User (2, "Vasya");
        User Petya = new User (3, "PetyaMSK");
        User Tolik = new User (4, "Tolik");
        User Pendos = new User (5, "Pendos");

        Users.add(Vasya);
        Users.add(Petya);
        Users.add(Tolik);
        Users.add(Pendos);

        Vasya.setCountry(Russia);
        Vasya.setCity(Abakan);
        Petya.setCity(Moscow);
        Tolik.setCity(Abakan);
        Pendos.setCity(NewYork);

        User Mike = new User(1, "Mike Antipiev");
        Mike.setCity(Abakan);

        myApp.setProfile(Mike);

        for (City city : Cities) {
            out = out + city.toString() + " (" + city.getCountUsers() + ", "+ Users.size()+ ") | ";
        }

        myApp.setCities(Cities);
        myApp.setCountries(Countries);
        myApp.setUsers(Users);

    }

    class AsyncVKInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            initClasses();
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {

        }
        @Override
        protected void onPostExecute(Void result) {
            asyncFindCities = new AsyncFindCities();
            asyncFindCities.execute();
        }

    }

    class AsyncFindCities extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {

        }
        @Override
        protected void onPostExecute(Void result) {

        }

    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int sectionNumber;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

/*        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }*/

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // TODO: Set progressBar invisible when rotate
            //MyApplication myApp = (MyApplication) getApplication();
            //if (myApp.isLoaded()) {  ((ProgressBar) rootView.findViewById(R.id.progressBar)).setVisibility(View.GONE); }

            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (getArguments() != null) {
                sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
                switch (sectionNumber) {
                    case 1: {
                    }
                   /* case 2: {
                        Fragment currFragment = new CitiesFragment().newInstance();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.add(view.getId(), currFragment).commit();
                    }*/
                }
            }

        }

        @Override
        public void onDetach() {
            super.onDetach();
                /*Fragment currFragment = getChildFragmentManager().findFragmentByTag("fragmentProfile");
                if (currFragment != null){
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.remove(currFragment).commit();
                }*/

        }

    }

}
