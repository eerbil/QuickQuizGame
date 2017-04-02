package com.example.eliferbil.quickquiz.memogame;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Ata on 2.4.2017.
 */

public class ImageAdapter extends BaseAdapter {
    private static final int IMAGE_EDGE_LENGTH = 100;
    private Context mContext;
    private
    @IdRes
    int[][] ids;
    private final int edgeLength;


    public ImageAdapter(Context c, @IdRes int[][] ids) {
        this.mContext = c;
        this.ids = ids;
        this.edgeLength = ids.length;
        if (edgeLength != ids[0].length) {
            throw new IllegalArgumentException("Ids must be square");
        }
    }

    public int getCount() {
        return edgeLength * edgeLength;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(IMAGE_EDGE_LENGTH, IMAGE_EDGE_LENGTH));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        int[] yx = posTo2D(position);
        imageView.setImageResource(ids[yx[0]][yx[1]]);
        return imageView;
    }

    private int[] posTo2D(int position) {
        return new int[]{position / edgeLength, position % edgeLength};
    }
}

