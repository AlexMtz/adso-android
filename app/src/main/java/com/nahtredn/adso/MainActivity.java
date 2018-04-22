package com.nahtredn.adso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lowagie.text.DocumentException;
import com.nahtredn.adapters.VacancyAdapter;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.fragments.VacancyFragment;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.PDF;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdView mAdView;
    private ProgressDialog pDialog;
    private String result;
    private VacancyFragment vacancyFragment;
    private String errorMesage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!RealmController.with(this).isLogged()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.title_main_activity));
            setSupportActionBar(toolbar);
        }

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

        vacancyFragment = (VacancyFragment)
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_download) {
            new FetchVacants().execute("https://naht-redn-dev.cloud.tyk.io/vacancies-reader/api/v1/vacancies/");
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

        if (id == R.id.nav_preview) {
            GeneratePdf task = new GeneratePdf(2);
            task.execute();
        }

        if (id == R.id.nav_share) {
            GeneratePdf task = new GeneratePdf(1);
            task.execute();
        }

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }

        if (id == R.id.nav_close_sesion) {
            RealmController.with(this).save(PreferencesProperties.IS_LOGGED.toString(), false);
            this.finish();
        }

        if (id == R.id.nav_exit) {
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GeneratePdf extends AsyncTask<String, String, String> {
        private int action;

        public GeneratePdf(int action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
            pDialog.setMessage("Generando solicitud...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                errorMesage = PDF.with(getApplication()).generaSolicitud();
                if(!errorMesage.equals("")){
                    this.cancel(true);
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            File f = new File(RealmController.with(getApplication()).find(PreferencesProperties.PATH_FILE.toString()));
            if (f.exists()){
                if (action == 1){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + f.getAbsolutePath()));
                    intent.setType("application/pdf");
                    startActivity(Intent.createChooser(intent, "Compartir mediante"));
                }

                if (action == 2){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(f), "application/pdf");
                    startActivity(Intent.createChooser(intent, "Visualizar mediante"));
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            Messenger.with(MainActivity.this).showMessage(errorMesage);
        }
    }

    class FetchVacants extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RealmController.with(MainActivity.this).deleteAllVacancies();
            pDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
            pDialog.setMessage("Descargando nuevas vacantes...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setConnectTimeout(5000);
                conection.connect();
                InputStream input = new BufferedInputStream(url.openStream());
                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(input, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }
                    input.close();
                    result = sBuilder.toString();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error converting result " + e.toString());
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    Vacancy vacancy = new Vacancy();
                    vacancy.setId(jObject.getString("id"));
                    vacancy.setJobTitle(jObject.getString("job_title"));
                    vacancy.setType(jObject.getString("job_type"));
                    vacancy.setCompany(jObject.getString("company"));
                    vacancy.setSalary(jObject.getString("salary"));
                    vacancy.setWorkingDay(jObject.getString("working_week"));
                    vacancy.setLocation(jObject.getString("location"));
                    vacancy.setDescription(jObject.getString("description"));
                    vacancy.setSkills(jObject.getString("skills"));
                    vacancy.setKnowledgments(jObject.getString("knowledge"));
                    vacancy.setBenefits(jObject.getString("benefits"));
                    vacancy.setExperience(jObject.getString("experience"));
                    vacancy.setEmail(jObject.getString("email"));
                    vacancy.setPhone(jObject.getString("phone"));

                    RealmController.with(MainActivity.this).save(vacancy);
                }
            } catch (JSONException e) {
                Messenger.with(MainActivity.this).showMessage("Error al consultar las vacantes en el servidor");
                Log.e("JSONException", "Error: " + e.toString());
            } catch (NullPointerException npe) {
                Messenger.with(MainActivity.this).showMessage("No se encontró ningúna vacante");
                Log.e("NullPointerException", "Error: " + npe.toString());
            }
            vacancyFragment.onResume();
            pDialog.dismiss();
        }

    }
}