package ru.ndevelop.reusersamsung.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ru.ndevelop.reusersamsung.App;
import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.interfaces.OnSettingsChangeListener;
import ru.ndevelop.reusersamsung.core.interfaces.TagDao;
import ru.ndevelop.reusersamsung.core.objects.Action;
import ru.ndevelop.reusersamsung.repositories.AppDatabase;
import ru.ndevelop.reusersamsung.repositories.PreferencesRepository;
import ru.ndevelop.reusersamsung.ui.actionsList.ActionsSelectedActivity;
import ru.ndevelop.reusersamsung.ui.preview.PreviewActivity;
import ru.ndevelop.reusersamsung.utils.RequestCodes;
import ru.ndevelop.reusersamsung.utils.Utils;


public class MainActivity extends AppCompatActivity implements OnSettingsChangeListener {
    //   private var nfcAdapter: NfcAdapter? = null
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private AppDatabase database;
    NavController navController;
    TagDao tagDao;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = App.getInstance().getDatabase();
        tagDao = database.getTagDao();

        Tag tempTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String registeredTag = "";
        List<Action> actions = new ArrayList<Action>();
        if (tempTag != null) {
            registeredTag = Utils.byteArrayToHexString(tempTag.getId());
            ru.ndevelop.reusersamsung.core.objects.Tag receivedTag = tagDao.getTagById(registeredTag);
            if (receivedTag != null) actions = receivedTag.getActions();
        }
        if (actions.size() != 0) {
            try {
                for (int i = 0; i < actions.size(); i++) {
                    Action tempAction = actions.get(i);
                    tempAction.getActionType().performAction(this, tempAction.getStatus(), tempAction.getSpecialData());

                }
            } catch (Exception e) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            if (PreferencesRepository.getInstance(this).isFirstLaunch()) {
                Intent i = new Intent(this, PreviewActivity.class);
                startActivity(i);
                PreferencesRepository.getInstance(this).setIsNotFirstLaunch();
            }
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);

            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);
            initAds();
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
        if (requestCode == RequestCodes.nfcRequestCode) {

            String tagId = data.getStringExtra("tagId"); //TODO тоже сделать проверку
            if (tagId == null) tagId = "";
            String tagName = data.getStringExtra("tagName");
            if (tagName == null) tagName = "New Tag";
            ArrayList<Action> actions =
                    ((ArrayList<Action>) data.getSerializableExtra("actions"));


            ru.ndevelop.reusersamsung.core.objects.Tag tag = new ru.ndevelop.reusersamsung.core.objects.Tag(tagName, tagId);


            tag.setActions(actions);

            AsyncTask.execute(() -> tagDao.addTag(tag));
            navController.popBackStack(); //-> it's because you must delete one step from navigation
            navController.navigate(
                    R.id.navigation_taglist
            );
            showAd();



        }
    }
    private void initAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("DEBUG", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
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
        if (tempTag != null) {
            String tagId = Utils.byteArrayToHexString(tempTag.getId());
            if (tagDao.getTagById(tagId) != null) {
                Toast.makeText(
                        this,
                        "Такая метка уже существует. Информация будет перезаписана",
                        Toast.LENGTH_SHORT
                ).show();
            }
            startActionsSelectionActivity(tagId);
        }
    }

    public void startActionsSelectionActivity(String tagId) {
        Intent i = new Intent(this, ActionsSelectedActivity.class);
        i.putExtra("tagId", tagId);
        startActivityForResult(i, RequestCodes.nfcRequestCode);
    }

    @Override
    public void onBackgroundChangedClicked() {

    }

    @Override
    public void onAdsClickedListener() {

    }

    @Override
    public void onSiteClickedListener() {

    }
    private void showAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("DEBUG", "The interstitial ad wasn't ready yet.");
        }
    }
}