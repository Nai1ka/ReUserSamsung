package ru.ndevelop.reusersamsung.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.repositories.DataBaseHandler;
import ru.ndevelop.reusersamsung.repositories.PreferencesRepository;
import ru.ndevelop.reusersamsung.ui.actionsList.ActionsSelectedActivity;
import ru.ndevelop.reusersamsung.utils.Action;
import ru.ndevelop.reusersamsung.utils.RequestCodes;
import ru.ndevelop.reusersamsung.utils.Utils;


public class MainActivity extends AppCompatActivity {
 //   private var nfcAdapter: NfcAdapter? = null
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tag tempTag = (Tag)getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String registeredTag =  "dgg";
        if(tempTag!=null){
            registeredTag = Utils.byteArrayToHexString(tempTag.getId());
        }
        Log.d("DEBUG", registeredTag);
        Toast.makeText(this, registeredTag, Toast.LENGTH_SHORT).show();
        List<Action> actions = DataBaseHandler.getInstance(this).getTagActions(registeredTag);
        Log.d("DEBUG", String.valueOf(actions.size()));

        if (actions.size()!=0) {
            try {
                for(int i=0;i<actions.size();i++){
                    Action tempAction = actions.get(i);
                    tempAction.getActionType().performAction(this,tempAction.getStatus(),tempAction.getSpecialData());
                }
            } catch (Exception e) {
                 Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            if(PreferencesRepository.getInstance(this).isFirstLaunch()){
               // Intent i  = new Intent(this, new FaqActivity());
               // startActivity(i);
                PreferencesRepository.getInstance(this).setIsNotFirstLaunch();
            }
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcPendingIntent = PendingIntent.getActivity(
                    this, 0,
                    new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            );


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if(requestCode == RequestCodes.nfcRequestCode)  {

                String tagId = data.getStringExtra("tagId"); //TODO тоже сделать проверку
                if(tagId==null) tagId = "";
                String tagName = data.getStringExtra("tagName");
                if(tagName==null) tagName = "New Tag";
                ArrayList<Action> actions =
                ((ArrayList<Action>)data.getSerializableExtra("actions"))  ;
                        DataBaseHandler.updateIfExistsElseInsert(
                                tagId,
                                tagName,
                                actions
                        );


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tempTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tempTag!=null){
            String tagId = Utils.byteArrayToHexString(tempTag.getId());
            if (DataBaseHandler.getInstance(this).isTagAlreadyExist(tagId)) {
                Toast.makeText(
                        this,
                        "Такая метка уже существует. Информация будет перезаписана",
                        Toast.LENGTH_SHORT
                ).show();
            }
            startActionsSelectionActivity(tagId);
        }

        //TODO проверить корректность метки(возможно)


    }
    public void startActionsSelectionActivity(String tagId){
        Intent i = new Intent(this, ActionsSelectedActivity.class);
        i.putExtra("tagId", tagId);
        startActivityForResult(i, RequestCodes.nfcRequestCode);
    }
}