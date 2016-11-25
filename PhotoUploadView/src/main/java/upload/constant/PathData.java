package upload.constant;

/**
 * author：created by Snail.江
 * time: 7/22/2016 12:22
 * email：409962004@qq.com
 * TODO:
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
