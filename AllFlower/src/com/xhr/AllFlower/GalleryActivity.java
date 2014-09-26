package com.xhr.AllFlower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.BasePagerAdapter;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.GalleryViewPager;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.UrlPagerAdapter;
import com.xhr.AllFlower.model.ImageInfo;
import com.xhr.AllFlower.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhrong on 2014/9/18.
 */
public class GalleryActivity extends Activity {
    private GalleryViewPager mViewPager;

    private int currPos = 0;
    private String flowerName = "";
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    private List<String> imageUrlList = new ArrayList<String>();

    private ImageView ivDownload;
    private TextView tvImageDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        initData();
        initView();
    }

    private void initView() {
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, imageUrlList);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                currPos = currentPosition;
                tvImageDescription.setText(imageInfoList.get(currPos).getDescription());
            }
        });
        mViewPager = (GalleryViewPager) findViewById(R.id.gvImageViewer);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(currPos);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnItemClickListener(new GalleryViewPager.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                DescriptionAnim();
            }
        });
        ivDownload = (ImageView) findViewById(R.id.ivDownload);
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageDownloader.getInstance().downloadFile(imageInfoList.get(currPos).getUrl());
            }
        });
        tvImageDescription = (TextView) findViewById(R.id.tvImageDescription);
        tvImageDescription.setText(imageInfoList.get(currPos).getDescription());
    }


    private void initData() {

        imageInfoList = (List<ImageInfo>) getIntent().getSerializableExtra("imagelist");
        currPos = getIntent().getIntExtra("pos", 0);
        flowerName = getIntent().getStringExtra("flowername");
        for (ImageInfo ii : imageInfoList) {
            imageUrlList.add(ii.getUrl());
        }
    }

    private void DescriptionAnim() {
        Animation animation;
        if (tvImageDescription.getVisibility() == View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(this, R.anim.gallery_description_hide);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvImageDescription.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tvImageDescription.startAnimation(animation);
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.gallery_description_show);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tvImageDescription.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tvImageDescription.startAnimation(animation);

        }

    }
}
