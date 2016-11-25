package upload.inteface;

/**
 * author：created by Snail.江
 * time: 7/21/2016 11:33
 * email：409962004@qq.com
 * TODO:
 */
public interface BaseFunction {
	/** 初始化数据方法 */
	void initData();

	/** 初始化UI控件方法 */
	void initView();

	/** 初始化事件监听方法 */
	void initListener();

	/** 初始加载*/
	void initLoad();
}
