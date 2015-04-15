package it.fold.remotecontrolandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Handles events and imaging
 */
public class StreamView extends SurfaceView implements SurfaceHolder.Callback {
    private StreamThread mStreamThread;
    /**
     * object to handle streaming
     */
    private Handler mStreamThreadHandler;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;


    private final GestureDetector.OnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
    };

    private final OnScaleGestureListener mScaleGestureListener
            = new SimpleOnScaleGestureListener() {
    };

    /**
     * constructor that inherits fields from surfaceview to give attributes
     * and context to view
     *
     * @param context context global information about application environment
     * @param attrs   attrs collection of attributes
     */
    public StreamView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetector(context, mGestureListener);

        mStreamThread = new StreamThread(surfaceHolder);
        mStreamThreadHandler = mStreamThread.getHandler();

        setFocusable(true);
    }

    @Override
    /**
     * When any changes are made to the surface this is called to update image
     *
     * @param SurfaceHolder holder handles surface changes to imaging
     */
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("StreamView", "creating surface");

        Paint paint = new Paint();
        paint.setColor(0xFFFFFFFF);

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(0xFFFF0000);
        canvas.drawLine(0, 0, 100, 100, paint);
        holder.unlockCanvasAndPost(canvas);

        mStreamThread.initialize(Constants.IP_ADDRESS_LOCAL, Constants.PORT, "");
        mStreamThread.setRunning(true);
        mStreamThread.start();
        Log.d("StreamView", "successfully created surface");
    }

    @Override
    /**
     * When surface is destroyed, logs to user
     *
     * @param SurfaceHolder holder unused but handles surface changing
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("StreamView", "surfaceDestroyed");
    }

    @Override
    /**
     * When surface is changed, logs to user
     *
     * @param SurfaceHolder holder unused but handles surface changing
     * @param int format new pixel format
     * @param int width width of surface
     * @param int height height of surface
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("StreamView", "surfaceChanged");
    }

    @Override
    /**
     * Handles touch events from the user
     *
     * @param MotionEvent e information about event
     * @return boolean true
     */
//        public boolean onTouchEvent(MotionEvent e) {
//
//            mGestureDetector.onTouchEvent(e);
//            int action = e.getAction();
//            int cl_action = 0;
//
//            int maskedAction = e.getActionMasked();
//            int pointerIndex = e.getActionIndex();
//            int pointerId = e.getPointerId(pointerIndex);
//            int x = (int) e.getX();
//            int y = (int) e.getY();
//
//            switch(action) {
//                case MotionEvent.ACTION_DOWN: {
//                    Log.d("debug", "ACTION_DOWN");
//                    cl_action = Constants.CLEV_MOUSE_DOWN;
//                }
//                case MotionEvent.ACTION_POINTER_DOWN: {
//                    Log.d("debug", "ACTION_POINTER_ DOWN");
//                    // New pointer
//
//                    PointF f = new PointF();
//                    f.x = e.getX(pointerIndex);
//                    f.y = e.getY(pointerIndex);
//                    mActivePointers.put(pointerId, f);
//
//                    Log.d("debug", "f.x: " + f.x + " ::: f.y: " + f.y);
//                    break;
//                }
//                case MotionEvent.ACTION_MOVE: { // a pointer was moved
//                    Log.d("debug", "ACTION_MOVE");
////                    cl_action = Constants.CLEV_MOUSE_MOVE;
//                    if(e.getPointerCount() > 1) {
//                        for (int size = e.getPointerCount(), i = 0; i < size; i++) {
//                            PointF point = mActivePointers.get(e.getPointerId(i));
//                            if (point != null) {
//                                int pointId = e.getPointerId(i);
//                                point.x = e.getX(i);
//                                point.y = e.getY(i);
//                                Log.d("debug", "POINT ID:: " + pointId + " point.x: " + point.x + " ::: point.y: " + point.y);
//                                switch (pointId) {
//                                    case 0: cl_action = Constants.CLEV_MOUSE_DOWN_AUX_0;
//                                    case 1: cl_action = Constants.CLEV_MOUSE_DOWN_AUX_1;
//                                    case 2: cl_action = Constants.CLEV_MOUSE_DOWN_AUX_2;
//                                }
//                             mStreamThreadHandler.obtainMessage(cl_action, ((int) point.x), ((int) point.y)).sendToTarget();
//                            }
//                        }
//                    } else {
//                            cl_action = Constants.CLEV_MOUSE_MOVE;
//                            mStreamThreadHandler.obtainMessage(cl_action, x, y).sendToTarget();
//                    }
//    //                break;
//                    return mScaleGestureDetector.onTouchEvent(e);
//                }
//            case MotionEvent.ACTION_UP: {
//                cl_action = Constants.CLEV_MOUSE_UP;
//            }
//            case MotionEvent.ACTION_POINTER_UP: {
//
//            }
//            case MotionEvent.ACTION_CANCEL: {
//                Log.d("debug", "pointer removed");
//                mActivePointers.remove(pointerId);
//                break;
//            }
//            case MotionEvent.ACTION_SCROLL: {
//                if (android.os.Build.VERSION.SDK_INT >= 12) {
//                    if (e.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0) {
//                        cl_action = Constants.CLEV_SCROLL_DOWN;
//                    } else {
//                        cl_action = Constants.CLEV_SCROLL_UP;
//                    }
//                }
//            }
//            }
//            invalidate();
//            Log.d("debug", "--");
//
//            return true;
//        }

    public boolean onTouchEvent(MotionEvent e) {
        //mScaleDetector.onTouchEvent(e);
        //touchTime = e.getEventTime();
        mGestureDetector.onTouchEvent(e);
//        if (e.getPointerCount() > 1) {
//            return mScaleGestureDetector.onTouchEvent(e);
//        }
        int x = (int) e.getX();
        int y = (int) e.getY();
        int action = e.getAction();
        int maskedAction = e.getActionMasked();
        int pointerCount = e.getPointerCount();
        int cl_action = 0;
//        Log.d("debug", "action = " + action);
//        Log.d("debug", "maskedAction = " + maskedAction);
//        Log.d("debug", "pointer count = " + pointerCount);

        if (pointerCount > 1) {
            if (maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
                Log.d("debug", "action pointer down");
                for (int size = pointerCount, i = 0; i < size; i++) {
                    int pointId = e.getPointerId(i);
                    x = (int) e.getX(i);
                    y = (int) e.getY(i);
//                    Log.d("debug", "POINT ID:: " + pointId + " point.x: " + x + " ::: point.y: " + y);
                    switch (pointId) {
                        case 0:
                            cl_action = Constants.CLEV_MOUSE_DOWN_AUX_0;
                        case 1:
                            cl_action = Constants.CLEV_MOUSE_DOWN_AUX_1;
                        case 2:
                            cl_action = Constants.CLEV_MOUSE_DOWN_AUX_2;
                    }
                    Log.d("debug", "Sending ::POINTER DOWN :: " + cl_action + " through the stream...");
                    mStreamThreadHandler.obtainMessage(cl_action, ((int) x), ((int) y)).sendToTarget();
                }
//                Log.d("debug", "ACTION POINTER DOWN ::::::::::::::::::::::::::::::: DOWN:");
            } else if (maskedAction == MotionEvent.ACTION_POINTER_UP) {
                Log.d("debug", "action pointer up");
                for (int size = pointerCount, i = 0; i < size; i++) {
                        int pointId = e.getPointerId(i);
                        x = (int) e.getX(i);
                        y = (int) e.getY(i);
//                        Log.d("debug", "POINT ID:: " + pointId + " point.x: " + point.x + " ::: point.y: " + point.y);
                        switch (pointId) {
                            case 0:
                                cl_action = Constants.CLEV_MOUSE_UP_AUX_0;
                            case 1:
                                cl_action = Constants.CLEV_MOUSE_UP_AUX_1;
                            case 2:
                                cl_action = Constants.CLEV_MOUSE_UP_AUX_2;
                        }
                        Log.d("debug", "Sending :: POINTER UP :: " + cl_action + " through the stream...");
                        mStreamThreadHandler.obtainMessage(cl_action, ((int) x), ((int) y)).sendToTarget();

                }
//                Log.d("debug", "ACTION POINTER UP :::::::::::::::::::::::::::::::: UP");
            } else if (maskedAction == MotionEvent.ACTION_MOVE) {
                Log.d("debug", "action move");
                for (int size = pointerCount, i = 0; i < size; i++) {
                        int pointId = e.getPointerId(i);
                        x = (int) e.getX(i);
                        y = (int) e.getY(i);
//                        Log.d("debug", "POINT ID:: " + pointId + " point.x: " + point.x + " ::: point.y: " + point.y);
                        switch (pointId) {
                            case 0:
                                cl_action = Constants.CLEV_MOUSE_MOVE_AUX_0;
                            case 1:
                                cl_action = Constants.CLEV_MOUSE_MOVE_AUX_1;
                            case 2:
                                cl_action = Constants.CLEV_MOUSE_MOVE_AUX_2;
                        }
                        Log.d("debug", "Sending :: MOVE :: " + cl_action + " through the stream...");
                        mStreamThreadHandler.obtainMessage(cl_action, ((int) x), ((int) y)).sendToTarget();

                }
            } else {
                Log.d("debug", "no case triggered for multitouch");
                return true;
            }
        } else {
            if (action == MotionEvent.ACTION_DOWN) {
                cl_action = Constants.CLEV_MOUSE_DOWN;
            } else if (action == MotionEvent.ACTION_UP) {
                cl_action = Constants.CLEV_MOUSE_UP;
            } else if (action == MotionEvent.ACTION_MOVE) {
                cl_action = Constants.CLEV_MOUSE_MOVE;
            } else if (action == MotionEvent.ACTION_SCROLL) {
                if (android.os.Build.VERSION.SDK_INT >= 12) {
                    if (e.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0) {
                        cl_action = Constants.CLEV_SCROLL_DOWN;
                    } else {
                        cl_action = Constants.CLEV_SCROLL_UP;
                    }
                }
            } else {
                return true;
            }
        }
        mStreamThreadHandler.obtainMessage(cl_action, x, y).sendToTarget();
        return true;
    }
}
