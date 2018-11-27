package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.frizzl.app.frizzleapp.SwipeDirection;

public class TaskViewPager extends ViewPager {
    private SwipeDirection direction;
    private boolean enabled;
    private float initialXValue;

    public TaskViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        this.direction = SwipeDirection.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(event) && super.onTouchEvent(event);

    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(this.direction == SwipeDirection.all) return true;

        if(direction == SwipeDirection.none )//disable any swipe
            return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && direction == SwipeDirection.left ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(event) && super.onInterceptTouchEvent(event);

    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}