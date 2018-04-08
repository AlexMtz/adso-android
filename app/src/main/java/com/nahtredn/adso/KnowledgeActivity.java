package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nahtredn.adso.fragments.KnowledgeFragment;

public class KnowledgeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            toolbar.setSubtitle(R.string.subtitle_activity_knowledge);
            setSupportActionBar(toolbar);
        }

        AdView mAdView = findViewById(R.id.adViewKnowledge);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        KnowledgeFragment knowledgeFragment = (KnowledgeFragment)
                getSupportFragmentManager().findFragmentById(R.id.container_knowledge);

        if (knowledgeFragment == null) {
            knowledgeFragment = KnowledgeFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_knowledge, knowledgeFragment)
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
        getMenuInflater().inflate(R.menu.plus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), SkillDetailActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
