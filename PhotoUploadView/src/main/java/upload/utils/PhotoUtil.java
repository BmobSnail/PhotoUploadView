package upload.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import upload.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author：created by Snail.江
 * time: 7/26/2016 15:25
 * email：409962004@qq.com
 * TODO:
 */
public class PhotoUtil {

    public static final long FILE_MAX_SIZE = 80 * 1024;

    /**
     * 创建一个.png文件
     */
    public static String copyFileToLocalPath(String localPath) {
        if (!TextUtils.isEmpty(localPath)) {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                String cachePath = Constant.CACHE_PATH;
                File dir = new File(cachePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                return cachePath +
                        HexUtils.toHexString(localPath) +
                        ".png";
            }
        }
        return null;
    }

    /**
     * 文件复制功能
     */
    public static boolean copyFile(File inputFile, File outFile) {
        if (inputFile == null || outFile == null)
            return false;
        if ((!inputFile.isFile()) || (!outFile.isFile()))
            return false;
        try {
            return copyFile(new FileInputStream(inputFile),
                    new FileOutputStream(outFile));
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 文件复制功能
     */
    public static boolean copyFile(InputStream inputStream,OutputStream outPutStream) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer, 0, buffer.length) != -1) {
                outPutStream.write(buffer);
            }
        } catch (IOException e) {
            return false;
        } finally {
            inputStream.close();
            outPutStream.close();
        }
        return true;
    }

    /**
     * 压缩图片保存副本，返回副本路径
     */
    public static File compress(String localPath) {
        File outputFile = new File(localPath);
        long fileSize = outputFile.length();
        //大于80k继续压缩
        if (fileSize >= FILE_MAX_SIZE || localPath.contains("content")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localPath, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / FILE_MAX_SIZE);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
//            options.inDither = false;    /*不进行图片抖动处理*/
//            options.inPreferredConfig = null;  /*设置让解码器以最佳方式解码*/
//            options.inPurgeable = true;
//            options.inInputShareable = true;
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(localPath, options);
            outputFile = new File(copyFileToLocalPath(localPath));
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                } else {
                    File tempFile = outputFile;
                    outputFile = new File(copyFileToLocalPath(localPath));
                    PhotoUtil.copyFile(tempFile, outputFile);
                }

                deleteTempFile(localPath);
            }
        }
        return outputFile;
    }

    public static List<String> getCompressList(List<String> data) {
        List<String> images = new ArrayList<>();
        for (String path : data) {
            images.add(compress(path).getPath());
        }
        return images;
    }

    /**
     * 根据路径删除图片
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
