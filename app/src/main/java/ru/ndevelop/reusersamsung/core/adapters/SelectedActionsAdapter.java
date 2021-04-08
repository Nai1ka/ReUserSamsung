package ru.ndevelop.reusersamsung.core.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import ru.ndevelop.reusersamsung.R;

import ru.ndevelop.reusersamsung.core.interfaces.ItemTouchHelperAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnItemStateListener;
import ru.ndevelop.reusersamsung.core.interfaces.OnStartDragListener;
import ru.ndevelop.reusersamsung.utils.Action;

public class SelectedActionsAdapter extends RecyclerView.Adapter<SelectedActionsAdapter.SingleViewHolder> implements ItemTouchHelperAdapter {
    OnStartDragListener mDragStartListener;
    OnItemStateListener mOnItemsStateListener;
    private ArrayList<Action> items = new ArrayList<>();

    public SelectedActionsAdapter(OnStartDragListener dragStartListener, OnItemStateListener onItemsStateListener){
        mDragStartListener = dragStartListener;
        mOnItemsStateListener = onItemsStateListener;
    }

    @Override
    public SingleViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.single_action_selecting, parent, false);
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
    public void addAction(Action action){
        items.add(action);
        mOnItemsStateListener.onItemAdded();
        notifyDataSetChanged();

    }
    public void loadActions( ArrayList<Action> actions){
        items = actions;
        mOnItemsStateListener.onItemAdded();
        notifyDataSetChanged();

    }
    public void clear(){
        items.clear();
        mOnItemsStateListener.onItemDeleted(0);
        notifyDataSetChanged();
    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i =fromPosition;i<toPosition;i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition;i>toPosition+1;i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        mOnItemsStateListener.onItemDeleted(items.size());
        notifyItemRemoved(position);
    }
    public ArrayList<Action> getItems(){
        return items;
    }
    class SingleViewHolder extends RecyclerView.ViewHolder implements  View.OnTouchListener {

        private TextView tvActionName  = itemView.findViewById(R.id.tv_action_name);
        private LinearLayout llAction  = itemView.findViewById(R.id.ll_actions);
        private ImageView ivAction= itemView.findViewById(R.id.iv_action);
        private ToggleButton toggleButton = itemView.findViewById(R.id.toggle_button_rv);
        public SingleViewHolder(View itemView) {
            super(itemView);
        }
        void bind(Action item){
            tvActionName.setText(item.getActionType().getActionName());
            llAction.setTag(item.getActionType().getActionName());
            llAction.setOnTouchListener(this);
            ivAction.setImageResource(item.getActionType().getIcon());
            toggleButton.setClickable(false);
            if (item.getActionType().getIsTwoStatuses()) {
                toggleButton.setVisibility(View.VISIBLE);
                toggleButton.setChecked(item.getStatus());
            } else toggleButton.setVisibility(View.INVISIBLE);

        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEventCompat.getActionMasked(event) ==
                    MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(this);
            }
            return false;
        }
    }
}
