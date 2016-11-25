package upload.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import upload.ui.activity.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author：created by Snail.江
 * time: 7/22/2016 15:24
 * email：409962004@qq.com
 * TODO:
 */
public class UploadPicHelper {

    public final static int CHAT = 1;
    public final static int UPLOAD = 2;

    public final static int REQUEST_CODE_LOCAL = 3;
    public final static int REQUEST_CODE_CAMERA = 4;
    public final static int REQUEST_CODE_CROP = 5;

    private Activity activity;
    private Context context;
    private SelectPicListener selectPicListener;
    private SendPicListener sendPicListener;

    private int mode = CHAT;

    private String photoPath;

    public UploadPicHelper(Activity activity, int mode) {
        this.activity = activity;
        this.context = activity;
        this.mode = mode;
    }

    /**
     * 拍照获取图片
     */
    public void selectPicFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(PhotoUtil.copyFileToLocalPath(HexUtils.toHexString(System.currentTimeMillis() + "")));
            file.getParentFile().mkdir();
            Uri mOutPutFileUri = Uri.fromFile(file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
            activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
            photoPath = mOutPutFileUri.getPath();
        } else {
            Toast.makeText(activity, "没有找到储存卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 裁剪图片
     */
    public void selectCropFromLocal() {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, 1);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public void selectPicFromLocal(int count) {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, count);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
        activity.startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            List<String> photos = new ArrayList<>();
            switch (requestCode) {
                case REQUEST_CODE_LOCAL://本地图片
                    if (data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS) != null) {
                        photos.clear();
                        photos.addAll(data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS));
                        selectPicListener.onAlbumPictures(PhotoUtil.getCompressList(photos));//返回压缩后的图片集合
                    } else {
                        String path = data.getData().toString();
                        photos.clear();
                        photos.add(path);
                        selectPicListener.onAlbumPictures(PhotoUtil.getCompressList(photos));//返回压缩后的图片集合
                    }
                    break;

                case REQUEST_CODE_CAMERA://拍照图片
                    if (!TextUtils.isEmpty(photoPath)) {
                        PhotoUtil.galleryAddPic(activity, photoPath);
                        switch (mode) {
                            case CHAT:
                                if (sendPicListener != null) {
                                    sendPicListener.onSendPic(photoPath);
                                }
                                break;
                            case UPLOAD:
                                if (selectPicListener != null) {
                                    //没被压缩返回的图片路径
                                    String path = PhotoUtil.compress(photoPath).getPath();
                                    selectPicListener.onCameraPicture(path);
                                }
                                break;
                        }
                    }
                    break;

                case REQUEST_CODE_CROP://裁剪图片
                    photos.clear();
                    photos.addAll(data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS));
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    File file = new File(photos.get(0));
                    Uri mOutPutFileUri = Uri.fromFile(file);
                    intent.setDataAndType(mOutPutFileUri, "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 256);
                    intent.putExtra("outputY", 256);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", false); // 返回数据bitmap
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                    intent.putExtra("noFaceDetection", true); // no face detection
                    activity.startActivityForResult(intent, REQUEST_CODE_LOCAL);
                    break;
            }
        }
    }


    public interface SelectPicListener {
        void onCameraPicture(String picturePath);

        void onAlbumPictures(List<String> pictureData);
    }

    public interface SendPicListener {
        void onSendPic(String picturePath);
    }

    public SelectPicListener getSelectPicListener() {
        return selectPicListener;
    }

    public void setSelectPicListener(SelectPicListener selectPicListener) {
        this.selectPicListener = selectPicListener;
    }

    public void setSendPicListener(SendPicListener sendPicListener) {
        this.sendPicListener = sendPicListener;
    }
}
