package com.androider.drawingapp.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androider.drawingapp.Models.SquareCoords;
import com.androider.drawingapp.R;

public class DrawingScreen extends AppCompatActivity {

    private static final String TAG = DrawingScreen.class.getSimpleName();
    SquareCoords squareCoords;
    //screenWidth to incraese on each side of square from center
    private int marginSpecified = 80;
    private ImageView ivDrawBoard;
    private SeekBar sbSquare;
    private Button btnShow;
    private int screenHeight;
    private int screenWidth;
    private Bitmap bitmap;
    private AlertDialog dialog;

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
        btnShow = findViewById(R.id.btnShow);
        sbSquare.setProgress(80);
        sbSquare.setMax(300);

        setSeekListener();

        //detecting touch gesture in coordinator layout
        setListenerForTouch();

        setListenerForBtnClick();
    }

    private void setListenerForBtnClick() {
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (squareCoords != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DrawingScreen.this);

                    builder.setTitle("Coordinates");

                    int xCoords[] = squareCoords.getSquareCODX();
                    int yCoords[] = squareCoords.getSquareCODY();

                    String msg = "[{coordinates: [(" + xCoords[0] + ", " + yCoords[0] + ")," +
                            " (" + xCoords[1] + ", " + yCoords[1] + "), (" + xCoords[2] + ", " + yCoords[2] + ")," +
                            " (" + xCoords[3] + ", " + yCoords[3] + ")]}]";

                    builder.setMessage(msg);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(DrawingScreen.this, "Please touch screen to draw square.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setListenerForTouch() {
        ivDrawBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
                //we are changing margin at this point to user choice margin
                marginSpecified = i;
                if (squareCoords != null) {
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

        int xCoords[] = squareCoords.getSquareCODX();
        int yCoords[] = squareCoords.getSquareCODY();

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
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

    private void calculateAndDrawCoords(int x, int y) {

        squareCoords = new SquareCoords();

        squareCoords.setxTouch(x);
        squareCoords.setyTouch(y);

        int xCoords[] = new int[4];
        int yCoords[] = new int[4];

        //now creating four coorinates of square. with a margin specified
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

    @Override
    protected void onStop() {
        //resetting all the values
        squareCoords = null;
        ivDrawBoard.setImageBitmap(null);
        if (dialog != null) {
            dialog.dismiss();
        }
        sbSquare.setProgress(80);
        sbSquare.setMax(300);
        marginSpecified = 80;

        super.onStop();
    }

}
