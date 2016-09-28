package com.example.pictureuploadlibray.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureuploadlibray.R;
import com.example.pictureuploadlibray.inteface.BaseFunction;

import java.util.List;


public abstract class BaseActivity extends AppCompatActivity implements
        BaseFunction, View.OnClickListener, AdapterView.OnItemClickListener {

    private long mExitTime;

    protected InputMethodManager imm = null;

    private boolean isExit = false;

    protected Fragment mCurrFragment;

    protected int mFrameId;

    private TextView mCenterTxt;
    private View mLeftTxt, mRightTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewCreate(savedInstanceState);

    }

    abstract protected void onViewCreate(Bundle savedInstanceState);

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
        initView();
        initListener();
        initLoad();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        openActivity(toActivity, parameter, -1000);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter, int code) {
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }

        if (code == -1000) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, code);
        }
    }


    protected void showLongToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    protected void openTransition(Boolean arg) {
        if (arg) {
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			if (getCurrentFocus() != null
//					&& getCurrentFocus().getWindowToken() != null) {
//				hideKeyboard();
//			}
//		}
        return super.onTouchEvent(event);
    }

    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    showShortToast("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void finish() {
        super.finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean isExit) {
        this.isExit = isExit;
    }

    @Override
    public void initData() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void initView() {
        setActionBar();
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initLoad() {
    }

    protected void toFragment(Fragment toFragment) {

        if (mCurrFragment == null) {
            showShortToast("mCurrFragment is null!");
            return;
        }

        if (toFragment == null) {
            showShortToast("toFragment is null!");
            return;
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment temp : fragments) {
                if (!temp.isHidden())
                    getSupportFragmentManager()
                            .beginTransaction()

                            .hide(temp)
                            .commit();
            }
        }

        if (toFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .hide(mCurrFragment)
                    .show(toFragment).commit();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(mCurrFragment)
                    .add(mFrameId, toFragment).show(toFragment)
                    .commit();
        }
        mCurrFragment = toFragment;
    }

    protected void setFrameId(int id) {
        mFrameId = id;
    }

    public void setCurrFragment(Fragment mCurrFragment) {
        this.mCurrFragment = mCurrFragment;
    }


    public <T extends View> T findViewByIdToView(int id) {
        return (T) super.findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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



    public void setFunctionView(View v, String content, int picId) {
        if (v != null && v instanceof TextView) {
            TextView target = (TextView) v;
            if (TextUtils.isEmpty(content)) {
                target.setText("");
            } else {
                v.setVisibility(View.VISIBLE);
                target.setText(content);
            }

            if (picId == -1) {
                target.setCompoundDrawables(null, null, null, null);
            } else {
                v.setVisibility(View.VISIBLE);
                if (v.equals(mLeftTxt)) {
                    target.setCompoundDrawablesWithIntrinsicBounds(picId, 0, 0, 0);
                } else if (v.equals(mRightTxt)) {
                    target.setCompoundDrawablesWithIntrinsicBounds(0, 0, picId, 0);
                }

            }
        }
    }

    public void setTitle(String title) {
        if (mCenterTxt != null) {
            mCenterTxt.setText(title);
            mCenterTxt.setVisibility(View.VISIBLE);
        }
    }

    public TextView getCenterTxt() {
        return mCenterTxt;
    }

    public View getLeft() {

        return mLeftTxt;
    }

    public View getRight() {

        return mRightTxt;
    }
}
