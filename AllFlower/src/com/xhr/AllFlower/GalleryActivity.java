package com.xhr.AllFlower;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iflytek.iFramework.http.asynhttp.AsyncHttpClient;
import com.iflytek.iFramework.http.asynhttp.AsyncHttpResponseHandler;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.BasePagerAdapter;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.GalleryViewPager;
import com.iflytek.iFramework.ui.touchgallery.GalleryWidget.UrlPagerAdapter;
import com.xhr.AllFlower.model.ImageInfo;
import com.xhr.AllFlower.utils.ImageDownloader;
import org.apache.http.Header;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ImageButton imgBtnShiTu;

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
        imgBtnShiTu = (ImageButton) findViewById(R.id.imgBtnShiTu);
        imgBtnShiTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiTu(imageInfoList.get(currPos).getUrl());
            }
        });
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
    }

    private String getData(String sourceStr, String pattern) {
        //  String filetext = "张小名=25分|李小花=43分|王力=100分|";
        Pattern p = Pattern.compile(pattern);//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher m = p.matcher(sourceStr);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    private void shiTu(String imageURL) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        try {
            String apiUrl = "http://stu.baidu.com/i?objurl=" + imageURL + "&filename=&rt=0&rn=10&ftn=searchstu&ct=1&stt=1&tn=faceresult";
            asyncHttpClient.get(apiUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    String data = new String(responseBody);
                    Log.i("SHITU", data);
                    String name = getData(data, "keyword:BD.STU.escapeXSS\\(\\\"(.*)\\\"\\),keywordEnc");
                    String baikeUrl = getData(data, "baikeURL:\\\"(.*)\\\",baikeText");
                    String baikeText = getData(data, "baikeText:\\\"(.*)\\\",baikeTitle");
                    Log.i("SHITU", name);
                    Log.i("SHITU", baikeUrl);
                    Log.i("SHITU", baikeText);
                    showShiTuResult(name, baikeText, baikeUrl);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                }
            });
        } catch (Exception e) {
        }
    }

    private void showShiTuResult(final String name, final String baikeText, final String baikeUrl) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.pop_shitu);

        LinearLayout shiTuResultLayout = (LinearLayout) window.findViewById(R.id.shiTuResultLayout);
        String[] results = name.split("\\*");

        for (int i = 0; i < results.length; i++) {
            final String lname=results[i];
            TextView tvName = new TextView(this);
            tvName.setText(" "+lname+" ");
            tvName.setTextSize(25);

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = "http://image.baidu.com/i?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=" + URLEncoder.encode(lname, "utf-8");
                        Uri uri = Uri.parse(url);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);

                    } catch (Exception e) {

                    }
                }
            });

            shiTuResultLayout.addView(tvName);
        }
        TextView tvBaiKeText = (TextView) window.findViewById(R.id.tvBaiKeText);
        tvBaiKeText.setText(baikeText);
        tvBaiKeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(baikeUrl);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }
}