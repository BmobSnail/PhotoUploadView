# PhotoUploadView 1.0.0

@(图片选择、上传控件)
**PhotoUploadView **是一个图片选择、上传的自定义控件，通过精心的设计与技术实现，简化实现逻辑，让开发更效率简单。

-------------------
#### PhotoUploadView:1.0.0：
- 1.支持设置选择个数
- 2.是否显示上传进度（一般需求分两种）：
	  第一种，选择完图片就单张遍历上传，上传过程可能会导致某一张上传失败（需要显示上传结果的）
	  第二种，选择完图片，填写完内容，一次post的提交，批量上传
	  针对第一种情况，我为该控件添加了失败重发的机制。
- 3.选择后返回的都是压缩后的图片路径

####PhotoUploadView:1.1.0：
- 1.修改了运行时权限判断，支持 7.0 系统的使用
- 2.fragment 可以在 activity 的回调里面给 fragment 处理，具体参考 demo

在 Application 中设置缓存路径
```java
String cache = StorageUtils.getOwnCacheDirectory(this, "PictureUpload/photo").getAbsolutePath();
PictureLibrary.init(this, cache+"/");
```
在布局中添加
```xml
<upload.view.PictureUploadView
   android:id="@+id/pictureUploadView"
   android:numColumns="3"
   android:layout_margin="@dimen/partition_normal"
   android:verticalSpacing="1dp"
   android:horizontalSpacing="2dp"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   android:scrollbars="none"/>
```

在代码中使用
```java
mPictureUploadView = (PictureUploadView) findViewById(R.id.pictureUploadView);
//第一个参数上下文
//第二个参数有CHAT(返回原图路径)，UPLOAD(返回压缩路径)
//第三个参数选择个数
//第四个参数默认为true(可不填)，false为不显示上传进度
mPictureUploadView.init(this, UploadPicHelper.UPLOAD, 3, true);
mPictureUploadView.setShowMethod(PictureUploadView.POPUPWINDOW);//POPUPWINDOW，DIALOG
mPictureUploadView.setUploadCallBack(this);
```

最重要还是 result (如果不重写这一步，图片可能不会显示)
```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPictureUploadView.setResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```

其它回调逻辑详情查看 demo 的 MainActivity<br>

远程引用
>**compile 'com.github.BmobSnail:PhotoUploadView:1.0.0'**

## 感谢
本库依赖几个开源库
- compile 'me.iwf.photopicker:PhotoPicker:0.1.8'
- compile 'me.drakeet.materialdialog:library:1.2.8'
- compile 'com.commit451:PhotoView:1.2.4'
- compile 'com.github.bumptech.glide:glide:3.6.1'**


