package com.kredivation.pankh;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.kredivation.pankh.Main_Menu.MainMenuActivity;
import com.kredivation.pankh.SimpleClasses.Variables;
import com.kredivation.pankh.update.Constants;
import com.kredivation.pankh.update.InAppUpdateManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Splash_A extends AppCompatActivity {


    CountDownTimer countDownTimer;

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "SplashScreenActivity";
    private InAppUpdateManager inAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        getHSAKey();

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Intent intent = new Intent(Splash_A.this, MainMenuActivity.class);

                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();

            }
        }.start();


        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.IMMEDIATE);

        inAppUpdateManager.checkForAppUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
    public void getHSAKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(Variables.tag, "KeyHash*****:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
}
