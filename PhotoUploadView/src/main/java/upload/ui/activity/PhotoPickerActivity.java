package upload.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.snail.upload.R;

import upload.ui.fragment.PhotoPickerFragment;
import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.OnItemCheckListener;


/**
 * author：created by Snail.江
 * time: 7/21/2016 11:24
 * email：409962004@qq.com
 * TODO:
 */
public class PhotoPickerActivity extends BaseActivity {

    private PhotoPickerFragment pickerFragment;

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    public final static int DEFAULT_MAX_COUNT = 9;

    private int maxCount = DEFAULT_MAX_COUNT;

    private boolean showCamera, showGif;//显示照相机按钮,显示gif图


    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_photo);
    }

    @Override
    public void initData() {
        super.initData();
        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        setShowGif(showGif);
    }

    @Override
    public void initView() {
        super.initView();

        setTitle("选择图片");
        setFunctionView(getLeft(), null, R.mipmap.icon_back);

        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPickerFragment);
        pickerFragment.getPhotoGridAdapter().setShowCamera(showCamera);

        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int selectedItemCount) {

                int total = selectedItemCount + (isCheck ? -1 : 1);

                if (maxCount == 1) {
                    List<Photo> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo)) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    setFunctionView(getRight(), "完成", -1);
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(getActivity(), getString(R.string.picker_over_max_count_tips, maxCount),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                setFunctionView(getRight(), getString(R.string.picker_done_with_count, total, maxCount), -1);

                if (total == 0) {
                    setFunctionView(getRight(), "", -1);
                }

                return true;
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        getLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activities when it complete.
     */
    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

}
