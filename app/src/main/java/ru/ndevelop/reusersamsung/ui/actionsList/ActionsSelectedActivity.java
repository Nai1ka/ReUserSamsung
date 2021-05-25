package ru.ndevelop.reusersamsung.ui.actionsList;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private Button btnAdd;
    private LinearLayout llNotEmpty;
    private TextInputEditText etName;
    private ItemTouchHelper touchHelper;
    private FloatingActionButton fabOk;
    private FloatingActionButton fabDelete;
    private String tagId = "";
    int fabDeleteClickCounter =0;
    private AppDatabase database;
    TagDao tagDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_select);
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
        btnAdd = findViewById(R.id.btn_add_actions);
        btnAdd.setOnClickListener(this);
        etName = findViewById(R.id.et_name);
;
        fabOk = findViewById(R.id.fab_ok);
        fabOk.setOnClickListener(this);
        fabDelete = findViewById(R.id.fab_delete);
        fabDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedActionsAdapter.clear();
                fabDeleteClickCounter = 0;
                return false;
            }
        });
        fabDelete.setOnClickListener(this);
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
        else if(v==fabDelete){
            fabDeleteClickCounter += 1;
            if (fabDeleteClickCounter > 1) {
                Toast.makeText(this, "Удерживайте, чтобы удалить", Toast.LENGTH_SHORT).show();
                fabDeleteClickCounter = 0;
            }
        }
        else if(v==fabOk){
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
            fabDelete.setVisibility( View.GONE);
            llIfActionsNotSelected.setVisibility( View.GONE);
        }
    }

    @Override
    public void onItemAdded() {
        llNotEmpty.setVisibility(View.VISIBLE);
        fabDelete.setVisibility( View.VISIBLE);
        llIfActionsNotSelected.setVisibility( View.GONE);
    }
}
