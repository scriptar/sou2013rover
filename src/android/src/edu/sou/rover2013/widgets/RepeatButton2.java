package edu.sou.rover2013.widgets;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * Borrowed Code
 * 
 * A class, that can be used as a TouchListener on any view (e.g. a Button).
 * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
 * click is fired immediately, next after initialInterval, and subsequent after
 * normalInterval.
 *
 * <p>Interval is scheduled after the onClick completes, so it has to run fast.
 * If it runs slow, it does not generate skipped onClicks.
 */
public class RepeatButton2 implements OnTouchListener {

    private Handler handler = new Handler();

    private int initialInterval;
    private final int normalInterval;
    private final OnClickListener clickListener;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            clickListener.onClick(downView);
        }
    };

    private View downView;

    /**
     * @param initialInterval The interval after first click event
     * @param normalInterval The interval after second and subsequent click 
     *       events
     * @param clickListener The OnClickListener, that will be called
     *       periodically
     */
    public RepeatButton2(int initialInterval, int normalInterval, 
            OnClickListener clickListener) {
        if (clickListener == null)
            throw new IllegalArgumentException("null runnable");
        if (initialInterval < 0 || normalInterval < 0)
            throw new IllegalArgumentException("negative interval");

        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
            handler.removeCallbacks(handlerRunnable);
            handler.postDelayed(handlerRunnable, initialInterval);
            downView = view;
            clickListener.onClick(view);
            break;
        case MotionEvent.ACTION_UP:
            handler.removeCallbacks(handlerRunnable);
            downView = null;
            break;
        }
        return false;
    }

}