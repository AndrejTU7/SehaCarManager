package com.studioadriatic.sehacarmanager.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.fragments.AdminFragment;
import com.studioadriatic.sehacarmanager.fragments.CarsFragment;
import com.studioadriatic.sehacarmanager.fragments.ChatFragment;
import com.studioadriatic.sehacarmanager.fragments.DriverFragment;
import com.studioadriatic.sehacarmanager.fragments.GuestsFragment;
import com.studioadriatic.sehacarmanager.fragments.LocationFragment;
import com.studioadriatic.sehacarmanager.fragments.RidesFragment;
import com.studioadriatic.sehacarmanager.models.User;
import com.studioadriatic.sehacarmanager.services.RegistrationIntentService;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    private static final String TAG = "SEHA MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Device is registered.");
            }
        };

        String token = App.getCurrentToken(this);
        if(token.equals("")){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        } else {
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        RequestParams params = Api.getRequestParams(token);
        Api.post(Api.USER_LOGIN_TOKEN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        String jsonOutput = new String(responseBody, "UTF-8");
                        User user = new Gson().fromJson(jsonOutput, User.class);
                        App.setCurrentUser(user);
                        View headerView = getLayoutInflater().inflate(R.layout.navigation_header, navigationView, false);
                        TextView email = (TextView) headerView.findViewById(R.id.email);
                        email.setText(user.getEmail());
                        TextView name = (TextView) headerView.findViewById(R.id.name);
                        switch (user.getType()) {
                            case User.ADMIN:
                                navigationView.inflateMenu(R.menu.nav_menu_admin);
                                name.setText(R.string.administrator);
                                break;
                            case User.MODERATOR:
                                name.setText(R.string.moderator);
                                break;
                            case User.DRIVER:
                                navigationView.inflateMenu(R.menu.nav_menu_driver);
                                name.setText(R.string.driver);
                                break;
                            case User.REFEREE_DRIVER:
                                name.setText(R.string.driver);
                                break;
                        }
                        navigationView.addHeaderView(headerView);
                        navigationView.setCheckedItem(R.id.nav_rides);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 400) {
                    try {
                        String jsonOutput = new String(responseBody, "UTF-8");
                        ApiError apiError = new Gson().fromJson(jsonOutput, ApiError.class);
                        Toast.makeText(MainActivity.this, apiError.getError(), Toast.LENGTH_SHORT).show();

                        logoutUser();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Remove token from Shared Preferences
     * and redirect to Login Activity
     */
    private void logoutUser() {
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(App.SP_TOKEN, "").commit();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        int contentId = content.getId();
        switch (id) {
            case R.id.nav_rides:
                fragmentTransaction.replace(contentId, new RidesFragment()).commitAllowingStateLoss();
                break;
            case R.id.nav_cars:
                fragmentTransaction.replace(contentId, new CarsFragment()).commitAllowingStateLoss();
                break;
            case R.id.nav_drivers:
                fragmentTransaction.replace(contentId, new DriverFragment(), DriverFragment.TAG).commitAllowingStateLoss();
                break;
            case R.id.nav_vip_guests:
                fragmentTransaction.replace(contentId, new GuestsFragment()).commitAllowingStateLoss();
                break;
            case R.id.nav_chat:
                fragmentTransaction.replace(contentId, new ChatFragment()).commitAllowingStateLoss();
                break;
            case R.id.nav_locations:
                fragmentTransaction.replace(contentId, new LocationFragment()).commitAllowingStateLoss();
                break;
            case R.id.nav_admins:
                fragmentTransaction.replace(contentId, new AdminFragment(), AdminFragment.TAG).commitAllowingStateLoss();
                break;
            case R.id.nav_logout:
                logoutUser();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(App.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
