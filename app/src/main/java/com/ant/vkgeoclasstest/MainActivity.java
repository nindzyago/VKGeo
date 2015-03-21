package com.ant.vkgeoclasstest;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.SortedSet;

import com.ant.vkgeoclasstest.ProfileFragment.OnProfileInteractionListener;
import com.ant.vkgeoclasstest.CitiesFragment.OnCitiesInteractionListener;
import com.ant.vkgeoclasstest.MapFragment.OnMapInteractionListener;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        OnProfileInteractionListener, OnCitiesInteractionListener, OnMapInteractionListener,
        CountriesFragment.OnCountriesInteractionListener {

    /**
     * Define arrays to store VK data
     */
    private ArrayList<User> Users;
    private SortedSet<City> Cities;
    private SortedSet<Country> Countries;
    private User Profile;

    /**
     * Id's to pass current user and cities between fragments
     */
    private int userId, cityId;

    /**
     * Async Task for loading VK info
     */
    AsyncVKInfo asyncVKInfo;

    /**
     * Singleton app variable to retrieve global arrays
     */
    MyApplication myApp;

    //boolean ProfileLoaded=false;

    /**
     * UI variables
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ActionBar actionBar;

    private static final String VK_APP_ID = "4697955";
    private final VKSdkListener sdkListener = new VKSdkListener() {

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Log.d("VkDemoApp", "onAcceptUserToken " + token);
            //startLoading();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            Log.d("VkDemoApp", "onReceiveNewToken " + newToken);
            //startLoading();
        }

        @Override
        public void onRenewAccessToken(VKAccessToken token) {
            Log.d("VkDemoApp", "onRenewAccessToken " + token);
            //startLoading();
        }

        @Override
        public void onCaptchaError(VKError captchaError) {
            Log.d("VkDemoApp", "onCaptchaError " + captchaError);
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            Log.d("VkDemoApp", "onTokenExpired " + expiredToken);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            Log.d("VkDemoApp", "onAccessDenied " + authorizationError);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Getting User and City vars from Intent
         */
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        cityId = intent.getIntExtra("cityId", 0);

        myApp = (MyApplication) getApplication();
        if (cityId!=0) {
            myApp.setLoaded(true);
        }

        /**
         * Login to vk.com
         */
        VKSdk.initialize(sdkListener, VK_APP_ID);
        VKUIHelper.onCreate(this);
        if (!VKSdk.wakeUpSession()) {
            VKSdk.authorize(VKScope.FRIENDS, VKScope.GROUPS, VKScope.PHOTOS, VKScope.WALL);
        }

        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager and PagerAdapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
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

        startLoading();
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

        // Temp action for test
        //removeFragments();
        if (id == R.id.action_settings) {
            GetFusionTablesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "ANT-VKGeo/1.0";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/fusion_tables_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Fusiontables fusiontables;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(
                        FusionTablesSample.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
                            + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
                dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }


    private void GetFusionTablesData() {

    }

    // Getting Tag name of a Fragment by position
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
            // Show 4 total pages.
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
    // Listen actions from Fragments
    public void onProfileInteraction() {
    }

    public void onCitiesInteraction() {
    }

    public void onCountriesInteraction() {
    }

    public void onMapInteraction() {
        mViewPager.setCurrentItem(0);
    }


    // Loading VK info
    private void startLoading() {
        // Getting VK information in AsyncTask

        if (!myApp.isLoaded()) {
            asyncVKInfo = new AsyncVKInfo();
            asyncVKInfo.execute();
        } else {
            if (cityId!=0) {
                //showFragments();
            }
        }
    }

    private void showFragments() {
        // Show content fragments in ViewPager placeholders
        Fragment currFragment;
        FragmentTransaction transaction;
        Fragment newFragment;

        for (int i=0;i<4;i++) {
            // Get Fragment by ViewPager Tag
            currFragment = getSupportFragmentManager().findFragmentByTag(getFragmentTag(i));
            transaction = currFragment.getChildFragmentManager().beginTransaction();

            switch (i) {
                case 0: newFragment = new ProfileFragment().newInstance(); break;
                case 1: newFragment = new MapFragment().newInstance(); break;
                case 2: newFragment = new CitiesFragment().newInstance(); break;
                case 3: newFragment = new CountriesFragment().newInstance(); break;
                default: newFragment = new Fragment();
            }
            // Add content Fragment to Placeholder
            newFragment.setRetainInstance(true);
            transaction.add(R.id.fragmentMain, newFragment, "fragment"+i).commit();
            ((ProgressBar) currFragment.getView().findViewById(R.id.progressBar)).setVisibility(View.GONE);
        }
    }

    private void removeFragments() {
        Fragment currFragment, tempFragment;
        FragmentTransaction transaction;
        for (int i=0;i<4;i++) {
            currFragment = getSupportFragmentManager().findFragmentByTag(getFragmentTag(i));
            tempFragment = currFragment.getChildFragmentManager().findFragmentByTag("fragment"+i);
            transaction = currFragment.getChildFragmentManager().beginTransaction();
            transaction.remove(tempFragment).commit();
        }
    }


    // TODO: Remove Temporary initialization
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
           // out = out + city.toString() + " (" + city.getCountUsers() + ", "+ Users.size()+ ") | ";
        }

        myApp.setCities(Cities);
        myApp.setCountries(Countries);
        myApp.setUsers(Users);

    }

    class AsyncVKInfo extends AsyncTask<Void, Void, Void> {

        private VKRequest friendsRequest, profileRequest;
        private String currentPhoto;
        private User currentUser;
        private City currentCity;
        private Country currentCountry;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            //initClasses();
//            final MyApplication myApp = (MyApplication) getApplication();
            myApp.clearData();
            Cities = myApp.getCities();
            Users = myApp.getUsers();
            Countries = myApp.getCountries();

            if (friendsRequest != null)   { friendsRequest.cancel();   }
            if (profileRequest != null) { profileRequest.cancel(); }

            // Default profile and friends request
            friendsRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "country,city,id,first_name,last_name,photo_200"));
            profileRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "country,city,id,first_name,last_name,photo_200"));

            // Selected user request
            if (userId != 0) {
                friendsRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "country,city,id,first_name,last_name,photo_200", VKApiConst.USER_ID, userId));
                profileRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "country,city,id,first_name,last_name,photo_200", VKApiConst.USER_ID, userId));
            }
            
            // Batch execution of VK requests
            VKBatchRequest batch = new VKBatchRequest(friendsRequest, profileRequest);
            batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
                @Override
                public void onComplete(VKResponse[] responses) {
                    super.onComplete(responses);

                    // Parse Profile request
                    VKList<VKApiUserFull> VKUser = (VKList<VKApiUserFull>) responses[1].parsedModel;
                    for (VKApiUserFull user : VKUser) {
                        Profile = new User(user.id, user.toString());
                        
                        currentCity = new City(user.city.id, user.city.toString());
                        currentCountry = new Country(user.country.id, user.country.toString());

                        Profile.setCity(currentCity);
                        Profile.setCountry(currentCountry);

                        currentPhoto = "";
                        //if (user.photo_50 != null) {currentPhoto = user.photo_50.toString();  	   }
                        //if (user.photo_100 != null) {currentPhoto = user.photo_100.toString();  	   }
                        if (user.photo_200 != null) {
                            currentPhoto = user.photo_200.toString();
                        }
                        Profile.setPhoto(currentPhoto);
                    }

                    // Parse Friends request
                    VKUsersArray usersArray = (VKUsersArray) responses[0].parsedModel;
                    for (VKApiUserFull user : usersArray) {
                        currentUser = new User(user.id, user.toString());

                        currentPhoto = "";
                        //if (user.photo_50 != null) {currentPhoto = user.photo_50.toString();  	   }
                        //if (user.photo_100 != null) {currentPhoto = user.photo_100.toString();  	   }
                        if (user.photo_200 != null) {
                            currentPhoto = user.photo_200.toString();
                        }
                        currentUser.setPhoto(currentPhoto);

                        if (user.city != null) {
                            // Make new City object for current user
                            currentCity = new City(user.city.id, user.city.toString());
                            // If currentCity is exists in global Cities array, replace it with the existing object
                            if (Cities.contains(currentCity)) {
                                currentCity = Cities.tailSet(currentCity).first();
                            }
                            if (user.country != null) {
                                // The same with the countries
                                currentCountry = new Country(user.country.id, user.country.toString());
                                if (Countries.contains(currentCountry)) {
                                    currentCountry = Countries.tailSet(currentCountry).first();
                                }
                                currentCity.setCountry(currentCountry);
                                Countries.add(currentCountry);
                            }
                            currentUser.setCity(currentCity);
                            currentUser.setCountry(currentCountry);
                            Cities.add(currentCity);
                        } else {
                            // TODO: Decide what to do with null city in friend profile
                        }
                        Users.add(currentUser);
                    }
                    myApp.setCities(Cities);
                    myApp.setCountries(Countries);
                    myApp.setUsers(Users);
                    myApp.setProfile(Profile);
                    myApp.setLoaded(true);

                    showFragments();

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
        @Override
        protected void onPostExecute(Void result) {
            // Store arrays in singleton
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Set progressBar invisible when rotate
            MyApplication myApp = (MyApplication) this.getActivity().getApplication();
            if (myApp.isLoaded()) {  ((ProgressBar) rootView.findViewById(R.id.progressBar)).setVisibility(View.GONE); }

            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
 /*           if (getArguments() != null) {
                sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
                switch (sectionNumber) {
                    case 1: {
                    }
                }
            }*/
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        //removeFragments();

        // TODO: Remove requests
//            if (currentRequest != null) {
//                currentRequest.cancel();
//            }
    }


}
