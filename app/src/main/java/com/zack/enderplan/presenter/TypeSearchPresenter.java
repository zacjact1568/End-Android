package com.zack.enderplan.presenter;

import android.text.TextUtils;

import com.zack.enderplan.model.DataManager;
import com.zack.enderplan.model.bean.Type;
import com.zack.enderplan.view.adapter.TypeSearchListAdapter;
import com.zack.enderplan.view.contract.TypeSearchViewContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TypeSearchPresenter extends BasePresenter {

    private TypeSearchViewContract mTypeSearchViewContract;
    private DataManager mDataManager;
    private List<Type> mTypeSearchList;
    private TypeSearchListAdapter mTypeSearchListAdapter;

    @Inject
    TypeSearchPresenter(TypeSearchViewContract typeSearchViewContract, DataManager dataManager) {
        mTypeSearchViewContract = typeSearchViewContract;
        mDataManager = dataManager;

        mTypeSearchList = new ArrayList<>();

        mTypeSearchListAdapter = new TypeSearchListAdapter(mDataManager, mTypeSearchList);
        mTypeSearchListAdapter.setOnTypeItemClickListener(new TypeSearchListAdapter.OnTypeItemClickListener() {
            @Override
            public void onTypeItemClick(int typeListPos) {
                mTypeSearchViewContract.onTypeItemClicked(typeListPos);
            }
        });
    }

    @Override
    public void attach() {
        mTypeSearchViewContract.showInitialView(mDataManager.getTypeCount(), mTypeSearchListAdapter);
    }

    @Override
    public void detach() {
        mTypeSearchViewContract = null;
    }

    public void notifySearchTextChanged(String searchText) {
        mDataManager.searchType(mTypeSearchList, searchText);
        mTypeSearchListAdapter.notifyDataSetChanged();
        mTypeSearchViewContract.onSearchChanged(TextUtils.isEmpty(searchText), mTypeSearchList.size() == 0);
    }
}