package upload.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * author：created by Snail.江
 * time: 12/15/2016 16:40
 * email：409962004@qq.com
 * TODO: 权限工具类
 */
class PermissionUtil {

    public static String[] requirePermission = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private static List<String> lacksPermission;

    public static boolean checkLacksPermission(Activity context, String... params) {
        if (lacksPermission == null)
            lacksPermission = new ArrayList<>();
        lacksPermission.clear();

        for (String psm : params) {
            if (ContextCompat.checkSelfPermission(context, psm) == PackageManager.PERMISSION_DENIED) {
                lacksPermission.add(psm);
            }
        }

        if (lacksPermission.size() != 0) {
            applyPermission(context, lacksPermission.toArray(new String[lacksPermission.size()]));
            return false;
        } else {
            return true;
        }
    }

    private static void applyPermission(final Activity context, final String[] requirePermission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context)
                    .setMessage("你需要启动权限才能使用该功能")
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, requirePermission, 100);//请求开启权限
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(context, requirePermission, 100);
        }
    }


}
