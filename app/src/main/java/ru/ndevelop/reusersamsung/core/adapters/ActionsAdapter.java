package ru.ndevelop.reusersamsung.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.interfaces.OnActionClickListener;
import ru.ndevelop.reusersamsung.core.objects.Action;
import ru.ndevelop.reusersamsung.utils.ActionTypes;
import ru.ndevelop.reusersamsung.utils.Utils;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.SingleViewHolder>  {
    private Context mContext;
    private OnActionClickListener clickListener;
    private ArrayList<Action> items = Utils.getActionsList();
    int lastClickPosition = 0;
    public ActionsAdapter(Context context,OnActionClickListener onActionClickListener){
        mContext = context;
        this.clickListener = onActionClickListener;
    }

    @Override
    public SingleViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.single_selecting_actions, parent, false);
        return new SingleViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(SingleViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvActionName = itemView.findViewById(R.id.tv_action_name);
       // private LinearLayout llAction  = itemView.findViewById(R.id.ll_actions);
        private ToggleButton toggleButton = itemView.findViewById(R.id.toggle_button_rv);
        private ImageView ivAction = itemView.findViewById(R.id.iv_action);

        public SingleViewHolder(View itemView) {
            super(itemView);
        }
        void bind(Action item){
            itemView.setOnClickListener(this);
            tvActionName.setText(item.getActionType().getActionName());
            itemView.setTag(item.getActionType().name());
            toggleButton.setChecked(false);
            ivAction.setImageResource(item.getActionType().getIcon());
            if (item.getActionType().getIsTwoStatuses()) toggleButton.setVisibility(View.VISIBLE);
            else toggleButton.setVisibility(View.INVISIBLE);
            toggleButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                Action tempAction = new Action(ActionTypes.valueOf((String) itemView.getTag()));
                if(tempAction.getActionType().ordinal()!=lastClickPosition)  notifyItemChanged(lastClickPosition);
               // lastClickPosition = tempAction.getActionType().ordinal();
                tempAction.setStatus(toggleButton.isChecked());
                clickListener.onActionClicked(tempAction);


            }
        }
    }
}
