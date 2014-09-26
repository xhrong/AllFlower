package com.xhr.AllFlower.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import com.iflytek.iFramework.http.asynhttp.AsyncHttpClient;
import com.iflytek.iFramework.http.asynhttp.BinaryHttpResponseHandler;
import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xhrong on 2014/9/18.
 */
public class ImageDownloader {

    private  static ImageDownloader imageDownloader;

    public static ImageDownloader getInstance(){
        if(imageDownloader==null)
            imageDownloader=new ImageDownloader();
        return imageDownloader;
    }

    /**
     * @param url 要下载的文件URL
     * @throws Exception
     */
    public  void downloadFile(String url){

        AsyncHttpClient client = new AsyncHttpClient();
// 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
// 获取二进制数据如图片和其他文件
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                String tempPath = Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
                // TODO Auto-generated method stub
                // 下载成功后需要做的工作

                Log.d("binaryData:", "共下载了：" + binaryData.length);
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
                        binaryData.length);
                File file = new File(tempPath);
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                int quality = 100;
                try {
                    if (file.exists())
                        file.delete();
                    file.createNewFile();
                    OutputStream stream = new FileOutputStream(file);
                    bmp.compress(format, quality, stream);
                    stream.close();

//                    Toast.makeText(, "下载成功" + tempPath,
//                            Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                // TODO Auto-generated method stub
//                Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 下载进度显示
                //       progress.setProgress(count);
                Log.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);

            }

            @Override
            public void onRetry(int retryNo) {
                // TODO Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }

        });

    }
}
