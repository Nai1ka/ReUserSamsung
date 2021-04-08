package ru.ndevelop.reusersamsung.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;

import java.io.Serializable;
import java.util.ArrayList;

import ru.ndevelop.reusersamsung.R;

public enum ActionTypes {
    //status: true - включено false - выключено


    CAMERA("Открыть камеру", false, R.drawable.ic_baseline_camera_alt_24, new String[]{Manifest.permission.CAMERA}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            context.startActivity(intent);
        }
    },

    FLASHLIGHT("Фонарик", true, R.drawable.ic_baseline_flash_on_24,new String[]{Manifest.permission.CAMERA}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            //TODO("Not yet implemented")
        }

    },

    SOUND("Звук", true, R.drawable.ic_baseline_volume_up_24,new String[]{}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            // TODO("Not yet implemented")
        }

    },

    WIFI("WI-FI", true, R.drawable.ic_baseline_wifi_24,new String[]{Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_WIFI_STATE}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            WifiManager wifiManager = (WifiManager)
                    context.getApplicationContext().getSystemService(
                            Context.WIFI_SERVICE
                    );
            wifiManager.setWifiEnabled(status);
        }
    },

    BLUETOOTH("Bluetooth", true, R.drawable.ic_baseline_bluetooth_24,new String[]{Manifest.permission.BLUETOOTH}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }

    },

    SITE("Открыть сайт", false, R.drawable.ic_baseline_open_in_browser_24,new String[]{}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(specialData));
            context.startActivity(browserIntent);
        }

    },

    APPLICATION("Открыть приложение", false, R.drawable.ic_baseline_smartphone_24,new String[]{}) {
        public void performAction(Context context, boolean status, String specialData) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(specialData);
            context.startActivity(launchIntent);
        }
    },

    DELAY("Подождать", false, R.drawable.ic_baseline_timer_24, new String[]{}) {
        @Override
        public void performAction(Context context, boolean status, String specialData) {
            try {
                Thread.sleep(Long.parseLong(specialData) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private String actionName;
    private Boolean isTwoStatuses;
    private int icon;
    private String[] permissions;
    ActionTypes(String actionName, boolean isTwoStatuses, int icon, String[] permissions) {
        this.actionName = actionName;
        this.isTwoStatuses = isTwoStatuses;
        this.icon = icon;
        this.permissions = permissions;
    }
    public String getActionName() {
        return actionName;
    }
    public Boolean getIsTwoStatuses() {
        return isTwoStatuses;
    }
    public int getIcon() {
        return icon;
    }
    public String[] getPermissions() {return permissions;}
    public void setPermissions(String[] payload) {permissions = payload; }

    public abstract void performAction(Context context, boolean status, String specialData);
}
