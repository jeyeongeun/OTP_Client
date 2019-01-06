package com.example.administrator.webpasswordprogram;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconTextView extends LinearLayout {

    /**
     * TextView 01
     */
    private TextView mText01;

    /**
     * TextView 03
     */
    private TextView mText02;
    private ImageView mIcon;

    public IconTextView(Context context, IconTextItem aItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listitem, this, true);

        mIcon = (ImageView) findViewById(R.id.isRegistered);
        mIcon.setImageDrawable(aItem.getIcon());

        mText01 = (TextView) findViewById(R.id.siteName);
        mText01.setText(aItem.getData(0));

        mText02 = (TextView) findViewById(R.id.siteAddress);
        mText02.setText(aItem.getData(1));


    }

    /**
     * set Text
     *
     * @param index
     * @param data
     */
    public void setText(int index, String data) {
        if (index == 0) {
            mText01.setText(data);
        } else if (index == 1) {
            mText02.setText(data);
        }else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * set Icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }

}