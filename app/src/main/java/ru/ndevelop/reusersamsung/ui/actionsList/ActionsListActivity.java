package ru.ndevelop.reusersamsung.ui.actionsList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.adapters.ActionsAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnActionClickListener;
import ru.ndevelop.reusersamsung.core.objects.Action;

public class ActionsListActivity extends AppCompatActivity implements OnActionClickListener {

    private RecyclerView rvActions;
    private ActionsAdapter actionsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_list);
        actionsAdapter = new ActionsAdapter(this, this);
        initViews();
    }
    private void initViews() {
        rvActions = findViewById(R.id.rv_actions);
        rvActions.setAdapter(actionsAdapter);
        rvActions.setLayoutManager(new LinearLayoutManager(this));

    }

    boolean checkPermissionStatus(String[] permissions){
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActionClicked(Action action) {
        boolean isPermissionsGranted = checkPermissionStatus(action.getActionType().getPermissions());
        if(isPermissionsGranted){
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
                    sendAction(action);
            }
        }
        else{
            mRequestPermission(action.getActionType().getPermissions());
        }
    }
    public void mRequestPermission(String[] permissions){
        ActivityCompat.requestPermissions(this, permissions, 1);
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
                    sendAction(action);
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
                    sendAction(action);
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
                        sendAction(action);
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
    private void sendAction(Action action){
        Intent intent = new Intent();
        intent.putExtra("action", action);
        setResult(RESULT_OK, intent);
        finish();
    }
}
