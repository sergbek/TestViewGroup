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
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mRadius = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(0xFF01E98C);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 7, mPaint);

        mPaint.setColor(0xFFED4702);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 11, mPaint);

    }
}
