package com.ejemplo.appdocente.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;

import com.ejemplo.appdocente.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.TextForm;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

public class Tooltip {

    public void createTooltip(Typeface typeface, Context context, View view, String text, float width, int xoff, int yoff, LifecycleOwner viewLifecycleOwner) {

        TextForm textForm = new TextForm.Builder(context)
                .setText(text)
                .setTextColorResource(R.color.design_dark_default_color_on_background)
                .setTextSize(14f)
                .setTextTypeface(typeface)
                .build();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Balloon balloon = new Balloon.Builder(context)
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.RIGHT)
                        .setArrowVisible(true)
                        .setWidthRatio(width)
                        .setHeight(65)
                        .setTextSize(15f)
                        .setArrowPosition(0.5f)
                        .setCornerRadius(5f)
                        .setAlpha(0.9f)
                        .setTextForm(textForm)
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.design_blue_mp_dark))
                        .setBalloonAnimation(BalloonAnimation.ELASTIC)
                        .setLifecycleOwner(viewLifecycleOwner)
                        .build();

                balloon.showAlignBottom(view, xoff, yoff); // shows the balloon on an anchor view as the left alignment.
                balloon.dismissWithDelay(4000L);
            }
        }, 1000);
    }
}
