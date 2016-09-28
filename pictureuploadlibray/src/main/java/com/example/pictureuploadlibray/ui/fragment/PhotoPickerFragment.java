package com.example.pictureuploadlibray.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.pictureuploadlibray.R;

import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

/**
 * Created by Administrator on 2016/1/14.
 */
public class PhotoPickerFragment extends BaseFragment {

    private PhotoGridAdapter photoGridAdapter;
    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirectory> directories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_picker);
    }

    @Override
    public void initData() {
        super.initData();
        directories = new ArrayList<>();

        photoGridAdapter = new PhotoGridAdapter(getActivity(), directories);
        listAdapter  = new PopupDirectoryListAdapter(getActivity(), directories);

        MediaStoreHelper.getPhotoDirs(getActivity(),
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> directories) {
                        photoGridAdapter.notifyDataSetChanged();
                        PhotoPickerFragment.this.directories.addAll(directories);
                        listAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void initView() {
        super.initView();

        RecyclerView recyclerView = findViewByIdToView(R.id.rv_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Button btSwitchDirectory = findViewByIdToView(R.id.button);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(btSwitchDirectory);
        listPopupWindow.setAdapter(listAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();

                PhotoDirectory directory = directories.get(position);
                btSwitchDirectory.setText(directory.getName());
                photoGridAdapter.setCurrentDirectoryIndex(position);
                photoGridAdapter.notifyDataSetChanged();
            }
        });

        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else {
                    listPopupWindow.setHeight(Math.round(getActivity().getResources().getDisplayMetrics().heightPixels * 0.8f));
                    listPopupWindow.show();
                }
            }
        });

    }

    public PhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }

}
