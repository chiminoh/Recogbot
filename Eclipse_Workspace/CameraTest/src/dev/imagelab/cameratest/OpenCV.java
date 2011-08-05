package dev.imagelab.cameratest;

public class OpenCV {
    static {
        System.loadLibrary("opencv");
    }
	public native boolean learnFace(int[][] photoData, int width, int height, int imangeNum);
	public native int[] faceRecognition(int[] photoData, int width, int height);
}