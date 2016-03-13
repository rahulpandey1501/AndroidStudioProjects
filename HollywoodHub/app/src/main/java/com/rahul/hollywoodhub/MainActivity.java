package com.rahul.hollywoodhub;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,android.support.v7.widget.SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private static ImageView header;
    private CollapsingToolbarLayout collapsingToolbar;
    private TabPagerAdapter mTabPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    public String[] genre;
    public Spinner spinner;
    TextView genreIVButton;
    private String searchQuery="";
    static boolean forMoviePage = true;
    String tvSeriesText = "";
    Snackbar snack;
    boolean doubleBackToExitPressedOnce = false;
    static String GENRE_LINK="http://www.watchfree.to/?genre=", SEARCH_SECTION_TV="&search_section=2", SEARCH_SECTION_MOVIE="&search_section=1";
    static String DEFAULT_LINK="http://www.watchfree.to/?sort=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        genre = getResources().getStringArray(R.array.genre);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        genreIVButton = (TextView) findViewById(R.id.genre_iv);
        header = (ImageView) findViewById(R.id.backdrop);
        genreIVButton.setTranslationY(-25f);
//        mTabLayout.setTranslationY(-20f);
        spinner = (Spinner) findViewById(R.id.spinner_nav);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.genre, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (genre[position].equalsIgnoreCase("all")) {
                    collapsingToolbar.setTitle("Hollywood");
                    getSupportActionBar().setTitle("Hollywood");
                }
                else{
                    collapsingToolbar.setTitle(genre[position]);
                    getSupportActionBar().setTitle(genre[position]);
                }
                searchQuery = "";
                if (position == 0)
                    setViewPagerAdapter(DEFAULT_LINK);
                else
                    setViewPagerAdapter(GENRE_LINK + spinner.getSelectedItem().toString() + "&sort=");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genreIVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setViewPagerAdapter(String link) {
        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), link);
        mTabLayout.setTabsFromPagerAdapter(mTabPagerAdapter);
        mViewPager.setAdapter(mTabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            snack = Snackbar.make(drawer, "Press back again to exit", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.white));
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snack.show();
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Picasso.with(MainActivity.this).load(R.drawable.header_movie).into(header);
            spinner.setSelection(0);
            collapsingToolbar.setTitle("Hollywood");
            searchQuery = "";
            tvSeriesText = "";
            forMoviePage = true;
            setViewPagerAdapter(DEFAULT_LINK);
        } else if (id == R.id.nav_gallery) {
            Picasso.with(MainActivity.this).load(R.drawable.header_tvseries).into(header);
            collapsingToolbar.setTitle("TV Series");
            spinner.setSelected(false);
            spinner.setSelection(0);
            searchQuery = "";
            tvSeriesText = "&tv=";
            forMoviePage = false;
            setViewPagerAdapter(DEFAULT_LINK);
        }else if (id == R.id.nav_share) {
            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
                File srcFile = new File(ai.publicSourceDir);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/vnd.android.package-archive");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
                startActivity(Intent.createChooser(share, "Share App .."));
            } catch (Exception e) {
                Log.e("ShareApp", e.getMessage());
            }
        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","rahulpandey1501@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback -Hollywood Hub");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        tvSeriesText = "";
        if (forMoviePage)
            searchQuery = query.replace(" ", "+")+SEARCH_SECTION_MOVIE;
        else
            searchQuery = query.replace(" ", "+")+SEARCH_SECTION_TV;
        collapsingToolbar.setTitle(query);
        getSupportActionBar().setTitle(query);
        setViewPagerAdapter(DEFAULT_LINK);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class TabPagerAdapter extends FragmentStatePagerAdapter{

        String link;
        public TabPagerAdapter(FragmentManager fm, String link) {
            super(fm);
            this.link = link;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return RecyclerViewFragment.newInstance(link+"&keyword="+searchQuery+tvSeriesText);
                case 1:
                    return RecyclerViewFragment.newInstance(link+"release&keyword="+searchQuery+tvSeriesText);
                case 2:
                    return RecyclerViewFragment.newInstance(link+"views&keyword="+searchQuery+tvSeriesText);
                case 3:
                    return RecyclerViewFragment.newInstance(link+"alphabet&keyword="+searchQuery+tvSeriesText);
                default:
                    return RecyclerViewFragment.newInstance(DEFAULT_LINK);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Top";
                case 1:
                    return "Release";
                case 2:
                    return "Popular";
                case 3:
                    return "A-Z";
                default:
                    return "All";
            }
        }
    }
    public static ImageView getHeaderImage(){
        return header;
    }
}
