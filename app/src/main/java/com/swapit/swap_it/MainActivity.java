package com.swapit.swap_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.swapit.swap_it.ServiceJava.CreationAnnonceServiceActivity;
import com.swapit.swap_it.ServiceJava.FragmentService;
import com.swapit.swap_it.SoutienJava.CreationAnnonceSoutienActivity;
import com.swapit.swap_it.SoutienJava.FragmentSoutien;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;                                // Permet l'affichage des tabs
    private ViewPager viewPager;                                // Permet l'affichage de différentes activités
    private ViewPagerAdapter adapter;                           // Permet de choisir l'activité à afficher
    public static final String IDENTITE_USER = "IdentiteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gestion du bouton ajout d'annonce
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuAnnonce(view);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        adapter = new ViewPagerAdapter (getSupportFragmentManager());

        adapter.AddFragment(new FragmentService(), "Service");
        adapter.AddFragment(new FragmentSoutien(), "Soutien");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //ferme l'appli
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

            super.onBackPressed();
        }
    }

    //lancement des pages de creation d'annonce en fonction du choix de l'utilisateur
    public boolean lancementCreationAnnonce(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_service){
            Intent intentService = new Intent(this,CreationAnnonceServiceActivity.class);
            startActivity(intentService);
            return  true;
        }
        else if (id == R.id.menu_soutien){
            Intent intentSoutien = new Intent(this,CreationAnnonceSoutienActivity.class);
            startActivity(intentSoutien);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Gestion des items dans le tiroir latéral
        switch (item.getItemId()) {
            case R.id.nav_profile:
                // Vers la page profile
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                return true;
            case R.id.nav_settings:
                // Vers la page paramètre
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.nav_deco:
                // Permet de se déconecter
                deconnexion();
                // TODO: relancer page login
                return true;
            case R.id.nav_concerning:
                // Vers la page a propos
                Intent intentConcerning = new Intent(this, ConcerningActivity.class);
                startActivity(intentConcerning);
                return true;
            case R.id.nav_contact:
                // Vers la page contact
                Intent intentContact = new Intent(this, ContactActivity.class);
                startActivity(intentContact);
                return true;
        }
        // Affichage du tiroire latéral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deconnexion(){
        //SharedPreferences id = getApplicationContext().getSharedPreferences(IDENTITE_USER, MODE_PRIVATE);
        //SharedPreferences.Editor editor = id.edit();
        SharedPreferences.Editor editor = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE ).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //fait apparaitre les menus puis "listener" sur le menu
    public void showPopupMenuAnnonce(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_annonce, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                lancementCreationAnnonce(menuItem);
                return true;
            }
        });
    }

    //fait apparaitre le menu dans les 3 points coin en haut à droite
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
