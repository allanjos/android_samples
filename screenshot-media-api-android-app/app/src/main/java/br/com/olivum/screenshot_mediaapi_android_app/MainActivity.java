package br.com.olivum.screenshot_mediaapi_android_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ScreenshotApp";
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private OrientationChangeCallback orientationChangeCallback;
    private Display display;
    private int displayRotation;
    private int displayDensity;
    private int virtualDisplayWidth;
    private int virtualDisplayHeight;
    private static final String VIRTUAL_DISPLAY_NAME = "screenshot";
    private ImageView imageViewScreenshot = null;
    private static final String RESULT_CODE = "RESULT_CODE";
    private static final String DATA = "DATA";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageViewScreenshot = findViewById(R.id.image_view_screenshot);

        mediaProjectionManager = (MediaProjectionManager)getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);

        Button startScreenCaptureButton = findViewById(R.id.button_start_screen_capture);

        startScreenCaptureButton.setOnClickListener(view -> {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 1);
        });

        Button stopScreenCaptureButton = findViewById(R.id.button_stop_screen_capture);

        stopScreenCaptureButton.setOnClickListener(view -> {
            stopProjection();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopProjection();
    }

    /**
     * Result handler for permission request.
     * @param requestCode Request code of permission request.
     * @param resultCode Result code for permission request.
     * @param data Intent data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() requestCode=" + requestCode);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);

                handler.post(() -> {
                    start();
                });
            }
        }
    }

    /**
     * Start screenshot capture.
     */
    public void start() {
        Log.d(TAG, "start()");

        Intent intent = new Intent();

        int resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_OK);

        Intent data = intent.getParcelableExtra(DATA);

        startProjection(resultCode, data);
    }

    /**
     * Start media projection.
     */
    private void startProjection(int resultCode, Intent data) {
        Log.d(TAG, "startProjection()");

        if (mediaProjection != null) {
            // Display setup

            displayDensity = Resources.getSystem().getDisplayMetrics().densityDpi;

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            display = windowManager.getDefaultDisplay();

            createVirtualDisplay();

            // Register callback for orientation change

            orientationChangeCallback = new OrientationChangeCallback(this);

            if (orientationChangeCallback.canDetectOrientation()) {
                orientationChangeCallback.enable();
            }

            // Register callback called on media projection stop

            mediaProjection.registerCallback(new MediaProjectionStopCallback(), handler);
        }
    }

    /**
     * Stop media projection.
     */
    private void stopProjection() {
        if (handler != null) {
            handler.post(() -> {
                if (mediaProjection != null) {
                    mediaProjection.stop();
                }
            });
        }
    }

    /**
     * Callback called on stop of media projection.
     */
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.d(TAG, "stopping projection.");

            handler.post(() -> {
                if (virtualDisplay != null) {
                    virtualDisplay.release();
                }

                if (imageReader != null) {
                    imageReader.setOnImageAvailableListener(null, null);
                }

                if (orientationChangeCallback != null) {
                    orientationChangeCallback.disable();
                }

                mediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
            });
        }
    }

    /**
     * Orientation change callback.
     */
    private class OrientationChangeCallback extends OrientationEventListener {
        OrientationChangeCallback(Context context) {
            super(context);

            Log.d(TAG, "OrientationChangeCallback.OrientationChangeCallback()");
        }

        @Override
        public void onOrientationChanged(int orientation) {
            Log.d(TAG, "OrientationChangeCallback.onOrientationChanged()");

            final int currentRotation = display.getRotation();

            if (displayRotation != currentRotation) {
                displayRotation = currentRotation;

                try {
                    // Clean resources

                    if (virtualDisplay != null) {
                        virtualDisplay.release();
                    }

                    if (imageReader != null) {
                        imageReader.setOnImageAvailableListener(null, null);
                    }

                    // Re-create virtual display depending on device width / height

                    createVirtualDisplay();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get virtual display flags.
     * @return Virtual display flags.
     */
    private static int getVirtualDisplayFlags() {
        return DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    }

    /**
     * Creates a VirtualDisplay to capture the contents of the screen.
     */
    @SuppressLint("WrongConstant")
    private void createVirtualDisplay() {
        Log.d(TAG, "createVirtualDisplay()");

        // Get display width and height
        virtualDisplayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        virtualDisplayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Start capture reader
        imageReader = ImageReader.newInstance(virtualDisplayWidth,
                                              virtualDisplayHeight,
                                              PixelFormat.RGBA_8888,
                                              2);

        virtualDisplay = mediaProjection.createVirtualDisplay(VIRTUAL_DISPLAY_NAME,
                                                              virtualDisplayWidth,
                                                              virtualDisplayHeight,
                                                              displayDensity,
                                                              getVirtualDisplayFlags(),
                                                              imageReader.getSurface(),
                                                              null,
                                                              handler);

        imageReader.setOnImageAvailableListener(new ImageAvailableListener(), handler);
    }

    /**
     * Listener for image capture.
     */
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.d(TAG, "ImageAvailableListener.onImageAvailable()");

            try (Image image = imageReader.acquireLatestImage()) {
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * virtualDisplayWidth;

                    // Create bitmap from screenshot

                    Bitmap bitmap = Bitmap.createBitmap(virtualDisplayWidth + rowPadding / pixelStride,
                                                        virtualDisplayHeight,
                                                        Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    imageViewScreenshot.setImageBitmap(bitmap);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                /*
                if (bitmap != null) {
                    bitmap.recycle();
                }
                */
            }
        }
    }
}