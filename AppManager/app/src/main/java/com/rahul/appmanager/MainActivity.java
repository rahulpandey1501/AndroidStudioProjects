package com.rahul.appmanager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView manuFactTV, brandTV, modelTV, boardTV, hardwareTV, serialTV, androiIdTV, displayTV, resolutionTV, densityTV, versionTV, versionNameTV, apiLevelTV, fingerprintTV, buildIdTV, buildTimeTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intializeView();
        try {
            setTextViews();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setTextViews() throws PackageManager.NameNotFoundException {
        manuFactTV.setText(Build.MANUFACTURER);
        modelTV.setText(Build.MODEL);
        brandTV.setText(Build.BRAND);
        serialTV.setText(Build.SERIAL);
        hardwareTV.setText(Build.HARDWARE);
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
//        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
//        double screenInches = Math.sqrt(x+y);
        displayTV.setText(BuildConfig.FLAVOR);
        boardTV.setText(Build.BOARD);
        androiIdTV.setText(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        densityTV.setText(getResources().getDisplayMetrics().densityDpi + " ppi");
        resolutionTV.setText(getResources().getDisplayMetrics().widthPixels + " x " + getResources().getDisplayMetrics().heightPixels + " pixels");
        versionTV.setText(Build.VERSION.RELEASE);
        versionNameTV.setText(getVersionName(Build.VERSION.SDK_INT));
        apiLevelTV.setText(Build.VERSION.SDK);
        fingerprintTV.setText(Build.FINGERPRINT);
        buildIdTV.setText(Build.ID);
        buildTimeTV.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(Build.TIME)));
    }

    private String getVersionName(int sdkInt) {
        switch (sdkInt){
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                return "ICE_CREAM_SANDWICH";
            case Build.VERSION_CODES.JELLY_BEAN:
                return "JELLY_BEAN";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                return "ICE_CREAM_SANDWICH_MR1";
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
                return "JELLY_BEAN_MR1";
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                return "JELLY_BEAN_MR2";
            case Build.VERSION_CODES.KITKAT:
                return "KITKAT";
            case Build.VERSION_CODES.KITKAT_WATCH:
                return "KITKAT_WATCH";
            case Build.VERSION_CODES.LOLLIPOP:
                return "LOLLIPOP";
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                return "LOLLIPOP_MR1";
            case Build.VERSION_CODES.M:
                return "MARSHMALLOW";
        }
        return "N/A";
    }

    private void intializeView() {
        manuFactTV = (TextView) findViewById(R.id.manufacture_tv);
        boardTV = (TextView) findViewById(R.id.board_tv);
        modelTV = (TextView) findViewById(R.id.model_tv);
        brandTV = (TextView) findViewById(R.id.brand_tv);
        hardwareTV = (TextView) findViewById(R.id.hardware_tv);
        serialTV = (TextView) findViewById(R.id.serial_tv);
        androiIdTV = (TextView) findViewById(R.id.android_id_tv);
        displayTV = (TextView) findViewById(R.id.display_tv);
        resolutionTV = (TextView) findViewById(R.id.screen_reso_tv);
        densityTV = (TextView) findViewById(R.id.density_tv);
        versionTV = (TextView) findViewById(R.id.version_tv);
        versionNameTV = (TextView) findViewById(R.id.version_name_tv);
        apiLevelTV = (TextView) findViewById(R.id.api_level_tv);
        fingerprintTV = (TextView) findViewById(R.id.fingerprint_tv);
        buildIdTV = (TextView) findViewById(R.id.build_id_tv);
        buildTimeTV = (TextView) findViewById(R.id.build_time_tv);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sys_app) {
            // Handle the camera action
        } else if (id == R.id.nav_user_app) {

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_permis) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
