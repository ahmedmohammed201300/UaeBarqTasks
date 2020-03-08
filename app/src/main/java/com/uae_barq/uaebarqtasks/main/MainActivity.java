package com.uae_barq.uaebarqtasks.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.uae_barq.uaebarqtasks.R;
import com.uae_barq.uaebarqtasks.constants.BarqConstants;
import com.uae_barq.uaebarqtasks.task_dynamic.TaskDynamicActivity;
import com.uae_barq.uaebarqtasks.task_speed.TaskSpeedActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
//        Log.e(TAG, "onCreate: " + appLinkData.toString() );

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        testingFirebaseRealtimeDatabase();
        checkForDynamicLinks();
    }

    private void checkForDynamicLinks() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Log.e(TAG, "onSuccess: ");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e(TAG, "onSuccess: " + deepLink.toString());
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });

    }

    private void testingFirebaseRealtimeDatabase() {
        FirebaseDatabase.getInstance().getReference()
                .child(BarqConstants.STATUS)
                .setValue(true)
//                .push()
//                .setValue(new HashMap<String, Object>().put(BarqConstants.STATUS, true))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.connected, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    @OnClick({R.id.btnStartTaskDynamic, R.id.btnStartTaskSpeed})
    public void onBtnTaskClicked(View v) {
        switch (v.getId()) {
            case R.id.btnStartTaskDynamic:
                Log.e(TAG, "onBtnTaskClicked: " + ((Button) v).getText().toString());
                openTaskDynamic();
                break;
            case R.id.btnStartTaskSpeed:
                Log.e(TAG, "onBtnTaskClicked: " + ((Button) v).getText().toString());
                openTaskSpeed();
                break;
            default:
                break;
        }
    }

    private void openTaskSpeed() {
        openActivity(TaskSpeedActivity.class);
    }

    private void openActivity(Class<?> classToOpen) {
//        finish();
        Intent intent = new Intent(this, classToOpen);
        startActivity(intent);
    }

    private void openTaskDynamic() {
        openActivity(TaskDynamicActivity.class);
    }

}
