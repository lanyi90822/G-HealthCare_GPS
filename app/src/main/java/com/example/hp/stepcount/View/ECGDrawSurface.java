package com.example.hp.stepcount.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.hp.stepcount.Common.filter;

import java.util.ArrayList;

/**
 * Created by HP on 2017/6/14.
 */

public class ECGDrawSurface extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "package com.homemedical.ecgmonitor.view.ECGDrawSurface";
    private Context context;
    private SurfaceHolder mHolder=null;
    private Paint mPaint;
    private filter mfilter = new filter();
    private ArrayList<Integer> ECGdata1;
    private ArrayList<Integer> ECGdata2;
    private ArrayList<Integer> ECGdata3;
    private boolean ecg_update_state = false;
    private int mWidth, mHeight;
    private Canvas mCanvas;
    private MyThread mThread = null;
    private int mViewHeight = 0;
    private float mGridPixel;
    private float mXCoefficient, mYCoefficient;
    private int topBaseLine = 0;
    private int topBaseLine2 = 0;
    private int topBaseLine3 = 0;
    private int block_count = 0; //记录上一次X轴的位置
    private int Y_index = 0;
    private int Y_index2 = 0;
    private int Y_index3 = 0;
    private int X_index = 0;
    private int X_oid = 0;
    private int Y_old = 0;
    private int Y_old2 = 0;
    private int Y_old3 = 0;

    public ECGDrawSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mHolder = this.getHolder();  //获取holder
        mHolder.addCallback(this);	 //获取回调接口
        setZOrderOnTop(true);		 //设置画布  背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        setKeepScreenOn(true); //是窗口支持透明度设置
    }


    public void setGridPixel(float pixel){  //设置
        mGridPixel = pixel;
        mXCoefficient = mGridPixel*0.2f;
        mYCoefficient = mGridPixel*0.123f;
    }

     public void ECGDataUpdate(ArrayList<Integer> ECGBlock1, ArrayList<Integer> ECGBlock2, ArrayList<Integer> ECGBlock3){
         ECGdata1 = ECGBlock1;
         ECGdata2 = ECGBlock2;
         ECGdata3 = ECGBlock3;
         ecg_update_state = true;
         Log.w(TAG, "ECGDataUpdate_show" + " = " + ecg_update_state);
     }

    //心电绘图线程
    public class MyThread extends Thread {
        private SurfaceHolder holder;
        private boolean isRun;
        private int wait = 5;

        public void cancel() {
            isRun = false;
        }
        public void startRun(boolean isRun) {
            this.isRun = isRun;
        }
        public MyThread(SurfaceHolder h) {
            this.holder = h;
            isRun = true;
        }
        @Override
        public void run() {
            mCanvas = new Canvas();
            while (isRun){
                if (ecg_update_state){
                    ecg_update_state = false;
                    Log.w(TAG, "run+++" + "=开始画图");
                    if (mCanvas == null)
                        return;
                    try {
                        synchronized (holder) {
                            DrawECGRawdata(holder);
                        }
                        Log.w(TAG, "runtime" + " = " + System.currentTimeMillis());
                 //       Thread.sleep(wait);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void DrawECGRawdata(SurfaceHolder holder) throws InterruptedException {
//        mCanvas = holder.lockCanvas(new Rect( getPaddingLeft() + X_oid, 0,
//                (int) (ECGdata1.size()* mXCoefficient + X_oid + getPaddingLeft()), mHeight));   //锁定绘图区域

//        mCanvas = holder.lockCanvas(new Rect(X_oid, 0,
//                (int) ((ECGdata1.size() + 1 )* mXCoefficient + X_oid), mHeight));   //锁定绘图区域

        mCanvas = holder.lockCanvas(new Rect(getPaddingLeft() + X_oid, 0, (int) (ECGdata1.size() * mXCoefficient + X_oid + getPaddingLeft()), mHeight));
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        for (int i=0;i<ECGdata1.size();i++ ){
       //     int temp1 = (int) (ECGdata1.get(i) * mHeight /4096.0) * 12;
            int temp1 = (ECGdata1.get(i) - 2048) / 12;
            int temp2 = (ECGdata2.get(i) - 2048) / 12;
            int temp3 = (ECGdata3.get(i) - 2048) / 12;

            X_index = (int) (X_oid + mXCoefficient);

            if (X_index >= mWidth){
                X_index = (int) mXCoefficient;
                X_oid = 0;
                holder.unlockCanvasAndPost(mCanvas);
                mCanvas = holder.lockCanvas(new Rect(getPaddingLeft() + X_oid, 0, (int) ((ECGdata1.size() - i - 1) * mXCoefficient + X_oid + getPaddingLeft()+ 20), mHeight));
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }


            Y_index = (int) (topBaseLine - temp1 * mYCoefficient);
            Y_index2 = (int) (topBaseLine2 - temp2 * mYCoefficient);
            Y_index3 = (int) (topBaseLine3 - temp3 * mYCoefficient);

            mCanvas.drawLine(X_oid,Y_old,X_index,Y_index,mPaint);
            mCanvas.drawLine(X_oid,Y_old2,X_index,Y_index2,mPaint);
            mCanvas.drawLine(X_oid,Y_old3,X_index,Y_index3,mPaint);

            X_oid = X_index;
            Y_old = Y_index;
            Y_old2 = Y_index2;
            Y_old3 = Y_index3;

         //   Thread.sleep(6);

        }

        holder.unlockCanvasAndPost(mCanvas);
        invalidate();
    }




    /**
     * 依据specMode的值，（MeasureSpec有3种模式分别是UNSPECIFIED, EXACTLY和AT_MOST）
     * 如果是AT_MOST，specSize 代表的是最大可获得的空间； 如果是EXACTLY，specSize 代表的是精确的尺寸；
     * 如果是UNSPECIFIED，对于控件尺寸来说，没有任何参考意义。
     */
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        mWidth = width;
        mHeight = height;
        // 告诉父控件，需要多大地方放置子控件
        setMeasuredDimension(width, height);
        topBaseLine = (int) (height * 0.25);
        topBaseLine2 = (int) (height * 0.55);
        topBaseLine3 = (int) (height * 0.84);
        X_oid = 0;
        Y_old = topBaseLine;
        Y_old2 = topBaseLine2;
        Y_old3 = topBaseLine3;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread = new MyThread(mHolder);
        Log.w(TAG, "surfaceCreated" + "=已经执行");

        if (!mThread.isAlive()){
            mThread.setName("drawThread");   //设置绘图线程名
            mThread.startRun(true);
            mThread.start();
        }
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        mPaint.setAlpha(255);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mThread != null){

            mThread.cancel();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
