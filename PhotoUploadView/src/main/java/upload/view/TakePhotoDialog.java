package upload.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.snail.upload.R;

import upload.utils.UploadPicHelper;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * author：created by Snail.江
 * time: 7/27/2016 20:00
 * email：409962004@qq.com
 * TODO:
 */
public class TakePhotoDialog extends MaterialDialog {

    private int count = 1;

    public void setCount(int count) {
        this.count = count;
    }

    public TakePhotoDialog(Context context, final UploadPicHelper mHelper) {
        super(context);

        LinearLayout dialogPic = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_takephoto, null);
        dialogPic.findViewById(R.id.dialog_pic_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.selectPicFromCamera();
                dismiss();
            }
        });
        dialogPic.findViewById(R.id.dialog_pic_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.selectPicFromLocal(count);
                dismiss();
            }
        });
        dialogPic.findViewById(R.id.dialog_pic_default).setVisibility(View.GONE);
        this.setCanceledOnTouchOutside(true);
        this.setTitle("选择图片");
        this.setContentView(dialogPic);
        this.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
