package com.example.hp.stepcount.Common;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

/**
 * Created by lenovo on 2015/10/14.
 */
public class PaintBOX {

    Paint solidpaint_LTGRAY,solidpaint_BLACK,dashedpaint_LTGRAY,solidpaint_BLUE,solidpaint_RED,solidpaint_YELLOW;
    public static final String SolidPaintLTGRAY="com.example.lenovo.caseapp.View.PaintBOX.solidpaint_LTGRAY";
    public static final String SolidPaintBLACK="com.example.lenovo.caseapp.View.PaintBOX.SolidPaintBLACK";
    public static final String DashedPaintLTGRAY="com.example.lenovo.caseapp.View.PaintBOX.dashedpaint_LTGRAY";
    public static final String SolidPaintBLUE="com.example.lenovo.caseapp.View.PaintBOX.solidpaint_BLUE";
    public static final String SolidPaintRED="com.example.lenovo.caseapp.View.PaintBOX.solidpaint_RED";
    public static final String SolidPaintYELLOW="com.example.lenovo.caseapp.View.PaintBOX.SolidPaintYELLOW";
    /*********************************
     * 画笔管理
     *********************************/
    public PaintBOX()
    {
        solidpaint_LTGRAY = new Paint();                                                                     /******************* 实线画笔  LTGRAY********************/
        solidpaint_LTGRAY.setColor(Color.LTGRAY);
        solidpaint_LTGRAY.setStyle(Paint.Style.STROKE);
        solidpaint_LTGRAY.setAntiAlias(false);

        solidpaint_BLACK = new Paint();                                                                     /******************* 实线画笔  black********************/
        solidpaint_BLACK.setColor(Color.BLACK);
        solidpaint_BLACK.setTextSize(16);
        solidpaint_BLACK.setStyle(Paint.Style.STROKE);
        solidpaint_BLACK.setAntiAlias(true);

        dashedpaint_LTGRAY=new Paint();                                                                     /******************* 虚线画笔  LTGRAY********************/
        dashedpaint_LTGRAY.setColor(Color.LTGRAY);
        dashedpaint_LTGRAY.setStyle(Paint.Style.STROKE);
        dashedpaint_LTGRAY.setAntiAlias(false);
        PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
        dashedpaint_LTGRAY.setPathEffect(effects);

        solidpaint_BLUE = new Paint();                                                                        /******************* 实线画笔  BLUE********************/
        solidpaint_BLUE.setColor(Color.BLUE);
        solidpaint_BLUE.setStrokeWidth(2);
        solidpaint_BLUE.setStyle(Paint.Style.STROKE);
        solidpaint_BLUE.setAntiAlias(true);

        solidpaint_RED = new Paint();                                                                      /******************* 实线画笔  RED********************/
        solidpaint_RED.setColor(Color.RED);
        solidpaint_RED.setStrokeWidth(2);
        solidpaint_RED.setStyle(Paint.Style.STROKE);
        solidpaint_RED.setAntiAlias(false);

        solidpaint_YELLOW = new Paint();                                                                      /******************* 实线画笔  YELLOW********************/
        solidpaint_YELLOW.setColor(Color.YELLOW);
        solidpaint_YELLOW.setStrokeWidth(2);
        solidpaint_YELLOW.setStyle(Paint.Style.STROKE);
        solidpaint_YELLOW.setAntiAlias(true);
    }

    public Paint getPaint(String S)
    {
        Paint paint = new Paint();
       switch(S)
       {
           case SolidPaintLTGRAY :
               paint = solidpaint_LTGRAY;
               break;
           case SolidPaintBLACK:
               paint = solidpaint_BLACK;
               break;
           case DashedPaintLTGRAY:
               paint = dashedpaint_LTGRAY;
               break;
           case SolidPaintBLUE:
               paint = solidpaint_BLUE;
               break;
           case SolidPaintRED:
               paint = solidpaint_RED;
               break;
           case SolidPaintYELLOW:
               paint = solidpaint_YELLOW;
               break;
           default:
               paint = solidpaint_LTGRAY;
       }
        return paint;
    }
}
