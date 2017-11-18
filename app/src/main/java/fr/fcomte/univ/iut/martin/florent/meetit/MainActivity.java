package fr.fcomte.univ.iut.martin.florent.meetit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.action_settings;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.drawer_layout;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.fab;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_camera;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_gallery;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_manage;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_send;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_share;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_slideshow;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.nav_view;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.toolbar;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.activity_main;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.menu.main;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.navigation_drawer_close;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.navigation_drawer_open;

public final class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        final Toolbar bar = findViewById(toolbar);
        setSupportActionBar(bar);

        findViewById(fab).setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        final DrawerLayout drawer = findViewById(drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bar, navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView) findViewById(nav_view)).setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case nav_camera:
                // Handle the camera action
                break;
            case nav_gallery:

                break;
            case nav_slideshow:

                break;
            case nav_manage:

                break;
            case nav_share:

                break;
            case nav_send:

                break;
        }

        ((DrawerLayout) findViewById(drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }
}
