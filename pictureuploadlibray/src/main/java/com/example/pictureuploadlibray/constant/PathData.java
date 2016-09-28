package com.example.pictureuploadlibray.constant;

/**
 * Created by Administrator on 2016/7/22.
 */
public class PathData {
    public String path;
    public String tag;

    public PathData(String path, String tag) {
        this.path = path;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
