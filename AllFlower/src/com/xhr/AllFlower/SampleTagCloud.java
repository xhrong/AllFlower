package com.xhr.AllFlower;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.iflytek.iFramework.ui.tagcloud.Tag;
import com.iflytek.iFramework.ui.tagcloud.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class SampleTagCloud extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Step0: 得到一个全屏视图:
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Step1: 得到屏幕分辨率
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        //Step2: 创建所需的标记列表:
        //注意: 所有标签必须有独特的文本字段
        //如果没有,只有第一次出现将被添加,其余的将被忽略
        List<Tag> myTagList= createTags();

        //Step3: 创建TagCloudview MainActivity并将它设置为内容
        mTagCloudView = new TagCloudView(this, width, height, myTagList,new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(TagCloudView tagCloudView,View view, int position) {
                String data=tagCloudView.getTagCloud().get(position).getUrl();
                Toast.makeText(SampleTagCloud.this,data,Toast.LENGTH_LONG).show();
            }
        }); //通过当前上下文
        setContentView(mTagCloudView);
        mTagCloudView.requestFocus();
        mTagCloudView.setFocusableInTouchMode(true);
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    private List<Tag> createTags(){
        //创建的列表标记人气值和相关的url
        List<Tag> tempList = new ArrayList<Tag>();

        tempList.add(new Tag("Google", 7, "Google"));  //1,4,7,... 假定受欢迎的值
        tempList.add(new Tag("Yahoo", 3, "Yahoo"));
        tempList.add(new Tag("CNN", 4, "CNN"));
        tempList.add(new Tag("MSNBC", 5, "MSNBC"));
        tempList.add(new Tag("CNBC", 5, "CNBC"));
        tempList.add(new Tag("Facebook", 7, "Facebook"));
        tempList.add(new Tag("Youtube", 3, "Youtube"));
        tempList.add(new Tag("BlogSpot", 5, "BlogSpot"));
        tempList.add(new Tag("Bing", 3, "Bing"));
        tempList.add(new Tag("Wikipedia", 8, "Wikipedia"));
        tempList.add(new Tag("Twitter", 5, "Twitter"));
        tempList.add(new Tag("Msn", 1, "Msn"));
        tempList.add(new Tag("Amazon", 3, "Amazon"));
        tempList.add(new Tag("Ebay", 7, "Ebay"));
        tempList.add(new Tag("LinkedIn", 5, "LinkedIn"));
        tempList.add(new Tag("Live", 7, "Live"));
        tempList.add(new Tag("Microsoft", 3, "Microsoft"));
        tempList.add(new Tag("Flicker", 1, "Flicker"));
        tempList.add(new Tag("Apple", 5, "Apple"));
        tempList.add(new Tag("Paypal", 5, "Paypal"));
        tempList.add(new Tag("Craigslist", 7, "Craigslist"));
        tempList.add(new Tag("Imdb", 2, "Imdb"));
        tempList.add(new Tag("Ask", 4, "Ask"));
        tempList.add(new Tag("Weibo", 1, "Weibo"));
        tempList.add(new Tag("Tagin!", 8, "Tagin"));
        tempList.add(new Tag("Shiftehfar", 8, "Shiftehfar"));
        tempList.add(new Tag("Soso", 5, "Soso"));
        tempList.add(new Tag("XVideos", 3, "XVideos"));
        tempList.add(new Tag("BBC", 5, "BBC"));
        return tempList;
    }

    private TagCloudView mTagCloudView;
}