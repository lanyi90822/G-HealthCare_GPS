package com.example.hp.stepcount.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by Martin on 2017/3/24.
 */

public class HistoryECGView extends SurfaceView implements
        SurfaceHolder.Callback{

    private final String TAG = HistoryECGView.class.getSimpleName();
    private int mWidth,mHeight;
    private float mGridPixel;
    private SurfaceHolder mHolder=null;
    private Paint mPaint;
    private Path mPath;
    private Path mPath2;
    private Path mPath3;
    private Canvas mCanvas;
    private int mBaseLine = 0;
    private int lastX,lastY;
    private int base_pro;
    private float mXCoefficient, mYCoefficient;
    private static SeekBar mSeekBar;

    private int topBaseLine = 0;
    private int topBaseLine2 = 0;
    private int topBaseLine3 = 0;


    public void  setSeekBar(SeekBar bar){
        mSeekBar = bar;
    }
    public boolean setSeekBarProgress(int progress){
        if(mSeekBar != null) {
            mSeekBar.setProgress(progress);
            return true;
        }
        return false;
    }



    public HistoryECGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = this.getHolder();  //获取holder
        mHolder.addCallback(this);
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAlpha(255);
        mPaint.setAntiAlias(true);   //设置抗锯齿
        mPaint.setStrokeJoin(Paint.Join.ROUND);   //设置绘图转弯处圆滑
        mPath = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        mCanvas = new Canvas();
    }

    public void setGridPixel(float pixel){
        mGridPixel = pixel;
//        mXCoefficient = (float) mGridPixel*0.1f;
//        mYCoefficient = (float) mGridPixel*0.123f;
        mXCoefficient = (float) mGridPixel*0.1f;
        mYCoefficient = (float) mGridPixel*0.123f;

        Log.i(TAG,"mGridPixel ="+mGridPixel +"\n mXCoefficient ="+mXCoefficient+"\n mYCoefficient ="+mYCoefficient);
    }

    public float getmGridPixel(){
        return  mGridPixel;
    }

    public int getViewContainDataCount(){
    //    int count = (int)(mWidth/mGridPixel)*(250/25);
        int count = (int)(mWidth/mXCoefficient);
        Log.i(TAG,"getView datacount ="+count +"\n mWidth ="+mWidth);
        return count;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        mBaseLine = (int) (mHeight*0.8);

//        topBaseLine = (int) (mHeight * 0.25);
//        topBaseLine2 = (int) (mHeight * 0.57);
//        topBaseLine3 = (int) (mHeight * 0.86);
        topBaseLine = (int) (mHeight * 0.20);
        topBaseLine2 = (int) (mHeight * 0.52);
        topBaseLine3 = (int) (mHeight * 0.81);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                base_pro = mSeekBar.getProgress();
                break;
            case MotionEvent.ACTION_MOVE:
                int progress = (int) (base_pro-((float)((x-lastX)/mGridPixel)*90));
                setSeekBarProgress(progress);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.max(result, specSize);
            }
        }
        return result;
    }
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    public void DrawData(ArrayList<Integer> data1, ArrayList<Integer> data2, ArrayList<Integer> data3){
        float line_y;
        float line_y2;
        float line_y3;
        if(data1.size() >0){
            try {
                mCanvas = mHolder.lockCanvas(new Rect((int) 0 , 0 , mWidth , mHeight));
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mPath.reset();
                mPath2.reset();
                mPath3.reset();

                for(int j=0;j<data1.size()-1;j++){

                    line_y = topBaseLine - (data1.get(j+1) - 2048) / 24 * mYCoefficient+ getPaddingTop();
                    mPath.lineTo((j + 1) * mXCoefficient+ getPaddingLeft(),line_y);

                    line_y2 = topBaseLine2- (data2.get(j+1) - 2048) / 24 * mYCoefficient+ getPaddingTop();
                    mPath2.lineTo((j + 1) * mXCoefficient+ getPaddingLeft(),line_y2);

                    line_y3 = topBaseLine3- (data3.get(j+1) - 2048) / 24 * mYCoefficient+ getPaddingTop();
                    mPath3.lineTo((j + 1) * mXCoefficient+ getPaddingLeft(),line_y3);

                }
                mCanvas.drawPath(mPath, mPaint);
                mCanvas.drawPath(mPath2, mPaint);
                mCanvas.drawPath(mPath3, mPaint);

            }catch (Exception e) {
            } finally {
                if (mCanvas != null)
                    mHolder.unlockCanvasAndPost(mCanvas);  // 将画好的画布提交
            }

        }
    }

//    public void DrawData(ArrayList<Integer> data1,ArrayList<Integer> data2,ArrayList<Integer> data3){
//        float line_y;
//        if(data1.size() >0){
//            try {
//                mCanvas = mHolder.lockCanvas(new Rect((int) 0 , 0 , mWidth , mHeight));
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                mPath.reset();
//
//                for(int j=0;j<data1.size()-1;j++){
//
////                    line_y = mBaseLine- data1.get(j+1) * mYCoefficient+ getPaddingTop();
////                    int temp1 = (data1.get(j) - 2048) / 12;
////                    int temp2 = (data2.get(j) - 2048) / 12;
////                    int temp3 = (data3.get(j) - 2048) / 12;
//
//                    X_index = (int) (X_old + mXCoefficient);
//                    Y_index1 = (int) (topBaseLine- data1.get(j+1) *mHeight /4096 * mYCoefficient+ getPaddingTop());
//                    Y_index2 = (int) (topBaseLine2- data2.get(j+1) * mYCoefficient+ getPaddingTop());
//                    Y_index3 = (int) (topBaseLine3- data3.get(j+1) * mYCoefficient+ getPaddingTop());
//
//                    mCanvas.drawLine(X_old,Y_old1,X_index,Y_index1,mPaint);
//                    mCanvas.drawLine(X_old,Y_old2,X_index,Y_index2,mPaint);
//                    mCanvas.drawLine(X_old,Y_old3,X_index,Y_index3,mPaint);
//
//                    X_old = X_index;
//                    Y_old1 = Y_index1;
//                    Y_old2 = Y_index2;
//                    Y_old3 = Y_index3;
//                }
//                X_old = 0;
//                Y_old1 = topBaseLine;
//                Y_old2 = topBaseLine2;
//                Y_old3 = topBaseLine3;
//
//                if (mCanvas != null)
//                    mHolder.unlockCanvasAndPost(mCanvas);  // 将画好的画布提交
//
//            }catch (Exception e) {
//            } finally {
//
//            }
//
//        }
//    }






    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

//public class HistoryECGView extends SurfaceView implements
//        SurfaceHolder.Callback{
//    private final String TAG = HistoryECGView.class.getSimpleName();
//    private int mWidth,mHeight;
//    private float mGridPixel;
//    private SurfaceHolder mHolder=null;
//    private Paint mPaint;
//    private Path mPath;
//    private Canvas mCanvas;
//    private int mBaseLine = 0;
//    private int lastX,lastY;
//    private int base_pro;
//    private float mXCoefficient, mYCoefficient;
//    private SeekBar mSeekBar;
//    private int topBaseLine = 0;
//    private int topBaseLine2 = 0;
//    private int topBaseLine3 = 0;
//    private static int X_old = 0;
//    private static int Y_old1 = 0;
//    private static int Y_old2 = 0;
//    private static int Y_old3 = 0;
//    private static int X_index = 0;
//    private static int Y_index1 = 0;
//    private static int Y_index2 = 0;
//    private static int Y_index3 = 0;
//
//    public void  setSeekBar(SeekBar bar){
//        mSeekBar = bar;
//    }
//    public boolean setSeekBarProgress(int progress){
//        if(mSeekBar != null) {
//            mSeekBar.setProgress(progress);
//            return true;
//        }
//        return false;
//    }
//
//
//
//    public HistoryECGView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mHolder = this.getHolder();  //获取holder
//        mHolder.addCallback(this);
//        setZOrderOnTop(true);
//        mHolder.setFormat(PixelFormat.TRANSLUCENT);
//        mPaint = new Paint();
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(4);
//        mPaint.setAlpha(255);
//        mPaint.setAntiAlias(true);   //设置抗锯齿
//        mPaint.setStrokeJoin(Paint.Join.ROUND);   //设置绘图转弯处圆滑
//        mPath = new Path();
//        mCanvas = new Canvas();
//    }
//
//    public void setGridPixel(float pixel){
//        mGridPixel = pixel;
////        mXCoefficient = (float) mGridPixel*0.1f;
////        mYCoefficient = (float) mGridPixel*0.123f;
//        mXCoefficient = mGridPixel*0.2f;
//        mYCoefficient = mGridPixel*0.123f;
//        Log.i(TAG,"mGridPixel ="+mGridPixel +"\n mXCoefficient ="+mXCoefficient+"\n mYCoefficient ="+mYCoefficient);
//    }
//
//    public float getmGridPixel(){
//        return  mGridPixel;
//    }
//
//    public int getViewContainDataCount(){
//        int count = (int)(mWidth/mGridPixel)*(250/25);
//        Log.i(TAG,"getView datacount ="+count +"\n mWidth ="+mWidth);
//        return count;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = measureWidth(widthMeasureSpec);
//        mHeight = measureHeight(heightMeasureSpec);
//        mBaseLine = (int) (mHeight*0.7);
//
//        topBaseLine = (int) (mHeight * 0.25);
//        topBaseLine2 = (int) (mHeight * 0.57);
//        topBaseLine3 = (int) (mHeight * 0.86);
//
//        Y_old1 = topBaseLine;
//        Y_old2 = topBaseLine2;
//        Y_old3 = topBaseLine3;
//
//        Log.i(TAG,"onMeasure mWidth ="+mWidth +"\n mHeight ="+mHeight);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastX = x;
//                lastY = y;
//                base_pro = mSeekBar.getProgress();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG,"base_pro ="+base_pro+"x ="+x+"  lastX"+lastX+"  mGridPixel= "+mGridPixel);
//                Log.i(TAG,"(float)((x-lastX)/mGridPixel)*0.1f) ="+((float)((x-lastX)/mGridPixel)*0.1f));
//                int progress = (int) (base_pro-((float)((x-lastX)/mGridPixel)*10));
//                Log.i(TAG,"progress ="+progress);
//                setSeekBarProgress(progress);
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return true;
//    }
//
//    private int measureWidth(int measureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//        if (specMode == MeasureSpec.EXACTLY) {
//            result = specSize;
//        } else {
//            result = getWidth();
//            if (specMode == MeasureSpec.AT_MOST) {
//                result = Math.max(result, specSize);
//            }
//        }
//        return result;
//    }
//    private int measureHeight(int measureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//
//        if (specMode == MeasureSpec.EXACTLY) {
//            result = specSize;
//        } else {
//            result = getHeight();
//            if (specMode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, specSize);
//            }
//        }
//
//        return result;
//    }
//
//
//
//    public void DrawData(ArrayList<Integer> datas){
//        float line_y;
//        if(datas.size() >0) {
//            try {
//                mCanvas = mHolder.lockCanvas(new Rect((int) 0, 0, mWidth, mHeight));
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                mPath.reset();
//
////                line_y = mBaseLine-datas.get(0)*mYCoefficient+getPaddingTop();
////                mPath.moveTo(getPaddingLeft(), line_y<0?0:line_y);
//
//                for (int j = 0; j < datas.size() - 1; j++) {
//
//                 //   line_y = mBaseLine - datas.get(j + 1) * mYCoefficient + getPaddingTop();
//                    line_y = mBaseLine - ((datas.get(j) - 2048) / 12) * mYCoefficient + getPaddingTop();
//
//                    if (line_y < 0 || line_y > mHeight) {
//                        line_y = 0;
//                    } else if (line_y > mHeight) {
//                        line_y = mHeight;
//                    }
//                    mPath.lineTo((j + 1) * mXCoefficient + getPaddingLeft(), line_y);
//
//
//                }
//                mCanvas.drawPath(mPath, mPaint);
//
//            } catch (Exception e) {
//            } finally {
//                if (mCanvas != null)
//                    mHolder.unlockCanvasAndPost(mCanvas);  // 将画好的画布提交
//            }
//
//
//        }
//
//
//
////        if(data1.size() >0){
////            try {
////                mCanvas = mHolder.lockCanvas(new Rect((int) 0 , 0 , mWidth , mHeight));
////                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
////                mPath.reset();
////
////                for(int j=0;j<data1.size()-1;j++){
////
////                    int temp1 = (data1.get(j) - 2048) / 12;
////                    int temp2 = (data2.get(j) - 2048) / 12;
////                    int temp3 = (data3.get(j) - 2048) / 12;
////
////                    X_index = (int) (X_old + mXCoefficient);
////
////                    if (X_index >= mWidth){
////                        X_index = (int) mXCoefficient;
////                        X_old = 0;
////                        mHolder.unlockCanvasAndPost(mCanvas);
////                        mCanvas = mHolder.lockCanvas(new Rect(getPaddingLeft() + X_old, 0, (int) ((data1.size() - j - 1) * mXCoefficient + X_old + getPaddingLeft()+ 20), mHeight));
////                        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
////                    }
////
////
////                    Y_index1 = (int) (topBaseLine - temp1 * mYCoefficient);
////                    Y_index2 = (int) (topBaseLine2 - temp2 * mYCoefficient);
////                    Y_index3 = (int) (topBaseLine3 - temp3 * mYCoefficient);
////
////                    mCanvas.drawLine(X_old,Y_old1,X_index,Y_index1,mPaint);
////                    mCanvas.drawLine(X_old,Y_old2,X_index,Y_index2,mPaint);
////                    mCanvas.drawLine(X_old,Y_old3,X_index,Y_index3,mPaint);
////
////                    X_old = X_index;
////                    Y_old1 = Y_index1;
////                    Y_old2 = Y_index2;
////                    Y_old3 = Y_index3;
////
////                }
////
////
////
////            }catch (Exception e) {
////            } finally {
////                if (mCanvas != null)
////                    mHolder.unlockCanvasAndPost(mCanvas);  // 将画好的画布提交
////            }
////
////        }
//    }
//
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
//}
