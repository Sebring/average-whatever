package se.sebring.avgwhat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateSequenceDialog.NewSequenceListener, SequenceManager.SequenceHandler {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String PREF = "avgperday";
    private static final String PREF_CURRENT = "current";
    private static final String PREF_HISTORY = "history";
    private static final String DATE_PATTERN = "yy.MM.dd";

    private SharedPreferences mPref;
    private Sequence mSequence;
    private SequenceManager mSequenceManager;

    @Override
    public List<Sequence> getAllActive() {
        return mSequenceManager.getAllActive();
    }

    @Override
    public void updateSequence(Sequence sequence) {
        mSequenceManager.updateSequence(sequence);
    }

    @Override
    public void addSequence(Sequence sequence) {
        mSequenceManager.addSequence(sequence);
    }

    @Override
    public void selectSequence(Sequence sequence) {
        mSequence = sequence;
    }

    @Override
    public void showContextMenu() {
        //startSupportActionMode(mActionModeCallback);
    }

    @Override
    public void openSequence(Sequence sequence) {
        loadCurrentFrag(sequence);
    }

    @Override
    public void deleteSequence(Sequence sequence) {
        mSequenceManager.deleteSequence(sequence);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPref = getSharedPreferences(PREF, MODE_PRIVATE);
        //mPref.edit().clear().commit();
        mSequenceManager = new SequenceManager(mPref);

        SequenceListFrag frag = new SequenceListFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.content, frag, "FRAG").commit();
    }

    private void loadCurrentFrag(Sequence s) {
        Log.v(TAG, "loadCurrentFrag");

        //Sequence s = sm.getCurrentSequence();
        SequenceDetailFrag frag = SequenceDetailFrag.newInstance(s);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, frag, "FRAG").addToBackStack(null).commit();
    }

    private void onFabClick(View view) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("FRAG");
        ((SequenceDetailFrag) frag).onFabClick(view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_current:
                Snackbar.make(getCurrentFocus(), "Current selected", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewSequence(Sequence newSequence) {
        Log.v(TAG, "onNewSequence" + " - " + newSequence.getName());
        addSequence(newSequence);
        loadCurrentFrag(newSequence);
    }

}
