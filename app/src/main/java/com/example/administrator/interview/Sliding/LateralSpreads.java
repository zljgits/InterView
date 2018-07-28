package com.example.administrator.interview.Sliding;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 侧滑类
 * Created by Administrator on 2018/1/22 0022.
 */

public class LateralSpreads extends SlidingPaneLayout {
    private float fx;
    private float fy;
    private float distance;

    public LateralSpreads(Context context) {
        this(context, null);
    }

    public LateralSpreads(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LateralSpreads(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        distance = viewConfiguration.getScaledEdgeSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                fx = ev.getX();
                fy = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                if (fx > distance && !isOpen() && canScroll(this, false, Math.round(x - fx), Math.round(x), Math.round(y))) {
                    MotionEvent motionEvent = MotionEvent.obtain(ev);
                    motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onInterceptTouchEvent(motionEvent);
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
