package com.example.pictureuploadlibray.ui.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureuploadlibray.R;
import com.example.pictureuploadlibray.ui.activity.BaseActivity;
import com.example.pictureuploadlibray.inteface.BaseFunction;


public abstract class BaseFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener, BaseFunction,
		OnDismissListener {

	FragmentCallBack callBack;

	/** 界面视图 */
	protected View contentView;

	public String TAG;

	private TextView mLeftTxt, mCenterTxt, mRightTxt;

	private Bundle savedInstanceState;


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(context instanceof FragmentCallBack){
			this.callBack = (FragmentCallBack)context;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = getClass().getSimpleName();
		this.savedInstanceState = savedInstanceState;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		initData();
		initView();
		initListener();
		initLoad();
		return contentView;

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void openActivity(Class<? extends BaseActivity> toActivity) {
		openActivity(toActivity, null);
	}

	protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
		openActivity(toActivity, parameter, -1000);

	}

	protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter, int code) {
		Intent intent = new Intent(getContext(), toActivity);
		if (parameter != null) {
			intent.putExtras(parameter);
		}

		if(code == -1000){
			startActivity(intent);
		}else{
			startActivityForResult(intent, code);
		}
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(getActivity(), pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(getActivity(), pMsg, Toast.LENGTH_SHORT).show();
	}

	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	protected <T> void openService(Class<T> cls) {
		openService(cls, null);
	}

	protected <T> void openService(Class<T> cls, Bundle bundle) {
		Intent intent = new Intent(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		getActivity().startService(intent);
	}

	protected <T> void stopService(Class<T> cls) {
		Intent intent = new Intent(getActivity(), cls);
		getActivity().stopService(intent);
	}

	/**
	 * 设置界面视图
	 * 
	 * @param id
	 *            视图的资源id
	 * */
	public void setContentView(int id) {
		this.contentView = getActivity().getLayoutInflater().inflate(id, null);
	}

	protected <T extends View> T findViewByIdToView(int id) {
		return (T)contentView.findViewById(id);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
//		setActionBar();
	}

	@Override
	public void initListener() {
	}

	public void onDismiss(DialogInterface dialog) {

	}

	@Override
	public void initLoad() {
		
	}
	
	public void finish(){
		getActivity().finish();
	}

	public View getContentView() {
		return contentView;
	}

	public void setContentView(View contentView) {
		this.contentView = contentView;
	}
	

	public interface FragmentCallBack{
		void callBack(Object... arg);
	}

	protected FragmentCallBack getCallBack() {
		return callBack;
	}

	public boolean setActionBar() {
		try {
			mLeftTxt = findViewByIdToView(R.id.actionBar_left_txt);
			mCenterTxt = findViewByIdToView(R.id.actionBar_center_txt);
			mRightTxt = findViewByIdToView(R.id.actionBar_right_txt);

			mLeftTxt.setOnClickListener(this);
			mRightTxt.setOnClickListener(this);

			mLeftTxt.setVisibility(View.GONE);
			mCenterTxt.setVisibility(View.GONE);
			mRightTxt.setVisibility(View.GONE);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setFunctionView(TextView v, String content, int picId) {
		if(v != null){
			if(TextUtils.isEmpty(content)){
				v.setText("");
			}else{
				v.setVisibility(View.VISIBLE);
				v.setText(content);
			}

			if(picId == -1){
				v.setCompoundDrawables(null,null,null,null);
			}else{
				v.setVisibility(View.VISIBLE);
				if(v.equals(mLeftTxt)){
					v.setCompoundDrawablesWithIntrinsicBounds(picId, 0, 0, 0);
				}else if(v.equals(mRightTxt)){
					v.setCompoundDrawablesWithIntrinsicBounds(0, 0, picId, 0);
				}

			}
		}
	}

	public void setTitle(String title){
		if(mCenterTxt != null){
			mCenterTxt.setText(title);
			mCenterTxt.setVisibility(View.VISIBLE);
		}
	}

	public TextView getCenterTxt() {
		return mCenterTxt;
	}

	public TextView getLeft() {
		return mLeftTxt;
	}

	public TextView getRight() {
		return mRightTxt;
	}


	public LayoutInflater getLayoutInflater() {
		return super.getLayoutInflater(savedInstanceState);
	}
}
