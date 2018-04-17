package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lowagie.text.DocumentException;
import com.nahtredn.fragments.VacancyFragment;
import com.nahtredn.utilities.PDF;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.title_main_activity));
            setSupportActionBar(toolbar);
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        VacancyFragment vacancyFragment = (VacancyFragment)
                getSupportFragmentManager().findFragmentById(R.id.vacancies_main_container);

        if (vacancyFragment == null) {
            vacancyFragment = VacancyFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.vacancies_main_container, vacancyFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            Intent intent = new Intent(getApplicationContext(), DataActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }

        if (id == R.id.nav_download) {
            try {
                PDF.with(getApplication()).generaSolicitud();
                Toast.makeText(getApplicationContext(),"Guardada en:\nDISPOSITIVO/ADSO/SOLICITUDES",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException fe){
                Toast.makeText(getApplicationContext(), "Error: La foto de perfil no se ha encontrado", Toast.LENGTH_LONG).show();
            } catch (DocumentException de){
                Toast.makeText(getApplicationContext(), "Error: No se ha podido generar la solicitud, intentalo más tarde",
                        Toast.LENGTH_LONG).show();
            } catch (NullPointerException npe){
                Toast.makeText(getApplicationContext(), "Error: Hay datos incompletos", Toast.LENGTH_LONG).show();
                Log.w("Main", "Error: " + npe.getMessage());
            } catch (IOException ioe){
                Toast.makeText(getApplicationContext(), "Error: No se ha podido escribir el archivo", Toast.LENGTH_LONG).show();
            }
        }

        if (id == R.id.nav_preview) {

        }

        if (id == R.id.nav_share) {

        }

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }

        if (id == R.id.nav_close_sesion) {

        }

        if (id == R.id.nav_exit) {
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}