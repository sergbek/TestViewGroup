package com.example.sergbek.testviewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class CentralCircle extends View {

    private int mRadius;
    private int mCentX;
    private int mCentY;
    private Paint mPaint;

    public CentralCircle(Context context) {
        super(context);
        init();
    }

    public CentralCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {

//        int mCenterX = mCentX;
//        int mCenterY = mCentY ;

        int mCenterX = getWidth() /2;
        int mCenterY = getHeight() /2 ;

        mRadius = getWidth() /2;

        mPaint.setColor(0xFF01E98C);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 7, mPaint);

        mPaint.setColor(0xFFED4702);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 11, mPaint);

    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setCentX(int centX) {
        mCentX = centX;
    }

    public void setCentY(int centY) {
        mCentY = centY;
    }
}
