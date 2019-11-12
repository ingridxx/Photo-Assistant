package com.example.photoassistant;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * this class is an instance of a view pager, which would contain our tutorial photos.
 * I didnt have time to fully implement but all the parts which work are in here.
 */

public class ImageAdapter extends PagerAdapter {

    private Context mContext;

    private int[] mImageIDs = new int[]{R.drawable.weather1, R.drawable.sun1, R.drawable.body1, R.drawable.body2, R.drawable.body3, R.drawable.body4};
    //private List<List<Integer>> mImageIDs = new ArrayList(){};
    LinearLayout sliders;
    ImageView[] dots;
    int dotCount;

    private int currentImageSet;

    ImageAdapter(Context context, int which) {

        //mImageIDs.add(Arrays.asList(R.drawable.weather1, R.drawable.sun1, R.drawable.body1, R.drawable.body2, R.drawable.body3, R.drawable.body4));
        //mImageIDs.add(Arrays.asList(R.drawable.calculator1, R.drawable.calculator2, R.drawable.calculator3));

        mContext = context;
        currentImageSet = which;
        Log.d("imageDebug", "ImageAdapter: ");


    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(mImageIDs[position]);
        container.addView(imageView);
        Log.d("imageDebug", "instantiateItem: " + imageView.getBaseline());
        return imageView;
    }

    @Override
    public int getCount() {
        return mImageIDs.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
