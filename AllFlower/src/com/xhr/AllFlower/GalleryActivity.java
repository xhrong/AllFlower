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
import java.util.Collections;
import java.util.List;

/**
 * Created by xhrong on 2014/9/18.
 */
public class GalleryActivity extends Activity {
    private GalleryViewPager mViewPager;

    private int currPos = 0;

    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();

    String[] urls = {
            "http://img2.imgtn.bdimg.com/it/u=1345199435,233003775&fm=23&gp=0.jpg",
            "http://pic15.nipic.com/20110701/2786001_093006163000_2.jpg", //special url with error
            "http://pic21.nipic.com/20120511/5624330_220540429000_2.jpg",
            "http://pic16.nipic.com/20110926/7675514_071636011000_2.jpg",
            "http://pic10.nipic.com/20101004/3320946_144729002948_2.jpg",
            "http://pic6.nipic.com/20100425/736442_111601071747_2.jpg"
    };

    private ImageView ivDownload;
    private TextView tvImageDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery);
        initData();
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                currPos = currentPosition;
                tvImageDescription.setText(imageInfoList.get(currPos).getDescription());
            }
        });
        mViewPager = (GalleryViewPager) findViewById(R.id.gvImageViewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnItemClickListener(new GalleryViewPager.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                DescriptionAnim();
            }
        });
        initView();
    }

    private void initView() {
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
        String[] descriptions = {
                "葡萄（拉丁学名：Albizzia julibrissin Durazz.）是豆科，合欢属落叶乔木，喜温暖湿润和阳光充足环境，气微香，味淡。\n" +
                        "产中国东北至华南及西南部各省区。生于山坡或栽培。非洲、中亚至东亚均有分布；北美亦有栽培。\n" +
                        "合欢花花丝粉红色，荚果偏，是城市行道树、观赏树。心材黄灰褐色，边材黄白色，耐久，多用于制家具；嫩叶可食，老叶可以洗衣服；树皮供药用，有驱虫之效。它还有宁神作用，主要是治郁结胸闷、失眠健忘、滋阴补阳、眼疾、神经衰弱等功效",
                "",
                "",
                "彼岸花的日文别名叫做\"曼珠沙华\"，是来自于<<法华经>>中梵语\"摩诃曼珠沙华\"的音译。原意为天上之花，大红花，是天降吉兆四华（曼珠沙华、摩诃曼殊沙华、曼陀罗华、摩诃曼陀罗华）之一，典称见此花者，恶自去除。",
                "",
                "",
        };
        String[] titles = {
                "葡萄",
                "葡萄",
                "葡萄",
                "葡萄",
                "葡萄",
                "葡萄"
        };
        for (int i = 0; i < 6; i++) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setDescription(descriptions[i]);
            imageInfo.setTitle(titles[i]);
            imageInfo.setUrl(urls[i]);
            imageInfoList.add(imageInfo);
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
