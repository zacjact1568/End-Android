package com.zack.enderplan.presenter;

import com.zack.enderplan.activity.CalendarDialogFragment;
import com.zack.enderplan.activity.DateTimePickerDialogFragment;
import com.zack.enderplan.bean.Plan;
import com.zack.enderplan.database.EnderPlanDB;
import com.zack.enderplan.manager.DataManager;
import com.zack.enderplan.manager.ReminderManager;
import com.zack.enderplan.util.Util;
import com.zack.enderplan.view.CreatePlanView;
import com.zack.enderplan.widget.TypeSpinnerAdapter;

public class CreatePlanPresenter implements Presenter<CreatePlanView> {

    private CreatePlanView createPlanView;
    private DataManager dataManager;
    private Plan plan;

    public CreatePlanPresenter(CreatePlanView createPlanView) {
        attachView(createPlanView);
        dataManager = DataManager.getInstance();
        plan = new Plan(Util.makeCode());
    }

    @Override
    public void attachView(CreatePlanView view) {
        createPlanView = view;
    }

    @Override
    public void detachView() {
        createPlanView = null;
    }

    public TypeSpinnerAdapter createTypeSpinnerAdapter() {
        return new TypeSpinnerAdapter(dataManager.getTypeList(), dataManager.getTypeMarkAndColorResMap());
    }

    public void notifyContentChanged(String newContent) {
        plan.setContent(newContent);
    }

    public void notifyTypeCodeChanged(int posInSpinner) {
        plan.setTypeCode(dataManager.getType(posInSpinner).getTypeCode());
    }

    public void notifyDeadlineChanged(long newDeadline) {
        plan.setDeadline(newDeadline);
    }

    public void notifyReminderTimeChanged(long newReminderTime) {
        plan.setReminderTime(newReminderTime);
    }

    public void notifyStarStatusChanged() {
        //isStarred表示点击之前的星标状态
        boolean isStarred = plan.getStarStatus() == Plan.PLAN_STAR_STATUS_STARRED;
        plan.setStarStatus(isStarred ? Plan.PLAN_STAR_STATUS_NOT_STARRED : Plan.PLAN_STAR_STATUS_STARRED);
        //星标状态变化了
        createPlanView.onStarStatusChanged(!isStarred);
    }

    public void createDeadlineDialog() {
        CalendarDialogFragment deadlineDialog = CalendarDialogFragment.newInstance(plan.getDeadline());
        createPlanView.onCreateDeadlineDialog(deadlineDialog);
    }

    public void createReminderDialog() {
        DateTimePickerDialogFragment reminderDialog = DateTimePickerDialogFragment.newInstance(plan.getReminderTime());
        createPlanView.onCreateReminderDialog(reminderDialog);
    }

    public void createNewPlan() {
        plan.setCreationTime(System.currentTimeMillis());
        //处理并向model层传送数据，更新model
        dataManager.addToPlanList(0, plan);
        //更新未完成计划的数量
        dataManager.updateUcPlanCount(1);
        //设置提醒
        setReminder(plan.getPlanCode(), plan.getReminderTime());
        //更新每个类型具有的计划数量map
        dataManager.updatePlanCountOfEachType(plan.getTypeCode(), 1);
        //存储至数据库
        EnderPlanDB.getInstance().savePlan(plan);
    }

    private void setReminder(String planCode, long reminderTime) {
        if (reminderTime != 0) {
            ReminderManager manager = new ReminderManager();
            manager.setAlarm(planCode, reminderTime);
        }
    }
}