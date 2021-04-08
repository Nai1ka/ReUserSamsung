package ru.ndevelop.reusersamsung.ui.actionsList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.adapters.ActionsAdapter;
import ru.ndevelop.reusersamsung.core.interfaces.OnActionClickListener;
import ru.ndevelop.reusersamsung.utils.Action;
import ru.ndevelop.reusersamsung.utils.Utils;

public class ActionsListActivity extends AppCompatActivity implements View.OnClickListener, OnActionClickListener {
    private Button confirmButton;
    private RecyclerView rvActions;
    private ActionsAdapter actionsAdapter;
    private Action selectedAction = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_list);
        actionsAdapter = new ActionsAdapter(this, this);
        initViews();
    }
    private void initViews() {
        confirmButton = findViewById(R.id.btn_action_confirm);
        confirmButton.setOnClickListener(this);
        rvActions = findViewById(R.id.rv_actions);
        rvActions.setAdapter(actionsAdapter);
        rvActions.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onClick(View v) {
        if(v==confirmButton){
            if ( selectedAction!=null) {
                boolean isPermissionsGranted = checkPermissionStatus(selectedAction.getActionType().getPermissions());
                if(isPermissionsGranted){
                    Intent intent = new Intent();
                    intent.putExtra("action", selectedAction);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                        mRequestPermission(selectedAction.getActionType().getPermissions());
                }

            }
        }
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
        selectedAction = action;
    }
    public void mRequestPermission(String[] permissions){
        ActivityCompat.requestPermissions(this, permissions, 1);
    }
}
