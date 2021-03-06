package fr.fcomte.univ.iut.martin.florent.meetit.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fr.fcomte.univ.iut.martin.florent.meetit.views.fragments.CameraFragment;
import fr.fcomte.univ.iut.martin.florent.meetit.views.fragments.CreditsFragment;
import fr.fcomte.univ.iut.martin.florent.meetit.views.fragments.DataFragment;
import fr.fcomte.univ.iut.martin.florent.meetit.views.fragments.HomeFragment;
import fr.fcomte.univ.iut.martin.florent.meetit.views.fragments.MapFragment;
import lombok.experimental.FieldDefaults;

import static android.support.v4.view.GravityCompat.START;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.action_settings;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.content_main;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.drawer_layout;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.fab;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_camera;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_credits;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_data;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_home;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_map;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_view;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.toolbar;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.activity_main;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.menu.main;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.navigation_drawer_close;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.navigation_drawer_open;
import static lombok.AccessLevel.PRIVATE;

/**
 * Activité principale chargée au démarrage de l'application <br/>
 * Hérite de {@link AppCompatActivity} <br/>
 * Implémente {@link NavigationView.OnNavigationItemSelectedListener}
 */
@FieldDefaults(level = PRIVATE)
public final class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;

    /**
     * Change le fragment affiché à l'écran
     */
    void changeFragment() {
        getSupportFragmentManager().beginTransaction().replace(content_main, fragment).commit();
    }

    /**
     * Création de la vue, de la barre de menu et de {@link HomeFragment}
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        final Toolbar bar = findViewById(toolbar);
        setSupportActionBar(bar);

        findViewById(fab).setOnClickListener(
                view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show());

        final DrawerLayout drawer = findViewById(drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bar, navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView) findViewById(nav_view)).setNavigationItemSelectedListener(this);

        fragment = new HomeFragment();
        changeFragment();
    }

    /**
     * @see AppCompatActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = findViewById(drawer_layout);
        if (drawer.isDrawerOpen(START))
            drawer.closeDrawer(START);
        else
            super.onBackPressed();
    }

    /**
     * @param menu {@link Menu}
     * @return boolean
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main, menu);
        return true;
    }

    /**
     * @param item {@link MenuItem}
     * @return boolean
     * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Création et affichage d'un nouveau fragment suivant l'option choisie dans le menu
     *
     * @param item {@link MenuItem}
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case nav_home:
                fragment = new HomeFragment();
                break;
            case nav_camera:
                fragment = new CameraFragment();
                break;
            case nav_data:
                fragment = new DataFragment();
                break;
            case nav_map:
                fragment = new MapFragment();
                break;
            case nav_credits:
                fragment = new CreditsFragment();
                break;
        }

        changeFragment();
        ((DrawerLayout) findViewById(drawer_layout)).closeDrawer(START);
        return true;
    }
}
