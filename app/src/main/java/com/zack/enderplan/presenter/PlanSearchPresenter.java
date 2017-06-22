package com.zack.enderplan.presenter;

import android.text.TextUtils;

import com.zack.enderplan.model.DataManager;
import com.zack.enderplan.model.bean.Plan;
import com.zack.enderplan.view.adapter.PlanSearchListAdapter;
import com.zack.enderplan.view.contract.PlanSearchViewContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlanSearchPresenter extends BasePresenter {

    private PlanSearchViewContract mPlanSearchViewContract;
    private DataManager mDataManager;
    private List<Plan> mPlanSearchList;
    private PlanSearchListAdapter mPlanSearchListAdapter;

    @Inject
    PlanSearchPresenter(PlanSearchViewContract planSearchViewContract, DataManager dataManager) {
        mPlanSearchViewContract = planSearchViewContract;
        mDataManager = dataManager;

        mPlanSearchList = new ArrayList<>();

        mPlanSearchListAdapter = new PlanSearchListAdapter(mDataManager, mPlanSearchList);
        mPlanSearchListAdapter.setOnPlanItemClickListener(new PlanSearchListAdapter.OnPlanItemClickListener() {
            @Override
            public void onPlanItemClick(int planListPos) {
                mPlanSearchViewContract.onPlanItemClicked(planListPos);
            }
        });
    }

    @Override
    public void attach() {
        mPlanSearchViewContract.showInitialView(mDataManager.getPlanCount(), mPlanSearchListAdapter);
    }

    @Override
    public void detach() {
        mPlanSearchViewContract = null;
    }

    public void notifySearchTextChanged(String searchText) {
        mDataManager.searchPlan(mPlanSearchList, searchText);
        mPlanSearchListAdapter.notifyDataSetChanged();
        mPlanSearchViewContract.onSearchChanged(TextUtils.isEmpty(searchText), mPlanSearchList.size() == 0);
    }
}
