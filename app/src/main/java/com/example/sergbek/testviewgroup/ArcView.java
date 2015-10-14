package com.example.sergbek.testviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


public class ArcView extends View {

    private int mRadius;
    private Paint mPArc;
    private Paint mPLine;
    private RectF mBarabanBounds;
    private float sweepAngle;

    int count;
    private Drawable mIcon;

    private int mCentX;
    private int mCentY;

    public ArcView(Context context) {
        super(context);
        init();
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ArcView,
                0, 0);

        try {
            this.mIcon = typedArray.getDrawable(R.styleable.ArcView_src);
        } finally {
            typedArray.recycle();
        }

        init();
    }

    private void init() {
        mPArc = new Paint();
        mPLine = new Paint();
        mBarabanBounds = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mCenterX = mCentX / 2;
        int mCenterY = mCentY / 2;

        mBarabanBounds.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);

        float startAngle = 270 - sweepAngle / 2;

        canvas.rotate(sweepAngle *count, mCenterX, mCenterY);

        mPArc.setColor(0xFF574153);
        mPArc.setAntiAlias(true);
        canvas.drawArc(mBarabanBounds, startAngle, sweepAngle, true, mPArc);


        double y = mCenterY + (Math.sin(Math.toRadians(270 - sweepAngle / 2)) * mRadius);
        double x = mCenterX + (Math.cos(Math.toRadians(270 - sweepAngle / 2)) * mRadius);

        double yy = mCenterY + (Math.sin(Math.toRadians(270 + sweepAngle / 2)) * mRadius);
        double xx = mCenterX + (Math.cos(Math.toRadians(270 + sweepAngle / 2)) * mRadius);


        mPLine.setAntiAlias(true);
        mPLine.setColor(0xFFED4702);
        mPLine.setStrokeWidth(4f);
        canvas.drawLine(mCenterX, mCenterY, (int) x, (int) y, mPLine);
        canvas.drawLine(mCenterX, mCenterY, (int) xx, (int) yy, mPLine);


        int sizeImage = (mRadius * 30) / 100;
        int positionLeft = mCenterX - sizeImage / 2;
        int positionTop = mCenterY - (mRadius * 2 / 3) - sizeImage / 2;
        mIcon.setBounds(positionLeft, positionTop, positionLeft + sizeImage, positionTop + sizeImage);
        mIcon.draw(canvas);

//        Log.d("www", mRadius + "");

    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }


    public void setCentX(int centX) {
        mCentX = centX;
    }

    public void setCentY(int centY) {
        mCentY = centY;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
