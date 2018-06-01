package com.androider.drawingapp.Models;

public class SquareCoords {

    private int xTouch;
    private int yTouch;
    private int[] squareCODX;
    private int[] squareCODY;

    public int[] getSquareCODX() {
        return squareCODX;
    }

    public void setSquareCODX(int[] squareCODX) {
        this.squareCODX = squareCODX;
    }

    public int[] getSquareCODY() {
        return squareCODY;
    }

    public void setSquareCODY(int[] squareCODY) {
        this.squareCODY = squareCODY;
    }

    public int getxTouch() {
        return xTouch;
    }

    public void setxTouch(int xTouch) {
        this.xTouch = xTouch;
    }

    public int getyTouch() {
        return yTouch;
    }

    public void setyTouch(int yTouch) {
        this.yTouch = yTouch;
    }
}
