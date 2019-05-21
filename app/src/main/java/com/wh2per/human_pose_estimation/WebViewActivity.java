package com.wh2per.human_pose_estimation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    private String myUrl = "http://192.168.0.25/upload/application/sk/upload.php";
    private ValueCallback<Uri> filePathCallbackNormal;
    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 1;
    private final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2;

    AdmobController aac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        aac = new AdmobController(this);


        // 웹뷰 셋팅팅
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser( ValueCallback<Uri> uploadMsg) {
                Log.d("MainActivity", "3.0 <");
                openFileChooser(uploadMsg, "");
            }
            // For Android 3.0+
            public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.d("MainActivity", "3.0+");
                filePathCallbackNormal = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                // i.setType("video/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_NORMAL_REQ_CODE);
            }
            // For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d("MainActivity", "4.1+");
                openFileChooser(uploadMsg, acceptType);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                Log.d("MainActivity", "5.0+");
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
                filePathCallbackLollipop = filePathCallback;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
               // i.setType("video/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE);

                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(myUrl);
    }
    // 뒤로가기 버튼
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();
        aac.runVideoAd();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FILECHOOSER_NORMAL_REQ_CODE) {
                if (filePathCallbackNormal == null) return ;
                Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
                filePathCallbackNormal.onReceiveValue(result);
                Log.e("Normal : ",""+ filePathCallbackNormal+"");
                filePathCallbackNormal = null;
            } else if (requestCode == FILECHOOSER_LOLLIPOP_REQ_CODE) {
                if (filePathCallbackLollipop == null) return ;
                filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                Log.e("file : ", resultCode + ", "+data);
                Log.e("Normal_LOl1 : ",""+ filePathCallbackLollipop+"");
                filePathCallbackLollipop = null;
            }
        } else {
            if (filePathCallbackLollipop != null) {
                filePathCallbackLollipop.onReceiveValue(null);
                Log.e("Normal_LOl2 : ",""+ filePathCallbackLollipop+"");
                filePathCallbackLollipop = null;
            }
        }

    }

}
