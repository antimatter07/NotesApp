package com.mobdeve.xx22.memomate.note.note_drawing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.google.android.material.slider.RangeSlider;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.BrushStrokeModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

public class DrawingActivityDisplay extends View {

    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String FOLDER_KEY = "FOLDER_KEY";
    public static final String DATE_CREATED_KEY = "DATE_KEY";
    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

    private DrawingActivityDisplay paint;
    private Button color, size, undo, pen;

    private RangeSlider rangeSlider;

    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;


    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    //note db
    private NoteDatabase noteDatabase;

    private Paint mPaint;

    // ArrayList to store all the strokes
    // drawn by the user on the Canvas
    private ArrayList<BrushStrokeModel> paths = new ArrayList<>();
    private int currentColor;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public DrawingActivityDisplay(Context context){
        this(context, null);
    }

    public DrawingActivityDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAlpha(0xff);

    }

    public void init(int height, int width){
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        // set an initial color of the brush
        currentColor = Color.GREEN;

        // set an initial brush size
        strokeWidth = 20;
    }

    public Bitmap save(){
        return mBitmap;
    }

    protected void onDraw(Canvas canvas) {
        // save the current state of the canvas before,
        // to draw the background of the canvas
        canvas.save();

        // DEFAULT color of the canvas
        int backgroundColor = Color.WHITE;
        mCanvas.drawColor(backgroundColor);

        // now, we iterate over the list of paths
        // and draw each path on the canvas
        for (BrushStrokeModel fp : paths) {
            mPaint.setColor(fp.penColor);
            mPaint.setStrokeWidth(fp.penWidth);
            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        BrushStrokeModel fp = new BrushStrokeModel(currentColor, strokeWidth, mPath);
        paths.add(fp);

        // finally remove any curve
        // or line from the path
        mPath.reset();

        // this methods sets the starting
        // point of the line being drawn
        mPath.moveTo(x, y);

        // we save the current
        // coordinates of the finger
        mX = x;
        mY = y;
    }

        private void touchMove (float x, float y){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);

            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touchUp(){
            mPath.lineTo(mX, mY);
        }



        public boolean onTouchEvent (MotionEvent event){
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;
            }
            return true;
    }
}





