package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nahtredn.adso.fragments.ReferenceFragment;

public class ReferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            setSupportActionBar(toolbar);
        }

        AdView mAdView = findViewById(R.id.adViewReferences);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ReferenceFragment referenceFragment = (ReferenceFragment)
                getSupportFragmentManager().findFragmentById(R.id.container_reference);

        if (referenceFragment == null) {
            referenceFragment = ReferenceFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_reference, referenceFragment)
                    .commit();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_studies_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), ReferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
