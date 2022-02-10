package com.example.artify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

public class GestureDetectGridView extends GridView {
    private GestureDetector gDetector;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;

    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;


    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int action = e.getActionMasked();
                if(action == MotionEvent.ACTION_DOWN) {
                    return false;
                } else {
                    int position = GestureDetectGridView.this.pointToPosition(Math.round(e.getX()), Math.round(e.getY()));
                    if(position == -1)
                        position = 0;
                    Puzzlegame.getInstance().rotate(context, position);
                    return true;
                }
            }



            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final int position = GestureDetectGridView.this.pointToPosition(Math.round(e1.getX()), Math.round(e1.getY()));

                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                        //Swipe Upwards
                        Puzzlegame.getInstance().moveTiles(context, Puzzlegame.UP, position);
                    } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                        //Swipe Downwards
                        Puzzlegame.getInstance().moveTiles(context, Puzzlegame.DOWN, position);
                    }
                } else {
                    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                        //Swipe Leftwards
                        Puzzlegame.getInstance().moveTiles(context, Puzzlegame.LEFT, position);
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                        //Swipe Rightwards
                        Puzzlegame.getInstance().moveTiles(context, Puzzlegame.RIGHT, position);
                    }
                }


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        }else if(action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        }else{
            if(mFlingConfirmed) {
                return false;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();

        float dX = (Math.abs(ev.getX() - mTouchX));
        float dY = (Math.abs(ev.getY() - mTouchY));
        if((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
            mFlingConfirmed = true;
            return false;
        }

        if(action == MotionEvent.ACTION_UP) {
            return false;
        }
        return gDetector.onTouchEvent(ev);
    }

}
