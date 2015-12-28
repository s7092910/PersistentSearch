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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanderingcan.widget.R;

/**
 * A Persistent Search Bar that uses Material Design, very much like the Search Bar in the Google Now App
 * and the Google Play Store.
 */
public class PersistentSearchView extends CardView{

    private static final String TAG = "PersistentSearchView";
    private static final String EMPTY = "";

    private ImageButton mNavIcon;
    private ImageButton mEndIcon;
    private EditText mSearchText;
    private SearchMenuView mSearchMenuView;
    private View mDivider;

    private SearchMenuPresenter mPresenter;

    private CharSequence mHint;
    private boolean mHintAlwaysVisible;
    private boolean mHintVisible;
    private boolean mShowClearDrawable;
    private boolean mShowMenu;

    private boolean mOpened;

    private OnSearchListener mSearchListener;
    private OnIconClickListener mIconListener;
    private OnMenuItemClickListener mMenuListener;

    private Drawable mEndDrawable;
    private Drawable mClearDrawable;

    private int mTextMargin;
    private int mImageMargin;

    public PersistentSearchView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public PersistentSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public PersistentSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("deprecation")
    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mNavIcon = new ImageButton(context);
        mEndIcon = new ImageButton(context);
        mSearchText = new EditText(context);
        mSearchMenuView = new SearchMenuView(context);
        mDivider = new View(context);
        mDivider.setVisibility(GONE);

        mPresenter = new SearchMenuPresenter(context, new SearchMenuListener());

        mSearchMenuView.setAdapter(mPresenter.getAdapter());
        mSearchMenuView.addItemDecoration(new DividerItemDecoration(context, attrs));

        int[] attr = { android.R.attr.listDivider };
        TypedArray ta = context.obtainStyledAttributes(attr);
        Drawable divider = ta.getDrawable(0);
        ta.recycle();

        mOpened = false;
        mShowClearDrawable = false;
        mShowMenu = true;

        //Set up CardView
        setUseCompatPadding(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        //Set up TextView
        if(Build.VERSION.SDK_INT >= 16) {
            mSearchText.setBackground(null);
            mNavIcon.setBackground(null);
            mEndIcon.setBackground(null);
            mDivider.setBackground(divider);
        }else {
            mSearchText.setBackgroundDrawable(null);
            mNavIcon.setBackgroundDrawable(null);
            mEndIcon.setBackgroundDrawable(null);
            mDivider.setBackgroundDrawable(divider);
        }
        mSearchText.setSingleLine();
        mHintVisible = false;
        mSearchText.setOnFocusChangeListener(new SearchFocusListener());
        mSearchText.setOnEditorActionListener(new EditTextEditorAction());
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchText.addTextChangedListener(new EditTextTextWatcher());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PersistentSearchView,
                defStyleAttr, 0);
        Drawable drawable = a.getDrawable(R.styleable.PersistentSearchView_navSrc);
        setNavigationDrawable(drawable);
        mNavIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mNavIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIconListener != null){
                    mIconListener.OnNavigationIconClick();
                }
            }
        });

        drawable = a.getDrawable(R.styleable.PersistentSearchView_endSrc);
        setEndDrawable(drawable);
        mClearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_action_cancel);
        mEndIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mEndIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mShowClearDrawable) {
                    //Clears the text
                    mSearchText.setText(EMPTY);
                }else{
                    if(mIconListener != null){
                        mIconListener.OnEndIconClick();
                    }
                }
            }
        });

        mHint = a.getText(R.styleable.PersistentSearchView_hint);
        mHintAlwaysVisible = a.getBoolean(R.styleable.PersistentSearchView_hintAlwaysActive, false);
        if(mHintAlwaysVisible){
            mSearchText.setHint(mHint);
        }
        a.recycle();

        Resources res = context.getResources();
        int imageDimen = res.getDimensionPixelSize(R.dimen.image_dimen);
        mImageMargin = res.getDimensionPixelSize(R.dimen.image_side_margin);
        int imageTopMargin = res.getDimensionPixelSize(R.dimen.image_top_margin);

        //Sets all of the locations of the views
        CardView.LayoutParams lpNav = generateDefaultLayoutParams();
        lpNav.width = lpNav.height = imageDimen;

        CardView.LayoutParams lpEnd = (LayoutParams) generateLayoutParams(lpNav);

        lpNav.gravity = Gravity.START;
        lpEnd.gravity = Gravity.END;

        if(Build.VERSION.SDK_INT >= 17) {
            mNavIcon.setPaddingRelative(mImageMargin, imageTopMargin, mImageMargin/2, imageTopMargin);
            mEndIcon.setPaddingRelative(mImageMargin/2, imageTopMargin, mImageMargin, imageTopMargin);
        }else {
            mNavIcon.setPadding(mImageMargin, imageTopMargin, mImageMargin/2, imageTopMargin);
            mEndIcon.setPadding(mImageMargin/2, imageTopMargin, mImageMargin, imageTopMargin);
        }

        CardView.LayoutParams lpText = generateDefaultLayoutParams();
        lpText.gravity = Gravity.TOP;
        lpText.height = lpNav.topMargin + lpNav.height;
        mTextMargin = res.getDimensionPixelSize(R.dimen.text_margin);
        if(Build.VERSION.SDK_INT >= 17) {
            lpText.setMarginStart(mTextMargin);
            lpText.setMarginEnd(mTextMargin);
        }else {
            lpText.leftMargin = lpText.rightMargin = mTextMargin;
        }
        setupSearchTextMargin(lpText);

        CardView.LayoutParams lpMenu = generateDefaultLayoutParams();
        lpMenu.topMargin = lpNav.topMargin + lpNav.height;

        CardView.LayoutParams lpDivider = generateDefaultLayoutParams();
        if (divider != null) {
            lpDivider.height = divider.getIntrinsicHeight();
        }
        lpDivider.topMargin = lpNav.topMargin + lpNav.height;

        //Adds the views to the PersistentSearchView
        addView(mNavIcon, lpNav);
        addView(mEndIcon, lpEnd);
        addView(mSearchText, lpText);
        addView(mSearchMenuView, lpMenu);
        addView(mDivider, lpDivider);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int minHeight = getResources().getDimensionPixelSize(R.dimen.min_height);
        if(mShowMenu && isSearchOpen()){
            heightMode = MeasureSpec.UNSPECIFIED;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(minHeight, heightMode);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Sets the OnSearchListener for the PersistentSearchView
     * @see OnSearchListener
     * @param listener The listener to set for the View
     */
    public void setOnSearchListener(OnSearchListener listener){
        mSearchListener = listener;
    }

    /**
     * Sets the OnIconClickListener for the PersistentSearchView
     * @see OnIconClickListener
     * @param listener The listener to set for the View
     */
    public void setOnIconClickListener(OnIconClickListener listener){
        mIconListener = listener;
    }

    /**
     * Sets the OnMenuItemClickListener for the PersistentSearchView
     * @see OnMenuItemClickListener
     * @param listener The listener to set for the View
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener){
        mMenuListener = listener;
    }

    /**
     * Populates the Text on the PersistentSearchView
     * @param text The text to set
     */
    public void populateSearchText(CharSequence text){
        mSearchText.setText(text);
        mSearchText.setSelection(mSearchText.getText().length());
    }

    /**
     * Sets if the SearchMenu should be shown or not, even if there are SearchMenuItems to be shown
     * @param showMenu True if the SearchMenu should be shown, false otherwise
     */
    public void setShowSearchMenu(boolean showMenu){
        mShowMenu = showMenu;
    }

    /**
     * Returns if the SearchMenu will be shown or not
     * @return True if the SearchMenu will be shown, false otherwise
     */
    public boolean showSearchMenu(){
        return mShowMenu;
    }

    /**
     * Returns if the PersistentSearchView is currently open or not
     * @return True if the PersistentSearchView
     */
    public boolean isSearchOpen(){
        return mOpened;
    }

    /**
     * Opens the Search in the PersistentSearchView
     */
    public void openSearch(){
        mOpened = true;
        mSearchText.requestFocus();
        if(mSearchListener != null){
            mSearchListener.onSearchOpened();
        }

        if(mShowMenu){
            mSearchMenuView.setVisibility(VISIBLE);
            mDivider.setVisibility(VISIBLE);
        }
        requestLayout();
    }

    /**
     * Closes the Search in the PersistentSearchView
     */
    public void closeSearch(){
        mOpened = false;
        requestFocus();
        if(mSearchListener != null){
            mSearchListener.onSearchClosed();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(),
                0);

        if(mSearchMenuView.getVisibility() == VISIBLE) {
            mSearchMenuView.setVisibility(GONE);
            mDivider.setVisibility(GONE);
        }
        requestLayout();
        mPresenter.getMenu().clearItems();
    }

    /**
     * Sets the Bitmap for the Navigation Icon
     * @param bitmap The Bitmap for the Navigation Icon
     */
    public void setNavigationBitmap(Bitmap bitmap){
        if(bitmap == null){
            setNavigationDrawable(null);
        }else{
            setNavigationDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    /**
     * Sets the Drawable for the Navigation Icon
     * @param drawable The Drawable for the Navigation Icon
     */
    public void setNavigationDrawable(Drawable drawable){
        if(drawable == null && mNavIcon.getVisibility() == VISIBLE){
            mNavIcon.setVisibility(GONE);
            if(mSearchText.getLayoutParams() != null){
                mSearchText.setLayoutParams(setupSearchTextMargin((LayoutParams) mSearchText.getLayoutParams()));
            }
            return;
        }

        if(mNavIcon.getVisibility() == GONE){
            mNavIcon.setVisibility(VISIBLE);
            if(mSearchText.getLayoutParams() != null){
                mSearchText.setLayoutParams(setupSearchTextMargin((LayoutParams) mSearchText.getLayoutParams()));
                mSearchText.requestLayout();
            }
        }
        mNavIcon.setImageDrawable(drawable);
    }

    /**
     * Gets the Drawable for the Navigation Icon
     * @return The Drawable for the Navigation Icon
     */
    public Drawable getNavigationDrawable(){
        return mNavIcon.getDrawable();
    }

    /**
     * Sets the Bitmap for the End Icon
     * @param bitmap The Bitmap for the End Icon
     */
    public void setEndBitmap(Bitmap bitmap){
        if(bitmap == null){
            setEndDrawable(null);
        }else{
            setEndDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    /**
     * Sets the Drawable for the End Icon
     * @param drawable The Drawable for the End Icon
     */
    public void setEndDrawable(Drawable drawable){
        if(drawable == null){
            mEndDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_action_mic);
        }else{
            mEndDrawable = drawable;
        }
        if(!mShowClearDrawable) {
            mEndIcon.setImageDrawable(mEndDrawable);
        }
    }

    public Drawable getEndDrawable(){
        return mEndDrawable;
    }

    /**
     * Sets the hint for the PersistentSearchView
     * @param hint The text to set for the hint
     */
    public void setHint(CharSequence hint){
        mHint = hint;
        if(mHintAlwaysVisible || mHintVisible){
            mSearchText.setHint(hint);
        }
    }

    /**
     * Sets the hint for the PersistentSearchView
     * @param stringRes The String Res to set for the hint
     */
    public void setHint(@StringRes int stringRes){
        setHint(getResources().getString(stringRes));
    }

    private LayoutParams setupSearchTextMargin(LayoutParams lp){
        if(Build.VERSION.SDK_INT >= 17) {
            if (mNavIcon.getVisibility() == VISIBLE) {
                lp.setMarginStart(mTextMargin);
            } else {
                lp.setMarginStart(mImageMargin);
            }
        }else{
            if (mNavIcon.getVisibility() == VISIBLE) {
                lp.leftMargin = mTextMargin;
            } else {
                lp.leftMargin = mImageMargin;
            }
        }
        return lp;
    }

    /**
     * Gets the SearchMenu for the PersistentSearchView
     * @return The SearchMenu for the PersistentSearchView
     */
    public SearchMenu getSearchMenu(){
        return mPresenter.getMenu();
    }

    public interface OnSearchListener {
        /**
         * Called when the searchbox is opened
         */
        void onSearchOpened();

        /**
         * Called when the searchbox is closed
         */
        void onSearchClosed();

        /**
         * Called when the searchbox has been cleared by the user
         */
        void onSearchCleared();

        /**
         * Called when the searchbox's edittext changes
         */
        void onSearchTermChanged(CharSequence term);

        /**
         * Called when a search happens, with a the given text
         * @param text The text in the PersistentSearchView when the user requests a search
         */
        void onSearch(CharSequence text);

    }

    public interface OnMenuItemClickListener {
        /**
         * Called when a SearchMenuItem is clicked
         * @param item The SearchMenuItem that is clicked
         */
        void onMenuItemClick(SearchMenuItem item);
    }

    public interface OnIconClickListener {
        /**
         * Called when the Navigation Icon is clicked
         */
        void OnNavigationIconClick();

        /**
         * Called when the End Icon is clicked
         */
        void OnEndIconClick();
    }

    private class SearchMenuListener implements SearchMenuPresenter.SearchMenuItemListener{

        @Override
        public void onItemClick(SearchMenuItem item) {
            if(mMenuListener != null){
                mMenuListener.onMenuItemClick(item);
            }
        }

        @Override
        public void onActionClick(SearchMenuItem item, boolean defaultAction) {
            if(defaultAction){
                populateSearchText(item.getTitle());
            }
        }
    }

    private class EditTextTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(TextUtils.isEmpty(s)){
                mShowClearDrawable = false;
                mEndIcon.setImageDrawable(mEndDrawable);
            }else{
                mShowClearDrawable = true;
                if(!mEndIcon.getDrawable().equals(mClearDrawable)) {
                    mEndIcon.setImageDrawable(mClearDrawable);
                }
            }

            if(mSearchListener != null && !TextUtils.isEmpty(s)){
                mSearchListener.onSearchTermChanged(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(TextUtils.isEmpty(s)){
                if(mSearchListener != null){
                    mSearchListener.onSearchCleared();
                }
            }
        }
    }

    private class EditTextEditorAction implements EditText.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if(mSearchListener != null){
                    mSearchListener.onSearch(v.getText());
                }
                closeSearch();
                return true;
            }
            return false;
        }
    }

    private class SearchFocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && !mOpened){
                openSearch();
            }

            if(!mHintAlwaysVisible){
                if(hasFocus){
                    mSearchText.setHint(mHint);
                }else{
                    mSearchText.setHint(EMPTY);
                }
            }
        }
    }
}
