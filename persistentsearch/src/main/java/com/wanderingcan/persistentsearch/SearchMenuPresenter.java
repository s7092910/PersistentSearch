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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The class that is responsible for presenting the SearchMenu
 */
class SearchMenuPresenter {

    private Context mContext;
    private final SearchMenu mMenu;
    private final SearchMenuItemListener mListener;
    /**
     * The SearchMenuPresenter's Adapter
     */
    final SearchMenuAdapter mAdapter;

    public SearchMenuPresenter(Context context, SearchMenuItemListener listener){
        mContext = context;
        mMenu = new SearchMenu(this);
        mAdapter = new SearchMenuAdapter();
        mListener = listener;
    }

    /**
     * Gets the SearchMenuPresenter's Context
     * @return The Context attached to the SearchMenuPresenter
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Gets the SearchMenuPresenter's Adapter
     * @return The RecyclerView Adapter attached to the SearchMenuPresenter
     */
    @Deprecated
    public SearchMenuAdapter getAdapter(){
        return mAdapter;
    }

    /**
     * Gets the SearchMenuPresenter's Menu
     * @return The Menu attached to the SearchMenuPresenter
     */
    public SearchMenu getMenu(){
        return mMenu;
    }

    public interface SearchMenuItemListener{
        void onItemClick(SearchMenuItem item);
        void onActionClick(SearchMenuItem item, boolean defaultAction);
    }

    protected class SearchMenuAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.content_searchmenu_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SearchMenuItem item = mMenu.getItems().get(position);
            holder.mTitle.setText(item.getTitle());
            holder.mIcon.setImageDrawable(item.getIcon());
            holder.mAction.setImageDrawable(item.getActionIcon());
        }

        @Override
        public int getItemCount() {
            return mMenu.getCount();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mTitle;
        public final ImageView mIcon;
        public final ImageView mAction;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.content_title);
            mIcon = (ImageView) itemView.findViewById(R.id.content_display_icon);
            mAction = (ImageView) itemView.findViewById(R.id.content_action_icon);
            mAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) {
                if (v.equals(mAction)) {
                    SearchMenuItem item = mMenu.getItems().get(getAdapterPosition());
                    mListener.onActionClick(item, item.isDefaultAction());
                }
                if(v.equals(super.itemView)){
                    mListener.onItemClick(mMenu.getItems().get(getAdapterPosition()));
                }
            }
        }
    }
}
