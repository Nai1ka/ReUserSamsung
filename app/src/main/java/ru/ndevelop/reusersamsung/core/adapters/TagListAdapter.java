package ru.ndevelop.reusersamsung.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.sulek.ssml.OnSwipeListener;
import io.sulek.ssml.SimpleSwipeMenuLayout;

import ru.ndevelop.reusersamsung.R;

import ru.ndevelop.reusersamsung.core.enums.ButtonType;
import ru.ndevelop.reusersamsung.core.interfaces.OnEditButtonClickListener;
import ru.ndevelop.reusersamsung.core.objects.Tag;
import ru.ndevelop.reusersamsung.utils.Action;
import ru.ndevelop.reusersamsung.utils.ActionTypes;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.SingleViewHolder> {

    private ArrayList<Tag> items = new ArrayList<>();
    OnEditButtonClickListener mEditButtonClickListener;

    public TagListAdapter(OnEditButtonClickListener editButtonClickListener) {
        mEditButtonClickListener = editButtonClickListener;
    }

    @Override
    public SingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.swipe_menu_holder, parent, false);
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

    public void loadItems(ArrayList<ru.ndevelop.reusersamsung.core.objects.Tag> payload) {

        items = payload;
        notifyDataSetChanged();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTagList = itemView.findViewById(R.id.tv_tag_name);
        private TextView tvTagActions = itemView.findViewById(R.id.tv_tag_actions);
        private SimpleSwipeMenuLayout swipeContainer =
                itemView.findViewById(R.id.swipeContainer);
        private FrameLayout editBtn = itemView.findViewById(R.id.btn_edit);
        private FrameLayout deleteBtn = itemView.findViewById(R.id.btn_delete);
        private String tagId = "";

        public SingleViewHolder(View itemView) {
            super(itemView);
        }

        void bind(ru.ndevelop.reusersamsung.core.objects.Tag item) {
            tvTagList.setText(item.getName());
            tagId = item.getTagId();
            tvTagActions.setText("");
            ArrayList<Action> tempActions = item.getActions();
            for (int i = 0; i < tempActions.size(); i++) {
                String actionName = "";
                Action tempAction = tempActions.get(i);
                if (tempAction.getActionType().getIsTwoStatuses()) {
                    if (tempAction.getStatus()) actionName = "Включить ";
                    else actionName = "Выключить ";
                }
                if (tempAction.getActionType().getIsTwoStatuses() && tempAction.getActionType() != ActionTypes.WIFI) {
                    actionName += tempAction.getActionType().getActionName().toLowerCase();
                } else {
                    actionName += tempAction.getActionType().getActionName();
                }

                tvTagActions.append(actionName+" "+tempAction.getSpecialData()+"\n");
            }


            editBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
            swipeContainer.setOnSwipeListener(new OnSwipeListener() {
                @Override
                public void onSwipe(boolean isExpanded) {
                    item.setIsExpanded(isExpanded);
                }
            });
            swipeContainer.apply(item.getIsExpanded());

        }

        @Override
        public void onClick(View v) {
            if (!tagId.equals(""))
                if (v == editBtn) {
                    mEditButtonClickListener.onEditButtonClick(ButtonType.EDIT, tagId);
                } else if (v == deleteBtn) {
                    mEditButtonClickListener.onEditButtonClick(ButtonType.DELETE, tagId);
                }

        }
    }

}
