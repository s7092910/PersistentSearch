/*
 * Copyright 2015 Christopher Beda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanderingcan.persistentsearch;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The SearchMenu that creates and holds the SearchMenuItems
 */
public class SearchMenu {
    private static final String TAG = "SearchMenu";
    private static final int DEFAULT_SIZE = 5;

    private ArrayList<SearchMenuItem> mItems;
    private SearchMenuPresenter mPresenter;

    private SearchMenu(){
        //Private Constructor
    }

    protected SearchMenu(SearchMenuPresenter presenter){
        mPresenter = presenter;
        mItems = new ArrayList<>();
    }

    protected ArrayList<SearchMenuItem> getItems(){
        return mItems;
    }

    protected int getCount(){
        return mItems.size();
    }

    /**
     * Gets the max size of the SearchMenu
     * @return The max size of the SearchMenu
     */
    public int maxSearchMenuItems(){
        return DEFAULT_SIZE;
    }

    /**
     * Adds a SearchMenuItem to the menu and adds it to the end of the menu
     * @param id The id of the SearchMenuItem
     * @return Return the new SearchMenuItem, null if there is no more room in the search menu
     */
    public SearchMenuItem addSearchMenuItem(int id){
        return internalAddSearchMenuItem(id, null, Integer.MAX_VALUE);
    }

    /**
     * Adds a SearchMenuItem to the menu and adds it to the end of the menu
     * @param id The id of the SearchMenuItem
     * @param title The title of the SearchMenuItem
     * @return Return the new SearchMenuItem, null if there is no more room in the search menu
     */
    public SearchMenuItem addSearchMenuItem(int id, String title){
        return internalAddSearchMenuItem(id, title, Integer.MAX_VALUE);
    }

    /**
     * Adds a SearchMenuItem to the menu
     * @param id The id of the SearchMenuItem
     * @param order The order to display the SearchMenuItem in the SearchMenu
     * @return Return the new SearchMenuItem, null if there is no more room in the search menu
     */
    public SearchMenuItem addSearchMenuItem(int id, int order){
        return internalAddSearchMenuItem(id, null, order);
    }

    /**
     * Adds a SearchMenuItem to the menu
     * @param id The id of the SearchMenuItem
     * @param title The title of the SearchMenuItem
     * @param order The order to display the SearchMenuItem in the SearchMenu
     * @return Return the new SearchMenuItem, null if there is no more room in the search menu
     */
    public SearchMenuItem addSearchMenuItem(int id, String title, int order){
        return internalAddSearchMenuItem(id, title, order);
    }

    private SearchMenuItem internalAddSearchMenuItem(int id, String title, int order){
        if( mItems.size() < DEFAULT_SIZE){
            SearchMenuItem item = new SearchMenuItem(mPresenter, id, order, title);
            mItems.add(item);
            Collections.sort(mItems);
            int index = mItems.indexOf(item);
            mPresenter.getAdapter().notifyItemInserted(index);
            return item;
        }
        return null;
    }

    /**
     * Get the SearchMenuItem with the given id
     * @param id The id of the SearchMenuItem
     * @return The found SearchMenuItem or null if not found
     */
    public SearchMenuItem getSearchMenuItem(int id){
        for(SearchMenuItem item: mItems){
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }

    /**
     * Get the SearchMenuItem with the given title
     * @param title The title of the SearchMenuItem
     * @return The found SearchMenuItem or null if not found
     */
    public SearchMenuItem getSearchMenuItem(String title){
        for(SearchMenuItem item: mItems){
            if(item.getTitle().equals(title)){
                return item;
            }
        }
        return null;
    }

    /**
     * Removes the given SearchMenuItem from the SearchMenu
     * @param item The SearchMenuItem to remove
     * @return true if the menu is modified, false otherwise
     */
    public boolean removeSearchMenuItem(SearchMenuItem item){
        int index = mItems.indexOf(item);
        if(index != -1){
            mItems.remove(item);
            mPresenter.getAdapter().notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    /**
     * Clears all the SearchMenuItems in the SearchMenu
     */
    public void clearItems(){
        mItems.clear();
        mPresenter.getAdapter().notifyDataSetChanged();
    }
}
