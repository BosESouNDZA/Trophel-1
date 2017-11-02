package com.example.bhurivatmontri.trophel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.fragment.Attraction;
import com.example.bhurivatmontri.trophel.fragment.ListFriend;
import com.example.bhurivatmontri.trophel.fragment.Map;
import com.example.bhurivatmontri.trophel.fragment.Profile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.PorterDuff.Mode.SRC_IN;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_attraction_144dp,
            R.drawable.ic_map_144dp,
            R.drawable.ic_friend_144dp,
            R.drawable.ic_profile_144dp,
            //R.drawable.ic_menu_144dp,
    };
    private Drawer.Result navigationDrawerLeft ;
    private AccountHeader.Result headerNavigationLeft ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 :
                        getSupportActionBar().setTitle("Attraction");
                        break;
                    case 1 :
                        getSupportActionBar().setTitle("Map");
                        break;
                    case 2 :
                        getSupportActionBar().setTitle("Friend");
                        break;
                    case 3 :
                        getSupportActionBar().setTitle("Profile");
                        break;
                    default:
                        getSupportActionBar().setTitle("Trophel");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabIcons();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        headerNavigationLeft = new AccountHeader().withActivity(this).withCompactStyle(false)
                .withSavedInstance(savedInstanceState).withHeaderBackground(R.color.CyanA700)
                .addProfiles(
                        new ProfileDrawerItem().withName("Bhurivat Montri").withEmail("bhurivat.m@gmail.com")
                        .withIcon(getResources().getDrawable(R.drawable.profile_bhurivat_montri))
                ).build();
        navigationDrawerLeft = new Drawer().withActivity(this).withToolbar(toolbar)
                .withDisplayBelowToolbar(false).withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT).withSavedInstance(savedInstanceState)
                .withAccountHeader(headerNavigationLeft).withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem){
                        if(drawerItem.getIdentifier() == 100){
                            Toast.makeText(Home.this, "Attraction Processing", Toast.LENGTH_SHORT).show();
                            mViewPager.setCurrentItem(0);
                        }
                        else if(drawerItem.getIdentifier() == 200){
                            Toast.makeText(Home.this, "Map Processing", Toast.LENGTH_SHORT).show();
                            mViewPager.setCurrentItem(1);
                        }
                        else if(drawerItem.getIdentifier() == 300){
                            Toast.makeText(Home.this, "List Friend Processing", Toast.LENGTH_SHORT).show();
                            mViewPager.setCurrentItem(2);
                        }
                        else if(drawerItem.getIdentifier() == 400){
                            Toast.makeText(Home.this, "Profile Processing", Toast.LENGTH_SHORT).show();
                            mViewPager.setCurrentItem(3);
                        }else if(drawerItem.getIdentifier() == 600){
                            Toast.makeText(Home.this, "Logout" , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Home.this,Login.class);
                            Logout();
                        }
                    }
                })
                .build();

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Attraction").withIcon(getResources().getDrawable(R.drawable.icnav_attraction_144dp)).withIdentifier(100));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Map").withIcon(getResources().getDrawable(R.drawable.icnav_map_144dp)).withIdentifier(200));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Friend").withIcon(getResources().getDrawable(R.drawable.icnav_friend_144dp)).withIdentifier(300));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Profile").withIcon(getResources().getDrawable(R.drawable.icnav_person_144dp)).withIdentifier(400));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("About us").withIcon(getResources().getDrawable(R.drawable.icnav_about_us_144dp)).withIdentifier(500));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Logout").withIcon(getResources().getDrawable(R.drawable.icnav_logout_144dp)).withIdentifier(600));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_search_attraction :
                Toast.makeText(this,"Search in Attraction",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_search_map :
                Toast.makeText(this,"Search in Map",Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.menu_search_friend :
                Toast.makeText(this,"Search in ListFriend",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_add_friend :
                Toast.makeText(this,"Add Friend",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*@Overrideww
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }*/

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        //tabLayout.getTabAt(4).setIcon(tabIcons[4]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#A8A8A8"), SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#A8A8A8"), SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#A8A8A8"), SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#A8A8A8"), SRC_IN);
        //tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#A8A8A8"), SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#A8A8A8"), PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager mViewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Attraction(), "Attraction");
        adapter.addFragment(new Map(), "Map");
        adapter.addFragment(new ListFriend(), "Friend");
        adapter.addFragment(new Profile(), "Profile");
        //adapter.addFragment(new AppMenu(), "Menu");
        mViewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return null to display only the icon
            return mFragmentTitleList.get(position);
        }
    }
    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}