package com.zack.enderplan.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zack.enderplan.R;
import com.zack.enderplan.bean.Plan;
import com.zack.enderplan.util.LogUtil;

import java.util.List;

public class PlanSingleTypeAdapter extends RecyclerView.Adapter<PlanSingleTypeAdapter.ViewHolder> {

    private List<Plan> planList;

    public PlanSingleTypeAdapter(List<Plan> planList) {
        this.planList = planList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_single_type, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.contentText.setText(plan.getContent());
        holder.checkBox.setOnCheckedChangeListener(null);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentText;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            contentText = (TextView) itemView.findViewById(R.id.text_content);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
        }
    }
}