package com.wanderingcan.persistentsearch.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private PersistentSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (PersistentSearchView) findViewById(R.id.search_bar);
        searchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
                searchView.getSearchMenu().addSearchMenuItem(2, "Suggestion")
                        .setIcon(R.drawable.ic_globe);
                if(!ViewUtils.isLayoutRtl(searchView)) {
                    searchView.setNavigationDrawable(ContextCompat
                            .getDrawable(MainActivity.this, R.drawable.ic_action_arrow_left));
                }else{
                    searchView.setNavigationDrawable(ContextCompat
                            .getDrawable(MainActivity.this, R.drawable.ic_action_arrow_right));
                }
            }

            @Override
            public void onSearchClosed() {
                searchView.setNavigationDrawable(null);
            }

            @Override
            public void onSearchCleared() {
                searchView.getSearchMenu()
                        .removeSearchMenuItem(searchView.getSearchMenu().getSearchMenuItem(1));
            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                Log.d(TAG, "onSearchTermChanged: ");
                if(searchView.getSearchMenu().getSearchMenuItem(1) != null) {
                    searchView.getSearchMenu().getSearchMenuItem(1)
                            .setTitle(term.toString());
                }else{
                    searchView.getSearchMenu().addSearchMenuItem(1, term.toString(), 1)
                            .setIcon(R.drawable.ic_history);
                }
            }

            @Override
            public void onSearch(CharSequence text) {
                Snackbar.make(searchView, "Searched for \'" + text + "\'", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
        searchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
                if(searchView.isSearchOpen()){
                    searchView.closeSearch();
                }
            }

            @Override
            public void OnEndIconClick() {
            }
        });

        searchView.setOnMenuItemClickListener(new PersistentSearchView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(SearchMenuItem item) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else{
            super.onBackPressed();
        }
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
