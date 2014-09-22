package com.xhr.AllFlower;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.iflytek.iFramework.logger.Logger;
import com.iflytek.iFramework.ui.universalimageloader.core.DisplayImageOptions;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoader;
import com.iflytek.iFramework.ui.universalimageloader.core.assist.FailReason;
import com.iflytek.iFramework.ui.universalimageloader.core.assist.ImageSize;
import com.iflytek.iFramework.ui.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xhr.AllFlower.model.FlowerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private List<FlowerInfo> flowerInfoList = new ArrayList<FlowerInfo>();

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.noimage)
            .showImageForEmptyUri(R.drawable.noimage)
            .showImageOnLoading(R.drawable.noimage)
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .build();

    private ImageView ivFlowerImage;
    private TextView tvFlowerDescription;
    private TextView tvFlowerTitle;
    private TextView tvFlowerStory;
    private  ImageView ivNextFlower;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        initData();
        showFlower();

        ivNextFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFlower();
            }
        });
    }

    private FlowerInfo getFlower(){
        Random random=new Random();
        return flowerInfoList.get(random.nextInt(2));
    }

    private void showFlower(){
        FlowerInfo flowerInfo=getFlower();
        ImageSize targetSize = new ImageSize(100, 80);
        imageLoader.loadImage(flowerInfo.getThumbnailUrl(), targetSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ivFlowerImage.setImageBitmap(loadedImage);
                ivFlowerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                Logger.i("sss", "ddd");
            }
        });
        tvFlowerTitle.setText(flowerInfo.getTitle());
        tvFlowerStory.setText(flowerInfo.getStory());
        tvFlowerDescription.setText(flowerInfo.getDescription());
    }

    private void initView(){
        ivFlowerImage = (ImageView) findViewById(R.id.ivFlowerImage);
        tvFlowerStory=(TextView) findViewById(R.id.tvFlowerStory);
        tvFlowerTitle=(TextView) findViewById(R.id.tvFlowerTitle);
        tvFlowerDescription=(TextView) findViewById(R.id.tvFlowerDescription);
        ivNextFlower=(ImageView)findViewById(R.id.ivNextFlower);
    }

    private void initData() {
        String[] urls = {
                "http://b.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26%3Bt%3Dgif/sign=8830b492d11373f0e13267cdc566209e/b3b7d0a20cf431ad929c6fd04b36acaf2fdd9888.jpg",
                "http://b.hiphotos.baidu.com/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50%3Bt%3Dgif/sign=7f5381b8fffaaf5190ee89eded3dff8b/fd039245d688d43f36dcb86c7f1ed21b0ef43b32.jpg",
        };
        String[] descriptions={
                "合欢花（拉丁学名：Albizzia julibrissin Durazz.）是豆科，合欢属落叶乔木，喜温暖湿润和阳光充足环境，气微香，味淡。\n" +
                        "产中国东北至华南及西南部各省区。生于山坡或栽培。非洲、中亚至东亚均有分布；北美亦有栽培。\n" +
                        "合欢花花丝粉红色，荚果偏，是城市行道树、观赏树。心材黄灰褐色，边材黄白色，耐久，多用于制家具；嫩叶可食，老叶可以洗衣服；树皮供药用，有驱虫之效。它还有宁神作用，主要是治郁结胸闷、失眠健忘、滋阴补阳、眼疾、神经衰弱等功效",
                "彼岸花的日文别名叫做\"曼珠沙华\"，是来自于<<法华经>>中梵语\"摩诃曼珠沙华\"的音译。原意为天上之花，大红花，是天降吉兆四华（曼珠沙华、摩诃曼殊沙华、曼陀罗华、摩诃曼陀罗华）之一，典称见此花者，恶自去除。"
        };
        String[] titles={
                "合欢花",
                "彼岸花"
        };
        String[] stories={
                "相传虞舜南巡仓梧而死，其妃娥皇、女英遍寻湘江，终未寻见。二妃终日恸哭，泪尽滴血，血尽而死，逐为其神。后来，人们发现她们的精灵与虞舜的精灵“合二为一”，变成了合欢树。合欢树叶，昼开夜合，相亲相爱。自此，人们常以合欢表示忠贞不渝的爱情。[6] ",
                "彼岸花，花开一千年，花落一千年，花叶生生相错，世世永不相见。彼岸花开开彼岸，奈何桥前可奈何？走向死亡国度的人，就是踏着这凄美的花朵通向幽冥之狱。\n" +
                        "　　彼岸花学名“红花石蒜”，是单子叶植物纲百合目石蒜科石蒜属植物，英文学名“Lycoris radiata”，“Lycoris”一词是来自与西腊神话中海之女神的名字，而“radita”则表示辐射状的意思，用来形容花的外型。除红色外还有白色、黄色等品种。\n" +
                        "　　“彼岸花，开彼岸，只见花，不见叶”。\n" +
                        "　　曼珠沙华这个名字出自梵语「摩诃曼珠沙华」，梵语意为开在天界的大红花。天降吉兆，是天界四华之一。佛典中也说曼陀罗华是天上开的花，白色而柔软，见此花者，恶自去除。\n" +
                        "　　相传此花只开于黄泉，是黄泉路上唯一的风景。\n" +
                        "　　传说彼岸花是恶魔的温柔。自愿投入地狱的花朵，被众魔遣回，但仍徘徊于黄泉路上，众魔不忍，遂同意让她开在此路上，给离开人界的亡魂们一个指引与安慰。（此处与下文神话传说中的地藏菩萨段落并无冲突，仔细阅读可知，或见编者的话，请勿删去。）认为是生长在忘川河边的接引之花，是冥界唯一的花。在那儿大批大批的开着这花，远远看上去就像是血所铺成的地毯，又因其红的似火而被喻为”火照之路”。也是这长长黄泉路上唯一的风景与色彩，人们就踏着这花的指引通向幽冥之狱。因此又意为死亡之花。\n" +
                        "　　相传花香有魔力，能唤起死者生前的记忆。佛家语，荼蘼是花季最后盛开的花，开到荼蘼花事了，只剩下开在遗忘前生的彼岸的花。佛经记载有“彼岸花，开一千年，落一千年，花叶永不相见。情不为因果，缘注定生死。”"
        };
        for (int i = 0; i < 2; i++) {
            FlowerInfo flowerInfo = new FlowerInfo();
            flowerInfo.setDescription(descriptions[i]);
            flowerInfo.setStory(stories[i]);
            flowerInfo.setTitle(titles[i]);
            flowerInfo.setThumbnailUrl(urls[i]);
            flowerInfoList.add(flowerInfo);
        }
    }
}

