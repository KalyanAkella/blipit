package com.thoughtworks.blipit.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thoughtworks.blipit.R;

/**
 * A view representing a MapView marker information balloon.
 * <p>
 * This class has a number of Android resource dependencies:
 * <ul>
 * <li>drawable/balloon_overlay_bg_selector.xml</li>
 * <li>drawable/balloon_overlay_close.png</li>
 * <li>drawable/balloon_overlay_focused.9.png</li>
 * <li>drawable/balloon_overlay_unfocused.9.png</li>
 * <li>layout/balloon_map_overlay.xml</li>
 * </ul>
 * </p>
 *
 * @author Jeff Gilfelt
 *
 */
public class BalloonOverlayView extends FrameLayout {
    private LinearLayout layout;
    private TextView title;
    private TextView snippet;

    /**
     * Create a new BalloonOverlayView.
     *
     * @param context - The activity context.
     * @param balloonBottomOffset - The bottom padding (in pixels) to be applied
     * when rendering this view.
     */
    public BalloonOverlayView(Context context, int balloonBottomOffset) {

        super(context);

        setPadding(10, 0, 10, balloonBottomOffset);
        layout = new LinearLayout(context);
        layout.setVisibility(VISIBLE);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.balloon_map_overlay, layout);
        title = (TextView) v.findViewById(R.id.balloon_item_title);
        snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);

        ImageView close = (ImageView) v.findViewById(R.id.close_img_button);
        close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                layout.setVisibility(GONE);
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.NO_GRAVITY;

        addView(layout, params);

    }

    /**
     * Sets the view data from a given overlay item.
     *
     * @param title
     * @param snippet
     */
    public void setData(String title, String snippet) {

        layout.setVisibility(VISIBLE);
        if (title != null) {
            this.title.setVisibility(VISIBLE);
            this.title.setText(title);
        } else {
            this.title.setVisibility(GONE);
        }
        if (snippet != null) {
            this.snippet.setVisibility(VISIBLE);
            this.snippet.setText(snippet);
        } else {
            this.snippet.setVisibility(GONE);
        }

    }
}
