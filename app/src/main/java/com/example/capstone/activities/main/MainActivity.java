package com.example.capstone.activities.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone.R;
import com.example.capstone.activities.log.LogsActivity;
import com.example.capstone.databinding.ActivityMainBinding;
import com.example.capstone.manager.PreferenceManager;
import com.example.capstone.service.DotService;
import com.example.capstone.util.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1802;
    private boolean TRIGGERED_START = false;
    private PreferenceManager sharedPreferenceManager;
    private Intent serviceIntent;
    private ActivityMainBinding mBinding;

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPreferenceManager.isServiceEnabled()) {
            mBinding.mainSwitch.setChecked(checkAccessibility());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        sharedPreferenceManager = PreferenceManager.getInstance(getApplication());
        loadFromPrefs();
        init();
        checkAutoStartRequirement();
    }

    private void loadFromPrefs() {
        mBinding.locationSwitch.setChecked(sharedPreferenceManager.isLocationEnabled());
        mBinding.mainSwitch.setChecked(sharedPreferenceManager.isServiceEnabled());
    }

    //위치 권한 할당
    private void init() {
        mBinding.locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_round_location))
                            .setTitle("위치 권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해 위치 귄한이 필요합니다.\n\n참고: 이 앱은 인터넷 연결이 없어 데이터가 안전하게 보호됩니다.")
                            .setNeutralButton("취소", (dialog, which) -> mBinding.locationSwitch.setChecked(false))
                            .setPositiveButton("계속", (dialog, which) -> {
                                askPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                            })
                            .show();
                } else {
                    sharedPreferenceManager.setLocationEnabled(true);
                }
            }

        });
        mBinding.mainSwitch.setOnCheckedChangeListener((buttonView, b) -> {
            if (b) {
                checkForAccessibilityAndStart();
                TRIGGERED_START = true;
            } else {
                stopService();
                TRIGGERED_START = false;
            }
        });
        mBinding.logsOption.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LogsActivity.class);
            startActivity(intent);
        });


    }

    // 활성화 버튼 클릭
    private void checkForAccessibilityAndStart() {

        if (!accessibilityPermission(getApplicationContext(), DotService.class)) {
            mBinding.mainSwitch.setChecked(false);
            new MaterialAlertDialogBuilder(this)
                    .setTitle("접근성 권한이 필요합니다.")
                    .setMessage("활성화를 위해 접근성 권한을 활성화해야합니다.")
                    .setIcon(R.drawable.ic_baseline_accessibility_24)
                    .setPositiveButton("접근성 열기", (dialog, which) -> startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS")))
                    .setNegativeButton("취소", null)
                    .setCancelable(true)
                    .show();
        } else {
            mBinding.mainSwitch.setChecked(true);
            sharedPreferenceManager.setServiceEnabled(true);
            serviceIntent = new Intent(MainActivity.this, DotService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
    }

    private void stopService() {
        if (isAccessibilityServiceRunning()) {
//            sharedPreferenceManager.setServiceEnabled(false);
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, getString(R.string.close_app_note), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (TRIGGERED_START) {
//            TRIGGERED_START = false;
//            checkForAccessibilityAndStart();
//        }
//        if (!sharedPreferenceManager.isServiceEnabled()) {
//            mBinding.mainSwitch.setChecked(checkAccessibility());
//        }
//    }

    public static boolean accessibilityPermission(Context context, Class<?> cls) {
        ComponentName componentName = new ComponentName(context, cls);
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (string == null) {
            return false;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        simpleStringSplitter.setString(string);
        while (simpleStringSplitter.hasNext()) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(simpleStringSplitter.next());
            if (unflattenFromString != null && unflattenFromString.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    private boolean checkAccessibility() {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        return manager.isEnabled();
    }

    /**
     * @return
     */
    private boolean isAccessibilityServiceRunning() {
        String prefString = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString != null && prefString.contains(this.getPackageName() + "/" + DotService.class.getName());
    }

    /**
     * Asks permission runtime
     *
     * @param permission
     */
    private void askPermission(String permission) {
        if (!(ContextCompat.checkSelfPermission(this, permission) == 0)) {
            requestPermissions(new String[]{permission}, 0);
            sharedPreferenceManager.setLocationEnabled(true);
        }
    }

    private void checkAutoStartRequirement() {
        String manufacturer = Build.MANUFACTURER;
        if (sharedPreferenceManager.isFirstLaunch()) {
            if ("xiaomi".equalsIgnoreCase(manufacturer)
                    || ("oppo".equalsIgnoreCase(manufacturer))
                    || ("vivo".equalsIgnoreCase(manufacturer))
                    || ("Honor".equalsIgnoreCase(manufacturer))) {
                Utils.showAutoStartDialog(MainActivity.this, manufacturer);
                sharedPreferenceManager.setFirstLaunch();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}