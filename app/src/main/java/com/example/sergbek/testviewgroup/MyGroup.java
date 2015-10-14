package com.example.sergbek.testviewgroup;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;


public class MyGroup extends ViewGroup {

    private float sweepAngle;

    private CentralCircle centralCircle;
    private int mRadius;
    private int width;
    private int height;
    private Paint mPMainCircle;

    private int mBarabanRotation;

    private ObjectAnimator mAutoCenterAnimator;
    private Scroller mScroller;
    private ValueAnimator mScrollAnimator;
    private GestureDetector mDetector;

    private static final int DESIRED_WIDTH = 150;
    private static final int DESIRED_HEIGHT = 150;

    public static final int DEG_CIRCLE = 360;
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    public static final int AUTO_CENTER_ANIM_DURATION = 350;

    public MyGroup(Context context) {
        super(context);
        init();
    }

    public MyGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setWillNotDraw(false);
        mPMainCircle = new Paint();
        mPMainCircle.setShader(new LinearGradient(0, 100, 100, 0, Color.parseColor("#FFF04C08"),
                Color.parseColor("#FFE18D68"), Shader.TileMode.MIRROR));
        mPMainCircle.setColor(Color.parseColor("#FF574153"));
        mPMainCircle.setAntiAlias(true);

        mScroller = new Scroller(getContext(), null, true);

        mScrollAnimator = ValueAnimator.ofFloat(0, 1);
        mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tickScrollAnimation();
            }
        });

        mAutoCenterAnimator = ObjectAnimator.ofInt(getContext(), "BarabanRotation", 0);
        mDetector = new GestureDetector(getContext(), new GestureListener());
        mDetector.setIsLongpressEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        centralCircle = new CentralCircle(getContext());
        addView(centralCircle);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int count = getChildCount();
        sweepAngle = (float) DEG_CIRCLE / (count - 1);

        for (int i = 0; i < count - 1; i++) {
            final ArcView child = (ArcView) getChildAt(i);
            child.setSweepAngle(sweepAngle);
            child.setRadius((mRadius / 2) - 5);
            child.setCentX(width);
            child.setCentY(height);
            child.setCount(i);
//            final int width = child.getMeasuredWidth();
//            final int height = child.getMeasuredHeight();

            child.layout(left, top, right, bottom);
//            child.setRotation(sweepAngle * i);
        }


        centralCircle.setRadius(mRadius / 2);
        centralCircle.setCentX(width);
        centralCircle.setCentY(height);
        centralCircle.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mCenterX = width / 2;
        int mCenterY = height / 2;

        canvas.drawCircle(mCenterX, mCenterY, mRadius / 2, mPMainCircle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            mRadius = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            width = DESIRED_WIDTH;
        }

        mRadius = width;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            height = DESIRED_HEIGHT;
        }

        if (height < width)
            mRadius = height;

        setMeasuredDimension(width, height);

//        int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }



    public int getBarabanRotation() {
        return mBarabanRotation;
    }



    public void setBarabanRotation(int rotation) {
        rotation = (rotation % DEG_CIRCLE + DEG_CIRCLE) % DEG_CIRCLE;
        mBarabanRotation = rotation;
//        setRotation(rotation);

        int count = getChildCount();

        for (int i = 0; i < count-1; i++) {
            final ArcView child = (ArcView) getChildAt(i);
            child.setRotation(rotation);
        }

    }

    private void tickScrollAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.computeScrollOffset();
            setBarabanRotation(mScroller.getCurrY());
        } else {
            mScrollAnimator.cancel();
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            float scrollTheta = Utils.vectorToScalarScroll(
                    distanceX,
                    distanceY,
                    e2.getX() - (width / 2),
                    e2.getY() - (height / 2));
            setBarabanRotation(getBarabanRotation() - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE);

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float scrollTheta = Utils.vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - width / 2,
                    e2.getY() - height / 2);
            mScroller.fling(
                    0,
                    getBarabanRotation(),
                    0,
                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                    0,
                    0,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE);

            mScrollAnimator.setDuration(mScroller.getDuration());
            mScrollAnimator.start();

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mAutoCenterAnimator.cancel();

//            getRootView().invalidate();
            return true;
        }

    }
}