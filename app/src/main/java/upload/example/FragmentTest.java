package upload.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import upload.utils.UploadPicHelper;
import upload.view.PictureUploadView;

/**
 * author：created by Snail.江
 * time: 12/15/2016 18:05
 * email：409962004@qq.com
 * TODO:
 */
public class FragmentTest extends Fragment implements PictureUploadView.UploadCallBack {

    public static final int UPLOAD_SUCCEED = 200;
    public static final int UPLOAD_FAILED = 400;

    private PictureUploadView mPictureUploadView;
    private Map<String, String> mUploadMap, mLocalMap;

    /**
     * 图片上传回调操作
     *
     * 成功：隐藏图片进度
     * 失败：隐藏图片进度，显示重发按钮，并设置对应重发事件
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_SUCCEED:
                    if (mUploadMap != null &&
                            mPictureUploadView != null &&
                            mPictureUploadView.getProgressBar((String) msg.obj) != null)
                        mPictureUploadView.getProgressBar((String) msg.obj).setVisibility(View.GONE);
                    if (mPictureUploadView.getResend((String) msg.obj) != null) {
                        mPictureUploadView.getResend((String) msg.obj).setVisibility(View.GONE);
                    }
                    break;

                case UPLOAD_FAILED:
                    if (mUploadMap != null &&
                            mPictureUploadView != null &&
                            mPictureUploadView.getProgressBar((String) msg.obj) != null) {
                        mPictureUploadView.getProgressBar((String) msg.obj).setVisibility(View.GONE);
                        if (mPictureUploadView.getResend((String) msg.obj) != null) {
                            mPictureUploadView.getResend((String) msg.obj).setVisibility(View.VISIBLE);
                            mPictureUploadView.getResend((String) msg.obj).setOnClickListener(new FragmentTest.ResendListener(((String) msg.obj)));
                        }
                    }
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_main,null);
        mUploadMap = new HashMap<>();
        mLocalMap = new HashMap<>();

        mPictureUploadView = (PictureUploadView) view.findViewById(R.id.pictureUploadView);
        //第一个参数上下文
        //第二个参数有CHAT(返回原图路径)，UPLOAD(返回压缩路径)
        //第三个参数选择个数
        //第四个参数默认为true(可不填)，false为不显示上传进度
        mPictureUploadView.init(getActivity(), UploadPicHelper.UPLOAD, 3, true);
        mPictureUploadView.setShowMethod(PictureUploadView.POPUPWINDOW);//POPUPWINDOW，DIALOG
        mPictureUploadView.setUploadCallBack(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            mPictureUploadView.setResult(requestCode, resultCode, data);
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
        upload(path, tag);
    }


    //删除图片
    @Override
    public void onRemoveCallback(String tag) {
        mUploadMap.remove(tag);
        mLocalMap.remove(tag);
    }

    //失败重发事件
    public class ResendListener implements View.OnClickListener {

        String tag;

        ResendListener(String tag) {
            this.tag = tag;
        }

        @Override
        public void onClick(View v) {
            mPictureUploadView.getProgressBar(tag).setVisibility(View.VISIBLE);
            mPictureUploadView.getResend(tag).setVisibility(View.GONE);

            upload(mLocalMap.get(tag), tag);
        }
    }


    /**
     * 模拟网络上传图片
     *
     * path 图片本地uri
     * tag 图片标识
     * 随机模拟成功失败的上传状态
     */
    private void upload(String path, final String tag) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUploadMap.put(tag, "上传图片成功返回的可访问的完整路径file(path)--->imageUrl");

                Message msg = Message.obtain();
//                msg.what = new Random(10).nextInt() % 2 == 0 ? UPLOAD_SUCCEED : UPLOAD_FAILED;
                msg.what = UPLOAD_FAILED;
                msg.obj = tag;
                handler.sendMessage(msg);
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                System.out.println("权限申请成功");
            } else {
                System.out.println("权限申请失败");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
