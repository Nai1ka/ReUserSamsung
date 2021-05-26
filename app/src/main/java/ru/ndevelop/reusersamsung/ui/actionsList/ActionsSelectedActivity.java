package ru.ndevelop.reusersamsung.ui.actionsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.ndevelop.reusersamsung.App;
import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.adapters.SelectedActionsAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnItemStateListener;
import ru.ndevelop.reusersamsung.core.interfaces.OnStartDragListener;
import ru.ndevelop.reusersamsung.core.other.SimpleItemTouchHelperCallback;
import ru.ndevelop.reusersamsung.core.interfaces.TagDao;
import ru.ndevelop.reusersamsung.core.objects.Tag;
import ru.ndevelop.reusersamsung.repositories.AppDatabase;
import ru.ndevelop.reusersamsung.core.objects.Action;
import ru.ndevelop.reusersamsung.utils.RequestCodes;

public class ActionsSelectedActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener, OnItemStateListener {
    private RecyclerView rvSelectedActions;
    private SelectedActionsAdapter selectedActionsAdapter;
    private ConstraintLayout llIfActionsNotSelected;
    private LinearLayout llNotEmpty;
    private TextInputEditText etName;
    private ItemTouchHelper touchHelper;
    private ImageButton btnOk;
    private ImageButton btnAdd;
    private String tagId = "";
    private AppDatabase database;
    TagDao tagDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_selected);
        database = App.getInstance().getDatabase();
        tagDao = database.getTagDao();
        tagId = getIntent().getStringExtra("tagId");
        if(tagId==null) tagId ="";
        selectedActionsAdapter = new SelectedActionsAdapter(this, this);
        Tag receivedTag =tagDao.getTagById(tagId);
        ArrayList<Action> tagActions = new ArrayList<>();
        String tagName = null;
        if(receivedTag!=null )  {
            tagActions =receivedTag.getActions();
            tagName = receivedTag.getName();
        }

        initViews();
        if(tagActions.size()>0 && tagName!=null) {
            selectedActionsAdapter.loadActions(tagActions);
            etName.setText(tagName);
        }
    }
    private void initViews() {
        llIfActionsNotSelected = findViewById(R.id.ll_ifEmpty);
        llIfActionsNotSelected.setOnClickListener(this);
        rvSelectedActions = findViewById(R.id.rv_selected_actions);
        llNotEmpty = findViewById(R.id.ll_not_empty);
        etName = findViewById(R.id.et_name);
        btnOk = findViewById(R.id.fab_ok);
        btnOk.setOnClickListener(this);
        btnAdd = findViewById(R.id.fab_add);

        btnAdd.setOnClickListener(this);
        rvSelectedActions.setAdapter(selectedActionsAdapter);
        rvSelectedActions.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback  = new SimpleItemTouchHelperCallback(
                selectedActionsAdapter
        );

        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvSelectedActions);
    }

    @Override
    public void onClick(View v) {
        if(v==llIfActionsNotSelected || v== btnAdd){
            Intent i = new Intent(this, ActionsListActivity.class);
            startActivityForResult(i, RequestCodes.actionsListRequestCode);
        }
        else if(v== btnOk){
            if (selectedActionsAdapter.getItems().size()!=0) {
                Intent intent = new Intent();
                String tagName = etName.getText().toString();
                if(tagName.isEmpty()) tagName = "New Tag";
                intent.putExtra("tagId", tagId);
                intent.putExtra("tagName",tagName);
                intent.putExtra("actions", selectedActionsAdapter.getItems());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Вы не выбрали ни одного действия!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode){
            case RequestCodes.actionsListRequestCode:
                Action action = (Action)data.getSerializableExtra("action");
                selectedActionsAdapter.addAction(action);
        }
        }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }



    @Override
    public void onItemDeleted(int currentItemsSize) {
        if(currentItemsSize==0) {
            llNotEmpty.setVisibility(View.GONE);
            btnAdd.setVisibility( View.VISIBLE);
            btnOk.setVisibility(View.GONE);
            llIfActionsNotSelected.setVisibility( View.VISIBLE);
        }
    }

    @Override
    public void onItemAdded() {
        llNotEmpty.setVisibility(View.VISIBLE);
        btnAdd.setVisibility( View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        llIfActionsNotSelected.setVisibility( View.GONE);
    }
}
