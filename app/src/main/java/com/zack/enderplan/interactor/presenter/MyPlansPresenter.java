package com.zack.enderplan.interactor.presenter;

import com.zack.enderplan.model.bean.Plan;
import com.zack.enderplan.event.DataLoadedEvent;
import com.zack.enderplan.event.PlanCreatedEvent;
import com.zack.enderplan.event.PlanDeletedEvent;
import com.zack.enderplan.event.PlanDetailChangedEvent;
import com.zack.enderplan.event.RemindedEvent;
import com.zack.enderplan.event.TypeDetailChangedEvent;
import com.zack.enderplan.interactor.adapter.PlanAdapter;
import com.zack.enderplan.model.DataManager;
import com.zack.enderplan.domain.view.MyPlansView;
import com.zack.enderplan.utility.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MyPlansPresenter extends BasePresenter implements Presenter<MyPlansView> {

    private MyPlansView mMyPlansView;
    private DataManager mDataManager;
    private PlanAdapter mPlanAdapter;
    private EventBus mEventBus;

    public MyPlansPresenter(MyPlansView myPlansView) {
        mEventBus = EventBus.getDefault();
        attachView(myPlansView);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void attachView(MyPlansView view) {
        mMyPlansView = view;
        mEventBus.register(this);
    }

    @Override
    public void detachView() {
        mMyPlansView = null;
        mEventBus.unregister(this);
    }

    public void setInitialView() {
        //初始化adapter
        mPlanAdapter = new PlanAdapter(mDataManager.getPlanList(), mDataManager.getTypeCodeAndColorResMap());

        mMyPlansView.showInitialView(mPlanAdapter);

        //开始从数据库加载数据
        mDataManager.loadFromDatabase();
    }

    public void notifyPlanItemClicked(int position) {
        mMyPlansView.onPlanItemClicked(position);
    }

    public void notifyStarMarkClicked(int position) {
        mDataManager.notifyStarStatusChanged(position);
        mPlanAdapter.notifyItemChanged(position);
    }

    //滑动删除时（可以撤销）
    public void notifyPlanDeleted(int position) {

        //必须先把要删除计划的引用拿到
        Plan plan = mDataManager.getPlan(position);

        mDataManager.notifyPlanDeleted(position);
        mPlanAdapter.notifyItemRemoved(position);

        Util.makeShortVibrate();

        mEventBus.post(new PlanDeletedEvent(getPresenterName(), plan.getPlanCode(), position, plan));
    }

    public void notifyPlanStatusChanged(int position) {
        Plan plan = mDataManager.getPlan(position);
        //首先检测此计划是否有提醒
        if (plan.getReminderTime() != 0) {
            mDataManager.notifyReminderTimeChanged(position, 0);
            mPlanAdapter.notifyItemChanged(position);
            mEventBus.post(new PlanDetailChangedEvent(getPresenterName(), plan.getPlanCode(), position, PlanDetailChangedEvent.FIELD_REMINDER_TIME));
        }
        //执行以下语句时，只是在view上让position处的plan删除了，实际上还未被删除但也即将被删除
        //NOTE: 不能用notifyItemRemoved，会没有效果
        mPlanAdapter.notifyItemRemoved(position);
        mDataManager.notifyPlanStatusChanged(position);
        //这里，plan的状态已经更新
        int newPosition = plan.isCompleted() ? mDataManager.getUcPlanCount() : 0;
        mPlanAdapter.notifyItemInserted(newPosition);
        //发送事件，更新其他组件
        mEventBus.post(new PlanDetailChangedEvent(getPresenterName(), plan.getPlanCode(), newPosition, PlanDetailChangedEvent.FIELD_PLAN_STATUS));
    }

    @Subscribe
    public void onDataLoaded(DataLoadedEvent event) {
        mPlanAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onPlanCreated(PlanCreatedEvent event) {
        if (event.getEventSource().equals(getPresenterName())) return;
        //可能会报错
        mPlanAdapter.notifyItemInserted(event.getPosition());
        //mPlanAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onTypeDetailChanged(TypeDetailChangedEvent event) {
        List<Integer> singleTypeUcPlanPosList = mDataManager.getSingleTypeUcPlanLocations(event.getTypeCode());
        for (int position : singleTypeUcPlanPosList) {
            //所有属于这个类型的计划都需要刷新
            mPlanAdapter.notifyItemChanged(position);
        }
    }

    @Subscribe
    public void onPlanDetailChanged(PlanDetailChangedEvent event) {
        if (event.getEventSource().equals(getPresenterName())) return;
        if (event.getChangedField() == PlanDetailChangedEvent.FIELD_PLAN_STATUS) {
            //有完成情况的改变，直接全部刷新
            mPlanAdapter.notifyDataSetChanged();
        } else {
            //普通、类型改变的刷新
            mPlanAdapter.notifyItemChanged(event.getPosition());
        }
    }

    @Subscribe
    public void onPlanDeleted(PlanDeletedEvent event) {
        if (event.getEventSource().equals(getPresenterName())) return;
        mPlanAdapter.notifyItemRemoved(event.getPosition());
    }

    @Subscribe
    public void onReminded(RemindedEvent event) {
        mPlanAdapter.notifyItemChanged(event.getPosition());
    }
}
