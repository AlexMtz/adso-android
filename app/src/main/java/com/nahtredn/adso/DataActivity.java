package com.nahtredn.adso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nahtredn.fragments.DataFragment;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;

import java.io.File;

public class DataActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
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

        mAdView = findViewById(R.id.adViewData);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        DataFragment dataFragment = (DataFragment)
                getSupportFragmentManager().findFragmentById(R.id.data_items_data_container);

        if (dataFragment == null) {
            dataFragment = DataFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.data_items_data_container, dataFragment)
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_preview) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File f = new File(RealmController.with(this).find(PreferencesProperties.PATH_FILE.toString()));
            if (f.exists()) {
                intent.setDataAndType(Uri.fromFile(f), "application/pdf");
                startActivity(Intent.createChooser(intent, "Visualizar mediante"));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
