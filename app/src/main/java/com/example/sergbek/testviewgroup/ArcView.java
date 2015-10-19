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

    private Paint mPArc;
    private Paint mPLine;
    private RectF mArcBounds;

    private float sweepAngle;

    private int mRadius;
    private int mCenterX;
    private int mCenterY;
    private int mStartAngle;
    private int mEndAngle;
    private int sizeImage;
    private int positionLeft;
    private int positionTop;

    private double leftX;
    private double leftY;
    private double rightX;

    private Drawable mIcon;
    private int mColor;

    public ArcView(Context context) {
        super(context);
        init();
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs,
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
        mArcBounds = new RectF();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mRadius = getWidth() / 2;

        leftX = mCenterX + (Math.cos(Math.toRadians(270 - sweepAngle / 2)) * mRadius);
        leftY = mCenterY + (Math.sin(Math.toRadians(270 - sweepAngle / 2)) * mRadius);
        rightX = mCenterX + (Math.cos(Math.toRadians(270 + sweepAngle / 2)) * mRadius);

        sizeImage = (mRadius * 30) / 100;
        positionLeft = mCenterX - sizeImage / 2;
        positionTop = mCenterY - (mRadius * 2 / 3) - sizeImage / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mArcBounds.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);

        float startAngle = 270 - sweepAngle / 2;

        canvas.rotate(90 + sweepAngle / 2, mCenterX, mCenterY);

        mPArc.setColor(mColor);
        mPArc.setAntiAlias(true);
        canvas.drawArc(mArcBounds, startAngle, sweepAngle, true, mPArc);

        mPLine.setAntiAlias(true);
        mPLine.setColor(0xFFED4702);
        mPLine.setStrokeWidth(4f);
        canvas.drawLine(mCenterX, mCenterY, (int) leftX, (int) leftY, mPLine);
        canvas.drawLine(mCenterX, mCenterY, (int) rightX, (int) leftY, mPLine);

        mIcon.setBounds(positionLeft, positionTop, positionLeft + sizeImage, positionTop + sizeImage);
        mIcon.draw(canvas);
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    public void setEndAngle(int endAngle) {
        mEndAngle = endAngle;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getStartAngle() {
        return mStartAngle;
    }

    public int getEndAngle() {
        return mEndAngle;
    }

    public void rotateTo(float pieRotation) {
        setRotation(pieRotation);
    }
}
