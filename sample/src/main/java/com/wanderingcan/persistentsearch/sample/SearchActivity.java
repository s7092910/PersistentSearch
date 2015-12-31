package com.wanderingcan.persistentsearch.sample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows how to use a PersistentSearchView along with using text to speech
 */
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private PersistentSearchView mSearchView;

    private boolean mMicEnabled;

    private static final int VOICE_RECOGNITION_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMicEnabled = isIntentAvailable(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));

        Button button = (Button) findViewById(R.id.button);
        button.setText("Go to Drawer Example");

        mSearchView = (PersistentSearchView) findViewById(R.id.search_bar);
        mSearchView.setEndDrawable(null);
        mSearchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
                mSearchView.getSearchMenu().addSearchMenuItem(2, "Suggestion")
                        .setIcon(R.drawable.ic_globe);
                if(!ViewUtils.isLayoutRtl(mSearchView)) {
                    mSearchView.setNavigationDrawable(ContextCompat
                            .getDrawable(SearchActivity.this, R.drawable.ic_action_arrow_left));
                }else{
                    mSearchView.setNavigationDrawable(ContextCompat
                            .getDrawable(SearchActivity.this, R.drawable.ic_action_arrow_right));
                }
            }

            @Override
            public void onSearchClosed() {
                mSearchView.setNavigationDrawable(null);
            }

            @Override
            public void onSearchCleared() {
                mSearchView.getSearchMenu()
                        .removeSearchMenuItem(mSearchView.getSearchMenu().getSearchMenuItem(1));
            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                if(mSearchView.getSearchMenu().getSearchMenuItem(1) != null) {
                    mSearchView.getSearchMenu().getSearchMenuItem(1)
                            .setTitle(term.toString());
                }else{
                    mSearchView.getSearchMenu().addSearchMenuItem(1, term.toString(), 1)
                            .setIcon(R.drawable.ic_history);
                }
            }

            @Override
            public void onSearch(CharSequence text) {
                Snackbar.make(mSearchView, "Searched for \'" + text + "\'", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
        mSearchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
                if(mSearchView.isSearchOpen()){
                    mSearchView.closeSearch();
                }
            }

            @Override
            public void OnEndIconClick() {
                startVoiceRecognition();
            }
        });

        mSearchView.setOnMenuItemClickListener(new PersistentSearchView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(SearchMenuItem item) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }else{
            super.onBackPressed();
        }
    }

    public void buttonClick(View view){
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.populateSearchText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startVoiceRecognition() {
        if (mMicEnabled) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    this.getString(R.string.speak_now));
            startActivityForResult(intent, VOICE_RECOGNITION_CODE);
        }
    }

    private boolean isIntentAvailable(Intent intent) {
        PackageManager mgr = getPackageManager();
        if (mgr != null) {
            List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
