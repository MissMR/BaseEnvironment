package com.base.baseenvironment.baseFragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


import com.base.baseenvironment.R;
import com.base.baseenvironment.service.NetService;
import com.base.baseenvironment.utils.FileUtils;
import com.base.baseenvironment.utils.MyUtils;
import com.base.baseenvironment.utils.NetUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public abstract class BaseWebFragment extends BaseFragment {

    public static WebView web;
    public static String url  ;
    private ValueCallback<Uri> mUploadFile;
    private ValueCallback<Uri[]> mUploadFile1;
    private  final int REQUEST_UPLOAD_FILE_CODE = 100001;
    public static LinearLayout ll_img; // 网络状态不好的遮挡图案
    protected  void setUrl(String url){
        this.url = url;
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_web;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startIntent = new Intent(getMyActivity(), NetService.class);
        getMyActivity().startService(startIntent);
    }

    @Override
    public boolean onBackPressed() {
        if (web.canGoBack()){
            web.goBack();
            return true;
        }
        return false;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ll_img = (LinearLayout) view.findViewById(R.id.ll_img);
        web = (WebView) view.findViewById(R.id.webview5);
        // webview 一些基本设置
        MyUtils.setWebView(web, getMyActivity(), url);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

        });
        web.setWebChromeClient(new XHSWebChromeClient());
    }


    /**
     * 选择相册和头像回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPLOAD_FILE_CODE
                && resultCode == getActivity().RESULT_OK) {
            if (null == mUploadFile && mUploadFile1 == null)
                return;
            Uri result = data == null || resultCode != getActivity().RESULT_OK ? null
                    : data.getData();
            if (result == null) {
                mUploadFile.onReceiveValue(null);
                mUploadFile = null;
                return;
            }
            // CLog.i("UPFILE", "onActivityResult" + result.toString());
            String path = FileUtils.getPath(getActivity(), result);
            if (TextUtils.isEmpty(path)) {
                mUploadFile.onReceiveValue(null);
                mUploadFile = null;
                return;
            }

            File f = handleFile(new File(path));
            boolean s = f.exists();
            Uri uri;
            if (s) {
                uri = Uri.fromFile(handleFile(new File(path)));
            } else {
                uri = Uri.fromFile(new File(path));
            }
            // CLog.i("UPFILE", "onActivityResult after parser uri:" +
            // uri.toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadFile1.onReceiveValue(new Uri[] { uri });
            } else {
                mUploadFile.onReceiveValue(uri);
            }

            mUploadFile = null;
        } else {
            if (mUploadFile != null) {
                mUploadFile.onReceiveValue(null);
            }

            if (mUploadFile1 != null) {
                mUploadFile1.onReceiveValue(null);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     *  获取本地图片，并缩放
     * @param file
     * @return
     */
    private File handleFile(File file) {
        DisplayMetrics dMetrics = getResources().getDisplayMetrics();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        System.out.println("  imageWidth = " + imageWidth + " imageHeight = "
                + imageHeight);
        int widthSample = (int) (imageWidth / (dMetrics.density * 50));
        int heightSample = (int) (imageHeight / (dMetrics.density * 50));
        System.out.println("widthSample = " + widthSample + " heightSample = "
                + heightSample);
        options.inSampleSize = widthSample < heightSample ? heightSample
                : widthSample;
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                options);
        System.out.println("newBitmap.size = " + newBitmap.getRowBytes()
                * newBitmap.getHeight());
        File handleFile = new File(file.getParentFile(), "upload.jpg");
        try {
            if (newBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    new FileOutputStream(handleFile))) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return handleFile;
    }




    /**
     *  设置webview不同版本头像上传
     */
    public class XHSWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadFile = uploadMsg;
            if (mUploadFile == null) {
                mUploadFile.onReceiveValue(null);
            }
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    REQUEST_UPLOAD_FILE_CODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

            if (mUploadFile != null) {
                mUploadFile.onReceiveValue(null);
            }
            mUploadFile = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    REQUEST_UPLOAD_FILE_CODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            if (mUploadFile != null) {
                mUploadFile.onReceiveValue(null);
            }
            mUploadFile = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    REQUEST_UPLOAD_FILE_CODE);
        }

        // Android 5.0+
        @Override
        @SuppressLint("NewApi")
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {

            mUploadFile1 = filePathCallback;
            if (mUploadFile != null) {
                mUploadFile.onReceiveValue(null);
            }
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            if (fileChooserParams != null
                    && fileChooserParams.getAcceptTypes() != null
                    && fileChooserParams.getAcceptTypes().length > 0) {
                String s = fileChooserParams.getAcceptTypes()[0];
                i.setType("*/*");
            } else {
                i.setType("*/*");
            }
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    REQUEST_UPLOAD_FILE_CODE);
            return true;
        }
    }

    @Override
    public void onDestroy() {
        web.destroy();
        Intent stopIntent = new Intent(getMyActivity(), NetService.class);
        getMyActivity().stopService(stopIntent);
        super.onDestroy();
    }

    public static class NetReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean hasNet = NetUtils.isConnected(context);
            if (!hasNet) {
                ll_img.setVisibility(View.VISIBLE);

            } else {
                ll_img.setVisibility(View.GONE);
                web.loadUrl(url);
            }
        }
    }

}
