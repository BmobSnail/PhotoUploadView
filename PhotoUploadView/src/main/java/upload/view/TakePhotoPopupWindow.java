package upload.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.snail.upload.R;

import upload.utils.UploadPicHelper;

/**
 * author：created by Snail.江
 * time: 7/26/2016 17:24
 * email：409962004@qq.com
 * TODO:
 */
public class TakePhotoPopupWindow extends PopupWindow {

    private int count = 1;

    public void setCount(int count) {
        this.count = count;
    }

    public TakePhotoPopupWindow(final Context context,final UploadPicHelper mHelper) {
        super(context);

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup_takephoto, null);
        layout.findViewById(R.id.popup_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.selectPicFromCamera();
                dismiss();
            }
        });
        layout.findViewById(R.id.popup_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.selectPicFromLocal(count);
                dismiss();
            }
        });
        layout.findViewById(R.id.popup_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(layout);
        this.setTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popupWindow_anim_style);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.update();
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity)context).getWindow().setAttributes(lp);
            }
        });
    }
}
