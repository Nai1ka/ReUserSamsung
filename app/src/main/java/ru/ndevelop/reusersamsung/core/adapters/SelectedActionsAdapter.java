package ru.ndevelop.reusersamsung.core.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.ndevelop.reusersamsung.App;
import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.interfaces.ItemTouchHelperAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnItemStateListener;
import ru.ndevelop.reusersamsung.core.interfaces.OnStartDragListener;
import ru.ndevelop.reusersamsung.core.objects.Action;

public class SelectedActionsAdapter extends RecyclerView.Adapter<SelectedActionsAdapter.SingleViewHolder> implements ItemTouchHelperAdapter {
    OnStartDragListener mDragStartListener;
    OnItemStateListener mOnItemsStateListener;
    private ArrayList<Action> items = new ArrayList<>();

    public SelectedActionsAdapter(OnStartDragListener dragStartListener, OnItemStateListener onItemsStateListener) {
        mDragStartListener = dragStartListener;
        mOnItemsStateListener = onItemsStateListener;
    }

    @Override
    public SingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.single_selected_actions, parent, false);
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

    public void addAction(Action action) {
        items.add(action);
        mOnItemsStateListener.onItemAdded();
        notifyDataSetChanged();

    }

    public void loadActions(ArrayList<Action> actions) {
        items = actions;
        mOnItemsStateListener.onItemAdded();
        notifyDataSetChanged();

    }

    public void clear() {
        items.clear();
        mOnItemsStateListener.onItemDeleted(0);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        Action temp = items.get(fromPosition);
        items.set(fromPosition, items.get(toPosition));
        items.set(toPosition, temp);
       notifyItemMoved(fromPosition,toPosition);

    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        mOnItemsStateListener.onItemDeleted(items.size());
        notifyItemRemoved(position);
    }

    public ArrayList<Action> getItems() {
        return items;
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvActionName = itemView.findViewById(R.id.tv_action_name);
        private LinearLayout llAction = itemView.findViewById(R.id.ll_actions);
        private ImageView ivAction = itemView.findViewById(R.id.iv_action);
        //private ToggleButton toggleButton = itemView.findViewById(R.id.toggle_button_rv);
        private ImageView ivReorder = itemView.findViewById(R.id.iv_reorder);

        public SingleViewHolder(View itemView) {
            super(itemView);
        }

        void bind(Action item) {
            String actionName = "";
            if (item.getActionType().getIsTwoStatuses()) {
                if (item.getStatus()) actionName = "Включить ";
                else actionName = "Выключить ";
            }
            actionName+=item.getActionType().getActionName();
            if(!item.getSpecialData().equals(""))
                actionName+=" "+item.getSpecialData();
            tvActionName.setText(actionName);

            llAction.setTag(item.getActionType().getActionName());
            ivAction.setImageResource(item.getActionType().getIcon());
            itemView.setOnLongClickListener(v -> {
                Vibrator vibrator = (Vibrator)itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if(vibrator!=null) vibrator.vibrate(100);
                return false;
            });
            //toggleButton.setClickable(false);
            //  if (item.getActionType().getIsTwoStatuses()) {
            //  toggleButton.setVisibility(View.VISIBLE);
            //toggleButton.setChecked(item.getStatus());
            // } else toggleButton.setVisibility(View.INVISIBLE);

        }


    }
}
