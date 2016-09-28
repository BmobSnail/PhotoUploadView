package com.example.administrator.picuploadtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.pictureuploadlibray.utils.UploadPicHelper;
import com.example.pictureuploadlibray.view.PictureUploadView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements PictureUploadView.UploadCallBack {

    public static final int UPLOAD_SUCCEED = 200;
    public static final int UPLOAD_FAILED = 400;

    private PictureUploadView mPictureUploadView;
    private Map<String, String> mUploadMap, mLocalMap;


    /**图片上传回调操作
     *
     * 成功：隐藏图片进度
     * 失败：隐藏图片进度，显示重发按钮，并设置对应重发事件
     * */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_SUCCEED:
                    if (mUploadMap != null &&
                            mPictureUploadView != null &&
                            mPictureUploadView.getProgressBar((String) msg.obj) != null)
                        mPictureUploadView.getProgressBar((String) msg.obj).setVisibility(View.GONE);
                    break;

                case UPLOAD_FAILED:
                    if (mUploadMap != null &&
                            mPictureUploadView != null &&
                            mPictureUploadView.getProgressBar((String) msg.obj) != null){
                        mPictureUploadView.getProgressBar((String) msg.obj).setVisibility(View.GONE);
                        if (mPictureUploadView.getResend((String) msg.obj) != null){
                            mPictureUploadView.getResend((String) msg.obj).setVisibility(View.VISIBLE);
                            mPictureUploadView.getResend((String) msg.obj).setOnClickListener(new ResendListener(((String) msg.obj)));
                        }
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUploadMap = new HashMap<>();
        mLocalMap = new HashMap<>();

        mPictureUploadView = (PictureUploadView) findViewById(R.id.pictureUploadView);
        mPictureUploadView.init(this, UploadPicHelper.UPLOAD, 3);
        mPictureUploadView.setShowMethod(PictureUploadView.DIALOG);
        mPictureUploadView.setUploadCallBack(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPictureUploadView.setResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //获取所有上传成功的图片路径
    private String getAllPicture() {
        String pic = "";
        Set<String> set = mUploadMap.keySet();
        for (String key : set) {
            pic = pic + mUploadMap.get(key) + ",";
        }
        return pic.equals("") ? null : pic.substring(0, pic.length() - 1);
    }

    //添加图片
    @Override
    public void onAddCallback(String path, String tag) {
        mLocalMap.put(tag, path);

        upload(path,tag);
    }


    //删除图片
    @Override
    public void onRemoveCallback(String tag) {
        mUploadMap.remove(tag);
        mLocalMap.remove(tag);
    }

    public class ResendListener implements View.OnClickListener {

        String tag;

        ResendListener(String tag) {
            this.tag = tag;
        }

        @Override
        public void onClick(View v) {
            mPictureUploadView.getProgressBar(tag).setVisibility(View.VISIBLE);
            mPictureUploadView.getResend(tag).setVisibility(View.GONE);

            upload(mLocalMap.get(tag),tag);
        }
    }


    /**模拟网络上传图片
     *
     * path 图片本地uri
     * tag 图片标识
     */
    private void upload(String path,final String tag){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUploadMap.put(tag, "上传图片成功返回的可访问图片路径");

                Message msg = Message.obtain();
                msg.what = System.currentTimeMillis() % 2 == 0 ? UPLOAD_FAILED : UPLOAD_SUCCEED;
                msg.obj = tag;
                handler.sendMessage(msg);
            }
        },3000);
    }
}
