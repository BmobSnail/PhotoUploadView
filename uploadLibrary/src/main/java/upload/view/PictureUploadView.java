package upload.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.snail.upload.R;

import upload.constant.PathData;
import upload.utils.UploadPicHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * author：created by Snail.江
 * time: 7/22/2016 18:24
 * email：409962004@qq.com
 * TODO:
 */
public class PictureUploadView extends GridView implements UploadPicHelper.SelectPicListener {

    public static final int POPUPWINDOW = 0;
    public static final int DIALOG = 1;

    private Context context;

    private UploadPicHelper mHelper;

    private List<PathData> mPaths;

    private PicUploadAdapter mAdapter;

    private UploadCallBack mCallBack;

    private HashMap<String, ProgressBar> mAllProgressBarMap;

    private HashMap<String, ImageView> mAllResendViewMap;

    private TakePhotoDialog takePhotoDialog;

    private TakePhotoPopupWindow takePhotoPopupWindow;

    private int size, count, showMethod = POPUPWINDOW;

    private boolean showProgress;

    public PictureUploadView(Context context) {
        super(context, null);
    }

    public PictureUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAllProgressBarMap = new HashMap<>();
        mAllResendViewMap = new HashMap<>();
        mPaths = new ArrayList<>();

        this.context = context;
        mAdapter = new PicUploadAdapter();
        setAdapter(mAdapter);
    }

    public void init(Activity activity, int mode, int count) {
        init(activity, mode, count, true);
    }

    public void init(Activity activity, int mode, int count, boolean showsProgress) {
        init(activity, mode, count, showsProgress, mPaths);
    }

    public void init(Activity activity, int mode, int count, boolean showsProgress, List<PathData> mPaths) {
        if (mHelper == null) {
            mHelper = new UploadPicHelper(activity, mode);
            mHelper.setSelectPicListener(this);
            this.showProgress = showsProgress;
            this.mPaths = mPaths;
            this.count = count;
            size = count;
            setShowMethod(showMethod);
        }
    }

    public void setShowMethod(int showMethod) {
        this.showMethod = showMethod;
        switch (showMethod) {
            case 0:
                if (takePhotoPopupWindow == null) {
                    takePhotoPopupWindow = new TakePhotoPopupWindow(context, mHelper);
                    takePhotoPopupWindow.setCount(count);
                }
                break;

            case 1:
                if (takePhotoDialog == null) {
                    takePhotoDialog = new TakePhotoDialog(context, mHelper);
                    takePhotoDialog.setCount(count);
                }
                break;
        }
    }

    private void setSelectedCount(int count) {
        switch (showMethod) {
            case 0:
                if (takePhotoPopupWindow == null) {
                    takePhotoPopupWindow = new TakePhotoPopupWindow(context, mHelper);
                    takePhotoPopupWindow.setCount(count);
                } else {
                    takePhotoPopupWindow.setCount(count);
                }
                break;

            case 1:
                if (takePhotoDialog == null) {
                    takePhotoDialog = new TakePhotoDialog(context, mHelper);
                    takePhotoDialog.setCount(count);
                } else {
                    takePhotoDialog.setCount(count);
                }
                break;
        }
    }


    public void setResult(int requestCode, int resultCode, Intent data) {
        mHelper.onResult(requestCode, resultCode, data);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onCameraPicture(String picturePath) {
        String tag = System.currentTimeMillis() + picturePath;
        PathData pd = new PathData(picturePath, tag);
        mPaths.add(pd);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlbumPictures(List<String> pictureData) {
        for (String path : pictureData) {
            String tag = System.currentTimeMillis() + path;
            PathData pd = new PathData(path, tag);
            mPaths.add(pd);
        }
        mAdapter.notifyDataSetChanged();
    }


    private class PicUploadAdapter extends BaseAdapter {

        final int ADD_PIC = 11;
        final int COMMON_PIC = 22;

        PicUploadAdapter() {
        }

        @Override
        public int getCount() {
            return mPaths.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getCount() - 1) {
                return ADD_PIC;
            }
            return COMMON_PIC;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            int type = getItemViewType(position);
            OnFunctionClickListener listener = new OnFunctionClickListener(position);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.common_pic, parent, false);

                viewHolder.addLayout = (LinearLayout) convertView.findViewById(R.id.item_add_contain);
                viewHolder.imageCardView = (CardView) convertView.findViewById(R.id.item_pic_contain);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.pic);
                viewHolder.cancel = (ImageView) convertView.findViewById(R.id.pic_cancel);
                viewHolder.resend = (ImageView) convertView.findViewById(R.id.pic_resend);
                viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.pic_pro);

                if (!showProgress) viewHolder.progress.setVisibility(GONE);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            switch (type) {
                case ADD_PIC:
                    listener.setIds(1);
                    viewHolder.addLayout.setOnClickListener(listener);
                    viewHolder.addLayout.setVisibility(VISIBLE);
                    viewHolder.imageCardView.setVisibility(GONE);
                    break;

                case COMMON_PIC:
                    if (mPaths.get(position).path.contains("http://")) {
                        Glide.with(context).load(mPaths.get(position).path).asBitmap()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.color.half_transparent)
                                .error(R.color.half_transparent)
                                .into(viewHolder.image);
                        viewHolder.cancel.setVisibility(VISIBLE);
                        viewHolder.resend.setVisibility(GONE);
                        viewHolder.progress.setVisibility(GONE);
                    } else {
                        Glide.with(context).load("file://" + mPaths.get(position).path).asBitmap()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.color.half_transparent)
                                .error(R.color.half_transparent)
                                .into(viewHolder.image);
                    }
                    listener.setIds(0);
                    viewHolder.cancel.setOnClickListener(listener);
                    viewHolder.imageCardView.setVisibility(VISIBLE);
                    if (!showProgress) viewHolder.progress.setVisibility(GONE);
                    if (parent.getChildCount() == 0 || position > 0) {
                        if (!mAllProgressBarMap.containsKey(mPaths.get(position).tag)) {
                            mAllProgressBarMap.put(mPaths.get(position).tag, viewHolder.progress);
                            mAllResendViewMap.put(mPaths.get(position).tag, viewHolder.resend);

                            //添加图片的回调
                            if (mCallBack != null) {
                                setSelectedCount(--count);
                                mCallBack.onAddCallback(mPaths.get(position).path, mPaths.get(position).tag);
                            }
                        }
                    }
                    break;
            }

            if (size - count == size) {
                viewHolder.addLayout.setVisibility(GONE);
            }

            return convertView;
        }


        private class OnFunctionClickListener implements OnClickListener {

            private int position;
            private int ids = -1;

            OnFunctionClickListener(int position) {
                this.position = position;
            }

            void setIds(int ids) {
                this.ids = ids;
            }

            @Override
            public void onClick(View v) {
                switch (this.ids) {
                    case 1:
                        if (mHelper != null) {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm.isActive()) {
                                imm.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            switch (showMethod) {
                                case 0:
                                    takePhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                                    break;
                                case 1:
                                    takePhotoDialog.show();
                                    break;
                                default:
                                    takePhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                                    break;
                            }

                            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                            lp.alpha = 0.7f;
                            ((Activity) context).getWindow().setAttributes(lp);
                        }
                        break;

                    case 0:
                        for (Iterator<PathData> it = mPaths.iterator(); it.hasNext(); ) {
                            PathData pd = it.next();
                            if (pd.tag.equals(mPaths.get(position).tag)) {
                                it.remove();
                                mAllProgressBarMap.remove(pd.tag);
                                mAllResendViewMap.remove(pd.tag);
                                notifyDataSetChanged();

                                //删除已选图片的回调
                                if (mCallBack != null) {
                                    setSelectedCount(++count);
                                    mCallBack.onRemoveCallback(pd.tag);
                                }
                                break;
                            }
                        }
                        break;
                }
            }
        }
    }


    //添加图片和删除图片的回调
    public interface UploadCallBack {
        void onAddCallback(String path, String tag);
        void onRemoveCallback(String tag);
    }

    public void setUploadCallBack(UploadCallBack callback) {
        this.mCallBack = callback;
    }

    public class ViewHolder {
        ImageView image;
        ImageView cancel;
        ImageView resend;
        ProgressBar progress;
        LinearLayout addLayout;
        CardView imageCardView;
    }

    public HashMap<String, ProgressBar> getAllProgressView() {
        return mAllProgressBarMap;
    }

    public HashMap<String, ImageView> getAllResendView() {
        return mAllResendViewMap;
    }

    public ProgressBar getProgressBar(String path) {
        getAllProgressView();
        return mAllProgressBarMap.get(path);
    }

    public ImageView getResend(String path) {
        getAllResendView();
        return mAllResendViewMap.get(path);
    }
}
