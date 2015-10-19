package com.example.sergbek.testviewgroup;

import android.animation.Animator;
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

    private int mCenterX;
    private int mCenterY;

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
        setLayerToHW();
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

        mAutoCenterAnimator = ObjectAnimator.ofInt(this, "BarabanRotation", 0);


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

        int sizeArc = mRadius - 5;
        int startAngle = 0;
        for (int i = 0; i < count - 1; i++) {
            final ArcView child = (ArcView) getChildAt(i);
            child.setSweepAngle(sweepAngle);

//            child.setRadius(mRadius);
//            child.setCentX(mCenterX);
//            child.setCentY(mCenterY);
//            child.setCount(i);
            child.setColor(0xFF574153);

            child.setStartAngle(startAngle);
            child.setEndAngle(startAngle + (int) sweepAngle);
            startAngle += sweepAngle;
//            final int width = child.getMeasuredWidth();
//            final int height = child.getMeasuredHeight();

//            child.layout(left, top, right, bottom);
            child.layout(mCenterX - sizeArc, mCenterY - sizeArc, mCenterX + sizeArc, mCenterY + sizeArc);
            child.setRotation(sweepAngle * i);
        }


//        centralCircle.setRadius(mRadius);
//        centralCircle.setCentX(mCenterX);
//        centralCircle.setCentY(mCenterY);
//        centralCircle.layout(left, top, right, bottom);
        centralCircle.layout(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPMainCircle);
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
            mRadius = width / 2;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            width = DESIRED_WIDTH;
        }

        mRadius = width / 2;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            height = DESIRED_HEIGHT;
        }

        if (height < width)
            mRadius = height / 2;

        mCenterX = width / 2;
        mCenterY = height / 2;

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

        return Utils.inCircle(event.getX(), event.getY(), mCenterX, mCenterY, mRadius);
    }


    public int getBarabanRotation() {
        return mBarabanRotation;
    }


    public void setBarabanRotation(int rotation) {
        rotation = (rotation % DEG_CIRCLE + DEG_CIRCLE) % DEG_CIRCLE;
        mBarabanRotation = rotation;
//        setRotation(rotation);

        int count = getChildCount();

        for (int i = 0; i < count - 1; i++) {
            final ArcView child = (ArcView) getChildAt(i);
            child.rotateTo(rotation + (sweepAngle * i));
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
                    e2.getX() - mCenterX,
                    e2.getY() - mCenterY);
            setBarabanRotation(getBarabanRotation() - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE);

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float scrollTheta = Utils.vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - mCenterX,
                    e2.getY() - mCenterY);
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
        public boolean onSingleTapUp(MotionEvent e) {

            double touchAngle = getTouchAngle(e);

            for (int i = 0; i < getChildCount() - 1; i++) {
                final ArcView child = (ArcView) getChildAt(i);
                child.setColor(0xFF574153);
                if (touchAngle > child.getStartAngle() && touchAngle < child.getEndAngle()) {
                    child.setColor(0xDD77DD77);

                    moveItemUp(child);
                }
                child.invalidate();
            }

            return true;
        }



        @Override
        public boolean onDown(MotionEvent e) {
            stopScrolling();
            mAutoCenterAnimator.cancel();

//            getRootView().invalidate();
            return true;
        }

    }

    private void stopScrolling() {
        mScroller.forceFinished(true);
    }

    private double getTouchAngle(MotionEvent e) {
        int xPosition = (int) e.getX();
        int yPosition = (int) e.getY();

        int dx = mCenterX - xPosition;
        int dy = mCenterY - yPosition;

        double angle = Math.toDegrees(Math.atan2(dy, dx)) - 180;
        if (angle < 0) {
            angle += 360;
        }

        return (angle + 360 - getBarabanRotation()) % 360;
    }


    private void setLayerToHW() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    private void moveItemUp(ArcView arcView) {
        int itemCenterAngle = ((arcView.getStartAngle() + arcView.getEndAngle()) / 2) + getBarabanRotation();
        itemCenterAngle %= 360;

        if (itemCenterAngle != 270) {
            int rotateAngle;
            if (itemCenterAngle > 270 || itemCenterAngle < 90) {
                rotateAngle = (270 - 360 - itemCenterAngle) % 360;
            } else {
                rotateAngle = (270 + 360 - itemCenterAngle) % 360;
            }

            mAutoCenterAnimator.setIntValues(mBarabanRotation, rotateAngle + mBarabanRotation);
            mAutoCenterAnimator.setDuration(AUTO_CENTER_ANIM_DURATION).start();
        }
    }


//    @Override
//    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
//        int xpad = (getPaddingLeft() + getPaddingRight());
//        int ypad = (getPaddingTop() + getPaddingBottom());
//
//        width = width - xpad;
//        height = height - ypad;
//
//
//        mRadius = Math.min(width, height) / 2;
//    }
}