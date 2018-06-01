package com.androider.drawingapp.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.androider.drawingapp.Models.SquareCoords;
import com.androider.drawingapp.R;

public class DrawingScreen extends AppCompatActivity {

    private static final String TAG = DrawingScreen.class.getSimpleName();

    //screenWidth to incraese on each side of square from center
    private int marginSpecified = 80;

    private ImageView ivDrawBoard;
    private SeekBar sbSquare;

    private int screenHeight;
    private int screenWidth;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_screen);

        //initializing xml elements and other variables in this method
        setScreenDimension();
        initXML();
    }

    private void initXML() {
        ivDrawBoard = findViewById(R.id.ivDrawBoard);
        sbSquare = findViewById(R.id.sbSquare);

        sbSquare.setProgress(80);
        sbSquare.setMax(300);
        setSeekListener();

        //detecting touch gesture in coordinator layout
        ivDrawBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.d(TAG, "OnTouch");
                windowTouched(motionEvent);
                return false;
            }
        });
    }


    //setting seek bar listener for changing square size
    private void setSeekListener() {
        sbSquare.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG,"onProgressChanged : "+i);
                marginSpecified = i;
                if (squareCoords != null){
                    calculateAndDrawCoords(squareCoords.getxTouch(), squareCoords.getyTouch());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void drawSquare(SquareCoords squareCoords) {

        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        ivDrawBoard.setImageBitmap(bitmap);

        final int xCoords[] = squareCoords.getSquareCODX();
        final int yCoords[] = squareCoords.getSquareCODY();

        final Canvas canvas = new Canvas(bitmap);

        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);


        for (int i = 0; i < 4; i++) {
            //points to draw line between two co-ords
            PointF startPoint = new PointF();
            PointF endPoint = new PointF();

            if (i != 3) {
                startPoint.set(xCoords[i], yCoords[i]);
                endPoint.set(xCoords[i + 1], yCoords[i + 1]);
            } else {
                startPoint.set(xCoords[i], yCoords[i]);
                endPoint.set(xCoords[0], yCoords[0]);
            }

            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint);
        }
    }

    private void windowTouched(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        Log.d(TAG, "X : " + x + ", Y : " + y);

        calculateAndDrawCoords(x, y);
    }

    SquareCoords squareCoords;
    private void calculateAndDrawCoords(int x, int y) {

        squareCoords = new SquareCoords();

        squareCoords.setxTouch(x);
        squareCoords.setyTouch(y);

        int xCoords[] = new int[4];
        int yCoords[] = new int[4];

        //now craeting four coorinates of square. with a margin specified
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    xCoords[0] = x - marginSpecified;
                    yCoords[0] = y + marginSpecified;
                    break;
                case 1:
                    xCoords[1] = x + marginSpecified;
                    yCoords[1] = y + marginSpecified;
                    break;
                case 2:
                    xCoords[2] = x + marginSpecified;
                    yCoords[2] = y - marginSpecified;
                    break;
                case 3:
                    xCoords[3] = x - marginSpecified;
                    yCoords[3] = y - marginSpecified;
                    break;

            }
        }

        squareCoords.setSquareCODX(xCoords);
        squareCoords.setSquareCODY(yCoords);

        drawSquare(squareCoords);
    }

    private void setScreenDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


}
