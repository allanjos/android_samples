package br.com.olivum.viewport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Vertical scrolling

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view_image);

        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    int scrollX = scrollView.getScrollX();
                    int scrollY = scrollView.getScrollY();

                    Log.d(TAG, "scrollView onScrollChange() x=" + scrollX + ", y=" + scrollY);
                }
            });
        }

        // Horizontal scrolling

        final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scroll_view);

        if (horizontalScrollView != null) {
            horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    int scrollX = horizontalScrollView.getScrollX();
                    int scrollY = horizontalScrollView.getScrollY();

                    Log.d(TAG, "horizontalScrollView onScrollChange() x=" + scrollX + ", y=" + scrollY);
                }
            });
        }

        // Image view - Main picture

        final ImageView imageView = (ImageView) findViewById(R.id.image_view);

        // Thumbnail container

        final RelativeLayout relativeLayoutThumbnail = (RelativeLayout) findViewById(R.id.linear_layout_thumbnail);

        // Bitmap picture

        Bitmap bitmapPicture = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                                            R.drawable.nuclear);

        Log.d(TAG, "bitmapPicture width=" + bitmapPicture.getWidth() +
                   ", height=" + bitmapPicture.getHeight());

        float proportion = (float) bitmapPicture.getWidth() / (float) bitmapPicture.getHeight();

        // Thumbnail - Bitmap

        Bitmap bitmapThumbnail = ThumbnailUtils.extractThumbnail(bitmapPicture,
                                                                 (int) ((float) 200 * proportion),
                                                                 200);

        // Thumbnail - Image view

        final ImageView imageViewThumbnail = (ImageView) findViewById(R.id.image_view_thumbnail);

        if (imageViewThumbnail != null) {
            imageViewThumbnail.setImageBitmap(bitmapThumbnail);


            // Current square

            final LinearLayout viewThumbnailCurrentSquare =  (LinearLayout) findViewById(R.id.view_thumbnail_current_square);

            // On touch for thumbnail container

            if (relativeLayoutThumbnail != null && viewThumbnailCurrentSquare != null) {
                relativeLayoutThumbnail.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        float touchX = event.getX();
                        float touchY = event.getY();
                        Log.d(TAG, "Touch coord: " + touchX + ", " + touchY);

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewThumbnailCurrentSquare.getLayoutParams();

                        float squareX = touchX - (viewThumbnailCurrentSquare.getWidth() / 2);
                        float squareY = touchY - (viewThumbnailCurrentSquare.getHeight() / 2);

                        if (squareX < 0) squareX = 0;
                        if (squareY < 0) squareY = 0;

                        if (squareX > imageViewThumbnail.getWidth() - viewThumbnailCurrentSquare.getWidth())
                            squareX = imageViewThumbnail.getWidth() - viewThumbnailCurrentSquare.getWidth();
                        if (squareY > imageViewThumbnail.getHeight() - viewThumbnailCurrentSquare.getHeight())
                            squareY = imageViewThumbnail.getHeight() - viewThumbnailCurrentSquare.getHeight();

                        layoutParams.setMargins((int) squareX, (int) squareY, 0, 0);

                        viewThumbnailCurrentSquare.setLayoutParams(layoutParams);

                        float thumbnailPercentScrollX = squareX / imageViewThumbnail.getWidth();
                        float thumbnailPercentScrollY = squareY / imageViewThumbnail.getHeight();

                        if (imageView != null) {
                            float imageScrollX = imageView.getWidth() * thumbnailPercentScrollX;
                            float imageScrollY = imageView.getHeight() * thumbnailPercentScrollY;

                            if (scrollView != null) {
                                scrollView.scrollTo((int) imageScrollX, (int) imageScrollY);
                            }

                            if (horizontalScrollView != null) {
                                horizontalScrollView.scrollTo((int) imageScrollX, (int) imageScrollY);
                            }
                        }

                        return true;
                    }
                });
            }
        }

        /*
        ViewTreeObserver vto = imageViewThumbnail.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewThumbnail.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // Get the width and height
                int width  = imageViewThumbnail.getMeasuredWidth();
                int height = imageViewThumbnail.getMeasuredHeight();

                Log.d(TAG, "imageViewThumbnail width=" + width + ", height=" + height);
            }
        });
        */
    }
}
