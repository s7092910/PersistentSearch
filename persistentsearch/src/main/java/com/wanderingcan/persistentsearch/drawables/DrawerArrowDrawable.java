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

package com.wanderingcan.persistentsearch.drawables;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.FloatRange;

/**
 * A drawable that can draw a "Drawer hamburger" menu or an arrow and animate between them.
 * <p>
 * The progress between the two states is controlled via {@link #setPosition(float)}.
 * </p>
 */
public class DrawerArrowDrawable extends android.support.v7.graphics.drawable.DrawerArrowDrawable {

    /**
     * The drawable is shown as a Hamburger
     */
    public static final float HAMBURGER = 0f;

    /**
     * The drawable is shown as an Arrow
     */
    public static final float ARROW = 1f;

    private ValueAnimator mToArrow;
    private ValueAnimator mToHam;

    private float mPosition;

    /**
     * @param context used to get the configuration for the drawable from
     */
    public DrawerArrowDrawable(Context context) {
        super(context);
        super.setSpinEnabled(true);

        mToArrow = ObjectAnimator.ofFloat(this, "position", HAMBURGER, ARROW);

        mToHam = ObjectAnimator.ofFloat(this, "position", ARROW, HAMBURGER);

        mPosition = HAMBURGER;
    }

    /**
     * Starts the animation between drawing a "Drawer hamburger" menu or an arrow, depending
     * on what is being currently drawn.
     */
    public void toggle(){
        if(mPosition == HAMBURGER){
            animateToArrow();
        }

        if(mPosition == ARROW){
            animateToHamburger();
        }
    }

    /**
     * Animates the drawable to drawing an arrow
     */
    public void animateToArrow(){
        mToHam.cancel();
        mToArrow.start();
    }

    /**
     * Animates the drawable to drawing a "Drawer hamburger" menu
     */
    public void animateToHamburger(){
        mToArrow.cancel();
        mToHam.start();
    }

    /**
     * Sets the length of the animation. The default duration is 300 milliseconds.
     *
     * @param duration The length of the animation, in milliseconds. This value cannot
     * be negative.
     */
    public void setDuration(long duration){
        if (duration < 0) {
            throw new IllegalArgumentException("Animations cannot have negative duration: " +
                    duration);
        }
        mToArrow.setDuration(duration);
        mToHam.setDuration(duration);
    }

    /**
     * Sets the position of the animation, with 0 drawing an "Drawer hamburger" menu and 1 drawing
     * an arrow
     * @param position The position of the animation
     */
    public void setPosition(@FloatRange(from = 0.0, to = 1.0) float position) {
        if (position == ARROW) {
            mPosition = ARROW;
            setVerticalMirror(true);
        } else if (position == HAMBURGER) {
            mPosition = HAMBURGER;
            setVerticalMirror(false);
        }
        super.setProgress(position);
    }

    /**
     * Returns the current progress of the arrow.
     */
    public float getPosition(){
        return super.getProgress();
    }

}
