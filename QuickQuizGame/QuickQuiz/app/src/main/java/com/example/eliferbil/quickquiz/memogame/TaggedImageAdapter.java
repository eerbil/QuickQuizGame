package com.example.eliferbil.quickquiz.memogame;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Ata on 2.4.2017.
 */

public class TaggedImageAdapter<T> extends BaseAdapter {
    private static final int IMAGE_EDGE_LENGTH = 100;
    protected Context mContext;
    protected
    @DrawableRes
    int[] mipmapIds;
    private final int size;
    private ImageView[] images;
    private List<T> tags;


    public TaggedImageAdapter(Context c, @DrawableRes int[] mipmapIds, List<T> tags) {
        this.mContext = c;
        this.mipmapIds = mipmapIds;
        this.size = mipmapIds.length;
        this.images = new ImageView[size];
        this.tags = tags;
    }


    public int getCount() {
        return size;
    }

    public Object getItem(int position) {
        return images[position];
    }

    public long getItemId(int position) {
        return images[position].getId();
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
            imageView.setTag(tags.get(position));

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mipmapIds[position]);

        if (imageView != images[position]) {
            images[position] = imageView;
        }
        return imageView;
    }

    /*private int[] posTo2D(int position) {
        return new int[]{position / size, position % size};
    }*/
}

