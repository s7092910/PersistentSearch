package com.wanderingcan.persistentsearch.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenuItem;
import com.wanderingcan.persistentsearch.drawables.DrawerArrowDrawable;

/**
 * Shows how to use DrawerArrowDrawable with a Drawer and a PersistentSearchView
 */
public class DrawerActivity extends AppCompatActivity {

    private static final String TAG = "DrawerActivity";

    private PersistentSearchView mSearchView;
    private DrawerArrowDrawable mArrowDrawable;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Button button = (Button) findViewById(R.id.button);
        button.setText("Go to Search Example");

        mArrowDrawable = new DrawerArrowDrawable(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mArrowDrawable.setPosition(slideOffset);
            }
        });

        mSearchView = (PersistentSearchView) findViewById(R.id.search_bar);
        mSearchView.setNavigationDrawable(mArrowDrawable);
        mSearchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
                mArrowDrawable.toggle();
                mSearchView.getSearchMenu().addSearchMenuItem(2, "Suggestion")
                        .setIcon(R.drawable.ic_globe);
            }

            @Override
            public void onSearchClosed() {
                mArrowDrawable.toggle();
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
                }else{
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }

            @Override
            public void OnEndIconClick() {
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
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

}
