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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.TintManager;

/**
 * A Class that holds the Title and Icons for the SearchMenu that is part of the PresistentSearchView
 */
public class SearchMenuItem implements Comparable<SearchMenuItem>{
    /** Used for the icon resource ID if this item does not have an icon */
    public static final int NO_ICON = 0;

    private final int mId;
    private int mOrdering;

    private Drawable mIconDrawable;
    private int mIconResId = NO_ICON;

    private String mTitle;

    private Drawable mActionDrawable;
    private int mActionResId = NO_ICON;
    private boolean mDefaultAction;

    private SearchMenuPresenter mMenu;

    private SearchMenuItem(){
        //Private Constructor
        mId = 0;
    }

    /**
     * Constructor for SearchMenuItem
     * @param presenter The Presenter for the SearchMenu
     * @param id The Id for SearchMenuItem
     * @param order The Ordering for the SearchMenu
     * @param title The Title to show for the Item
     */
    SearchMenuItem(SearchMenuPresenter presenter, int id, int order, String title){
        mMenu = presenter;
        mId = id;
        mOrdering = order;
        mTitle = title;
        mActionDrawable = ContextCompat.getDrawable(mMenu.getContext(), R.drawable.ic_action_arrow);
        mDefaultAction = true;
    }

    /**
     * Returns if the Action Icon is the default Action
     * @return True if the Action Icon is the default Action, false otherwise
     */
    protected boolean isDefaultAction(){
        return mDefaultAction;
    }

    /**
     * Gets the Id of the SearchMenuItem
     * @return The SearchMenuItem id
     */
    public int getId(){
        return mId;
    }

    /**
     * Gets the Order to display the SearchMenuItem in the SearchMenu
     * @return The Order to display the item in the menu
     */
    public int getOrdering(){
        return mOrdering;
    }

    /**
     * Sets the Order to display the SearchMenuItem in the SearchMenu
     * @param order The order to display the item in the menu
     */
    public void setOrdering(int order){
        mOrdering = order;
    }

    /**
     * Gets the Title to display in the SearchMenu for the SearchMenuItem
     * @return The Title to display for the item in the menu
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * Sets the Title to display for the item in the menu
     * @param title The Title to display for the item in the menu
     */
    public SearchMenuItem setTitle(String title){
        title = title.trim();
        mTitle = title;
        int index = mMenu.getMenu().getItems().indexOf(this);
        mMenu.getAdapter().notifyItemChanged(index);
        return this;
    }

    /**
     * Sets the Title to display for the item in the menu
     * @param resId The String Resource Id for the Title to display for the item in the menu
     */
    public SearchMenuItem setTitle(@StringRes int resId){
        Context context = mMenu.getContext();
        String title = context.getString(resId);
        return setTitle(title);
    }

    /**
     * Gets the action icon, loads the icon if it is not loaded.
     * @return Drawable for the action icon
     */
    public Drawable getActionIcon() {
        if (mActionDrawable != null) {
            return mActionDrawable;
        }

        if (mActionResId != NO_ICON) {
            Drawable icon = TintManager.getDrawable(mMenu.getContext(), mActionResId);
            mActionResId = NO_ICON;
            mActionDrawable = icon;
            return icon;
        }

        return null;
    }

    /**
     * Sets the drawable for the action icon for the SearchMenuItem
     * @param icon The drawable for the action icon
     */
    public SearchMenuItem setActionIcon(Drawable icon) {
        mDefaultAction = false;
        mActionResId = NO_ICON;
        mActionDrawable = icon;
        int index = mMenu.getMenu().getItems().indexOf(this);
        mMenu.getAdapter().notifyItemChanged(index);
        return this;
    }

    /**
     * Sets the ResourceId for the action icon for the SearchMenuItem
     * @param iconResId The resourceId for the action icon
     */
    public SearchMenuItem setActionIcon(@DrawableRes int iconResId) {
        mDefaultAction = false;
        mActionDrawable = null;
        mActionResId = iconResId;
        int index = mMenu.getMenu().getItems().indexOf(this);
        mMenu.getAdapter().notifyItemChanged(index);
        return this;
    }

    /**
     * Gets the icon, loads the icon if it is not loaded.
     * @return Drawable for the icon
     */
    public Drawable getIcon() {
        if (mIconDrawable != null) {
            return mIconDrawable;
        }

        if (mIconResId != NO_ICON) {
            Drawable icon = TintManager.getDrawable(mMenu.getContext(), mIconResId);
            mIconResId = NO_ICON;
            mIconDrawable = icon;
            return icon;
        }

        return null;
    }

    /**
     * Sets the drawable for the icon for the SearchMenuItem
     * @param icon The drawable for the icon
     */
    public SearchMenuItem setIcon(Drawable icon) {
        mIconResId = NO_ICON;
        mIconDrawable = icon;
        int index = mMenu.getMenu().getItems().indexOf(this);
        mMenu.getAdapter().notifyItemChanged(index);
        return this;
    }

    /**
     * Sets the ResourceId for the icon for the SearchMenuItem
     * @param iconResId The resourceId for the icon
     */
    public SearchMenuItem setIcon(@DrawableRes int iconResId) {
        mIconDrawable = null;
        mIconResId = iconResId;
        int index = mMenu.getMenu().getItems().indexOf(this);
        mMenu.getAdapter().notifyItemChanged(index);
        return this;
    }

    @Override
    public String toString() {
        return "SearchMenuItem{" +
                "\nId = " + mId +
                "\nTitle = '" + mTitle + '\'' +
                "\nOrdering = " + mOrdering +
                '}';
    }

    @Override
    public int compareTo(@NonNull SearchMenuItem another) {
        return mOrdering - another.mOrdering;
    }
}
