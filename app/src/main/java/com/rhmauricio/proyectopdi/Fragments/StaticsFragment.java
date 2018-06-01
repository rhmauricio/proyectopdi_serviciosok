package com.rhmauricio.proyectopdi.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.rhmauricio.proyectopdi.R;


public class StaticsFragment extends Fragment {

    private ViewFlipper mViewFlipper;

    private Context mContext;

    private float initialX;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statics, container, false);

        mContext = getContext();

        mViewFlipper = view.findViewById(R.id.view_flipper);

        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));

        mViewFlipper.setOnTouchListener(touchListener);

        return view;
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = event.getX();
                    return true;
                case MotionEvent.ACTION_UP:
                    float finalX = event.getX();
                    if (initialX > finalX) {
                        mViewFlipper.showNext();
                    } else {
                        mViewFlipper.showPrevious();
                    }
                    return true;
            }
            return false;
        }
    };


}
