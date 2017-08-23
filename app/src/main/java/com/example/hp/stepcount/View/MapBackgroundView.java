package com.example.hp.stepcount.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.hp.stepcount.R;


/**
 * Created by Martin on 2017/3/6.
 */

public class MapBackgroundView extends View {

    private float mSpace = -1;
    private float mStroke = -1;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mPaintColor;
    private int mPaintAlpha;
    private static final String TAG = MapBackgroundView.class.getSimpleName();
    public MapBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EcgBackgroundView);
        mSpace = array.getDimension(R.styleable.EcgBackgroundView_length,20.0f);
        mPaintColor = array.getColor(R.styleable.EcgBackgroundView_paintColor, Color.GREEN);
        mStroke = array.getDimension(R.styleable.EcgBackgroundView_paintStroke,2);
        mPaintAlpha = array.getInteger(R.styleable.EcgBackgroundView_paintAlpha,255);

        // 获得屏幕实际大小
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE); //设置为空心
        mPaint.setStrokeWidth((int) mStroke); //设置后线条变宽
        mPaint.setAlpha(mPaintAlpha);
        mPaint.setAntiAlias(true);   //设置抗锯齿
        //mPaint.setDither(true);      //设置防抖动
        mPaint.setStrokeJoin(Paint.Join.ROUND);   //设置绘图转弯处圆滑
    }

    public float getMapSpace(){
        return mSpace;
    }
    public MapBackgroundView(Context context) {
        super(context);
    }



    private int measureWidth(int widthMeasureSpec) {
        int result =2;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {//精确模式
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 2;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);  //设置当前view大小

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT); //设置背景单一颜色
        int vertz = 0;
        int hortz = 0;
        canvas.drawLine(0, 0, mWidth, 0, mPaint);
        for(int i=0;vertz<mHeight || hortz< mWidth;i++){
            vertz += mSpace;
            hortz += mSpace;
            Log.w(TAG, "onDrawindex" + " = " + mSpace);
            canvas.drawLine(0, vertz, mWidth, vertz, mPaint);
            canvas.drawLine(hortz, 0, hortz, mHeight, mPaint);
        }

    }
}
