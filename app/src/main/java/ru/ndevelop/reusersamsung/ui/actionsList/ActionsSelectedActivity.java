package ru.ndevelop.reusersamsung.ui.actionsList;

import android.content.DialogInterface;
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

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.adapters.SelectedActionsAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnItemStateListener;
import ru.ndevelop.reusersamsung.core.interfaces.OnStartDragListener;
import ru.ndevelop.reusersamsung.core.interfaces.SimpleItemTouchHelperCallback;
import ru.ndevelop.reusersamsung.repositories.DataBaseHandler;
import ru.ndevelop.reusersamsung.utils.Action;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_select);
        tagId = getIntent().getStringExtra("tagId");
        if(tagId==null) tagId ="";
        selectedActionsAdapter = new SelectedActionsAdapter(this, this);
        ArrayList<Action> tagActions = DataBaseHandler.getTagActions(tagId);
        String tagName = DataBaseHandler.getTagName(tagId);
        initViews();
        if(tagActions.size()>0) {
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
                newActionDetected(action);
        }
        }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
    private void newActionDetected(Action action){
        switch (action.getActionType()){
            case SITE:
                openSiteEditTextDialog(action);
                break;
            case APPLICATION:
                openApplicationEditTextDialog(action);
                break;
            case DELAY:
                openTimerEditTextDialog(action);
                break;
            default:
                selectedActionsAdapter.addAction(action);
        }



    }
    private void openSiteEditTextDialog(Action action){
        EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Введите адресс сайта")
                .setMessage("Какой сайт открыть?")
                .setView(taskEditText)
                .setPositiveButton("Ок", (dialog1, which) -> {
                    String url = taskEditText.getText().toString();
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://"+url;
                    action.setSpecialData(url);
                    selectedActionsAdapter.addAction(action);
                })

            .setNegativeButton("Отмена", null)
                .create();
        dialog.show();
    }
    private void openApplicationEditTextDialog(Action action){
        EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Введите название пакета приложения")
                .setMessage("Например ru.ndevelop.reuser")
                .setView(taskEditText)
                .setPositiveButton("Ок", (dialog1, which) -> {
                    action.setSpecialData( taskEditText.getText().toString());
                    selectedActionsAdapter.addAction(action);
                })
            .setNegativeButton("Отмена", null)
                .create();
        dialog.show();
    }
    private void openTimerEditTextDialog(Action action){
        EditText taskEditText = new EditText(this);
        taskEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Введите сколько нужно подождать")
                .setMessage("Введите время в секундах")
                .setView(taskEditText)
                .setPositiveButton("Ок", (dialog1, which) -> {
                    String inputTime =  taskEditText.getText().toString();
                    if(inputTime.matches("\\d+"))  {
                        action.setSpecialData(inputTime);
                        selectedActionsAdapter.addAction(action);
                    }
                    else {
                        Toast.makeText(this, "Время введено неверно", Toast.LENGTH_SHORT).show();
                        openTimerEditTextDialog(action);
                    }
                })
            .setNegativeButton("Отмена", null)
                .create();
        dialog.show();
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
