package com.xhr.AllFlower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.iflytek.iFramework.http.synhttp.SynHttpClient;
import com.iflytek.iFramework.ui.pulltorefresh.PullToRefreshBase;
import com.iflytek.iFramework.ui.pulltorefresh.PullToRefreshGridView;
import com.iflytek.iFramework.ui.slidingmenu.SlidingMenu;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoader;
import com.iflytek.iFramework.ui.universalimageloader.core.assist.FailReason;
import com.iflytek.iFramework.ui.universalimageloader.core.listener.ImageLoadingListener;
import com.iflytek.iFramework.utils.StringUtils;
import com.xhr.AllFlower.model.ImageInfo;
import com.xhr.AllFlower.utils.ScreenUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xhrong on 2014/9/23.
 */
public class MainActivity extends Activity {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private PullToRefreshGridView pullToRefreshGridView;
    private GridView gridView;
    private SearchView svSearch;

    private ListView lvFlowerName;
    private Button btnRandom;
    SlidingMenu menu;

    private PullToRefreshAdapter adapter;
    private List<ImageInfo> imageList = new ArrayList<ImageInfo>();
    private int curPage = 1;
    private String curFlowerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setCurrentFlowerName(AppConstants.DEFAULT_FLOWER_NAME);
        initSlidingMenu();
        svSearch = (SearchView) findViewById(R.id.svSearch);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                imageList.clear();
                if (StringUtils.isEmpty(s)) {
                    setCurrentFlowerName(AppConstants.DEFAULT_FLOWER_NAME);
                } else
                    setCurrentFlowerName(s);
                new GetDataTask().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.ptrgvList);
        gridView = pullToRefreshGridView.getRefreshableView();
        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                Toast.makeText(MainActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                Toast.makeText(MainActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
                new GetDataTask().execute();

            }
        });


        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        pullToRefreshGridView.setEmptyView(tv);
        adapter = new PullToRefreshAdapter(imageList, this);
        gridView.setAdapter(adapter);
        new GetDataTask().execute();
    }

    private void initSlidingMenu() {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        menu.setBehindWidth(screenWidth / 3);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidingmenu);

        lvFlowerName = (ListView) findViewById(R.id.lvFlowerName);
        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        menuAdapter.addAll(AppConstants.FLOWER_NAMES);
        lvFlowerName.setAdapter(menuAdapter);

        lvFlowerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageList.clear();
                setCurrentFlowerName(AppConstants.FLOWER_NAMES[i]);
                new GetDataTask().execute();
            }
        });

        btnRandom = (Button) findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                setCurrentFlowerName(AppConstants.FLOWER_NAMES[random.nextInt(AppConstants.FLOWER_NAMES.length)]);
                imageList.clear();
                new GetDataTask().execute();
            }
        });

        ((Button) findViewById(R.id.btnMore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.showMenu();
            }
        });
    }

    private void setCurrentFlowerName(String flowerName) {
        this.curFlowerName = flowerName;
        ((TextView) findViewById(R.id.tvCurrFlowerName)).setText(curFlowerName);
    }

    public class PullToRefreshAdapter extends BaseAdapter {

        List<ImageInfo> list;
        Context context;
        private Drawable drawable;

        public PullToRefreshAdapter(List<ImageInfo> list, Context context) {
            this.list = list;
            this.context = context;
            drawable = context.getResources().getDrawable(R.drawable.noimage);
        }


        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup group) {
            final Holder holder;
            if (view == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.image_item, null);
                holder.ivIcon = (ImageView) view.findViewById(R.id.row_icon);
                holder.pbLoad = (ProgressBar) view.findViewById(R.id.pb_load);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            String url = list.get(position).getThumbUrl();
            //  Object iconData = holder.ivIcon.getTag();
            //   if (iconData == null || StringUtils.isEmpty(iconData.toString()) || !iconData.toString().equals(url)) {
            holder.ivIcon.setTag(position);
            imageLoader.displayImage(url, holder.ivIcon, AppConstants.IMAGE_LOADER_DISPLAY_OPTIONS,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.ivIcon.setImageDrawable(drawable);
                            holder.pbLoad.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "Input/Output error";
                                    break;
                                case DECODING_ERROR:
                                    message = "can not be decoding";
                                    break;
                                case NETWORK_DENIED:
                                    message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "内存不足";
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                            .show();
                                    break;
                                case UNKNOWN:
                                    message = "Unknown error";
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                            .show();
                                    break;
                            }
                            holder.pbLoad.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            holder.pbLoad.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String paramString,
                                                       View paramView) {
                        }
                    }
            );
            //   }


            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imagelist", (Serializable) imageList);
                    bundle.putString("flowername", curFlowerName);
                    int pos = 0;
                    try {
                        pos = Integer.parseInt(view.getTag().toString());
                    } catch (Exception e) {

                    }
                    Log.i("POS", Integer.toString(pos));
                    bundle.putInt("pos", pos);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return view;
        }



    }


    class Holder {
        public ImageView ivIcon;
        public ProgressBar pbLoad;
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<ImageInfo>> {

        @Override
        protected List<ImageInfo> doInBackground(Void... params) {
            List<ImageInfo> result = new ArrayList<ImageInfo>();
            try {
                String url = AppConstants.GET_FLOWER_BY_NAME_URL
                        .replace("{flowername}", URLEncoder.encode(curFlowerName, "utf-8"))
                        .replace("{page}", Integer.toString(curPage));
                String data = SynHttpClient.sendGet(url, null);
                if (StringUtils.isEmpty(data) || "null".equals(data)) {
                    return result;
                }
                JSONObject jsonImgs = new JSONObject(data);
                JSONArray arrData = jsonImgs.getJSONArray("data");
                int length = arrData.length();
                for (int i = 0; i < length; i++) {
                    ImageInfo flowerInfo = new ImageInfo();
                    JSONObject oj = arrData.getJSONObject(i);
                    flowerInfo.setThumbUrl(oj.getString("thumbURL"));
                    flowerInfo.setUrl(oj.getString("objURL"));
                    flowerInfo.setTitle(oj.getString("fromPageTitle"));
                    result.add(flowerInfo);
                }
            } catch (JSONException je) {

            } catch (Exception e) {

            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(List<ImageInfo> result) {
            imageList.addAll(result);
            adapter.notifyDataSetChanged();
            curPage++;
            pullToRefreshGridView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}
