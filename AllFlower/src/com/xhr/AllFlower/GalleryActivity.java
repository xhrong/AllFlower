package com.xhr.AllFlower;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
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
import com.tietuku.entity.main.PostImage;
import com.tietuku.entity.token.Token;
import com.xhr.AllFlower.model.ImageInfo;
import com.xhr.AllFlower.utils.ImageDownloader;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xhrong on 2014/9/18.
 */
public class GalleryActivity extends Activity implements Handler.Callback{
    private GalleryViewPager mViewPager;

    private int currPos = 0;
    private String flowerName = "";
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    private List<String> imageUrlList = new ArrayList<String>();

    private ImageView ivDownload;
    private ImageButton imgBtnShiTu;
    private ImageButton imgBtnLocalShiTu;

    private  Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        handler=new Handler(this);
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

        imgBtnLocalShiTu = (ImageButton) findViewById(R.id.imgBtnLocalShiTu);
        imgBtnLocalShiTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letCamera();
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
            Log.e("eee",e.getMessage());
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
            final String lname = results[i];
            TextView tvName = new TextView(this);
            tvName.setText(" " + lname + " ");
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



    private void uploadImage(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = Token.createToken(new Date().getTime() + 3600, 15348, "{\"height\":\"h\",\"width\":\"w\",\"s_url\":\"url\"}");
                String filePath = fileName;//Environment.getExternalStorageDirectory() + "/dlion/20141023121127.jpg";
                String result = PostImage.doUpload(new File(filePath), token);
                Message msg=new Message();
                msg.what=10;
                msg.obj=result;
                handler.sendMessage(msg);

            }
        }).start();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            bitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
            FileOutputStream fout = null;
            File file = new File(Environment.getExternalStorageDirectory() + "/dlion");
            file.mkdirs();
            final String filename = file.getPath() + "/" + name;

            try {
                fout = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //显示图片
            uploadImage(filename);
        }
    }

    protected void letCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 1);
    }

    @Override
    public boolean handleMessage(Message message) {
        try {
            String result=message.obj.toString();
            JSONObject jobj = new JSONObject(result);
            shiTu(jobj.getString("url"));
            Log.i("UPLOAD IMAGE", result);
        } catch (Exception e) {

        }
        return false;
    }
}