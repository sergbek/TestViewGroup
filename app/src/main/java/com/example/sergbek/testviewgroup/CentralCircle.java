package com.example.sergbek.testviewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class CentralCircle extends View {

    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    private Paint mPaint;

    public CentralCircle(Context context) {
        super(context);
        init();
    }

    public CentralCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = getWidth() / 2;//TODO: Why are you using getWidth() method if you have width argument?
        mCenterY = getHeight() / 2;

        mRadius = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(0xFF01E98C);//TODO: make a constant; move to init() method
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 7, mPaint);

        mPaint.setColor(0xFFED4702);//TODO: make a constant; move to init() method
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 11, mPaint);

    }
}
