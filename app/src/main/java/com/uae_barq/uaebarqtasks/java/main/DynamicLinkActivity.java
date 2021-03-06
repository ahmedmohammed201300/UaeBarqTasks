package com.uae_barq.uaebarqtasks.java.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.uae_barq.uaebarqtasks.R;
import com.uae_barq.uaebarqtasks.java.constants.BarqConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicLinkActivity extends AppCompatActivity {

    private static final String TAG = "DynamicLinkActivity";


    @BindView(R.id.tvFirstName)
    TextView tvFirstName;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvCountry)
    TextView tvCountry;

    private ProgressDialog progressDialog;
    private Uri appLinkData = null;

    /**
     * manage a handler.post(runnable) by creating an instance member for the handler and runnable,
     * then managing the handler in the Activity Lifecycle methods.
     */
    private Handler mHandlerShowProgressDlg = null;
    private Runnable mRunnableShowProgressDlg = new Runnable() {
        @Override
        public void run() {
            //dismissing the dialog
            if (progressDialog != null) {
                progressDialog.dismiss();

                if (appLinkData != null) {
                    //then fetching the user data from parameters
                    fetchUserProfileData(appLinkData);
                }
            }

        }
    };

    //Butterknife also allows to unbind again, via the Unbinder object.
    private Unbinder unbinder;


    /**
     * Called firstly here as Activity Life Cycle
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dynamic_link);

        // Initialize ButterKnife
        //Before using any views, we need to inject ButterKnife
        unbinder = ButterKnife.bind(this);

    }

    /**
     * called after onCreate method as Activity Life Cycle
     * For accessing what i want to access
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //initialize the progress dialog
        initProgressDialog();

        //initialize handler
        initHandler();

        //Only for app links
        checkForAppDeepLink();

        //for firebase dynamic links
        checkForFirebaseDynamicLinks();

    }

    @Override
    protected void onStop() {
        super.onStop();

        releaseAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseAll();
    }

    private void releaseAll() {

        //releasing butterknife injection
        unbindButterKnife();

        //In case of activity finishes its lifecycle before the Handler executes the code.
        removeHandlerCallbacks();
    }

    /**
     * unbindButterKnife
     */
    private void unbindButterKnife() {
        if (unbinder != null)
            unbinder.unbind();
    }

    /**
     * initialize the progress dialog
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_user_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * checkForAppDeepLink
     */
    private void checkForAppDeepLink() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();


        //only for testing, please uncomment this below line for checking the dynamic link
        //appLinkData = createTestingDynamicLink();
//        Log.e(TAG, "checkForAppDeepLink: " + appLinkData.toString());

        //real when app is installed, the data will be filled from the clicked dynamic link
        appLinkData = appLinkIntent.getData();

        if (appLinkData != null) {
            showProgressBar(appLinkData);
        }
    }

    /**
     * createTestingDynamicLink
     *
     * @return
     */
    public Uri createTestingDynamicLink() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("example.com")
                .appendPath("appLinks")
                .appendQueryParameter(BarqConstants.USER_FIRST_NAME, "Ahmed")
                .appendQueryParameter(BarqConstants.USER_LAST_NAME, "Hegazi")
                .appendQueryParameter(BarqConstants.USER_AGE, "24")
                .appendQueryParameter(BarqConstants.USER_COUNTRY, "EG")
                .appendQueryParameter(BarqConstants.USER_PHONE_NUMBER, "01156749640");
//                .fragment("section-name");
        return builder.build();
    }

    /**
     * Showing the progress bar as UX waiting for fetching data from appLink
     *
     * @param appLinkData
     */
    private void showProgressBar(Uri appLinkData) {

        //Showing the progress dialog
        progressDialog.show();

        //showing the progress dialog for 2 sec
        mHandlerShowProgressDlg.postDelayed(mRunnableShowProgressDlg, BarqConstants.MILLI);

    }

    /**
     * Fetching the user profile data from the intent through the QueryParameters.
     *
     * @param appLinkData
     */
    private void fetchUserProfileData(Uri appLinkData) {
        String firstName = appLinkData.getQueryParameter(BarqConstants.USER_FIRST_NAME);
        String lastName = appLinkData.getQueryParameter(BarqConstants.USER_LAST_NAME);
        String phoneNumber = appLinkData.getQueryParameter(BarqConstants.USER_PHONE_NUMBER);
        String country = appLinkData.getQueryParameter(BarqConstants.USER_COUNTRY);
        String age = appLinkData.getQueryParameter(BarqConstants.USER_AGE);
//        String photo = appLinkData.getQueryParameter("photo");

        Log.i(TAG, "onCreate: firstName   " + firstName);
        Log.i(TAG, "onCreate: lastName    " + lastName);
        Log.i(TAG, "onCreate: phoneNumber " + phoneNumber);
        Log.i(TAG, "onCreate: country     " + country);
        Log.i(TAG, "onCreate: age         " + age);
//        Log.e(TAG, "onCreate: " + photo);

        if (!notNull(firstName)) {
            tvFirstName.setText(firstName);
        }
        if (!notNull(lastName)) {
            tvLastName.setText(lastName);
        }
        if (!notNull(phoneNumber)) {
            tvPhoneNumber.setText(phoneNumber);
        }
        if (!notNull(country)) {
            tvCountry.setText(country);
        }
        if (!notNull(age)) {
            tvAge.setText(age);
        }

        //fetching the param value
        String welcomeMessage = appLinkData.getQueryParameter(BarqConstants.WELCOME_MESSAGE);

        //sending the param value to alert dialog
        showWelcomeMessageAlertToUser(welcomeMessage);

    }

    /**
     * This method works but it tested in the Prev. mentioned email attached video for
     * the ToDoList app, as this method needs areal published app with firebase dynamic link integration.
     * Wow
     *
     * @param welcomeMessage
     */
    private void showWelcomeMessageAlertToUser(String welcomeMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(welcomeMessage);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Checking if the passed text not equals null
     *
     * @param firstName
     * @return
     */
    private boolean notNull(String firstName) {
        return TextUtils.equals(firstName, null);
    }

    /**
     *
     */
    private void checkForFirebaseDynamicLinks() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Log.i(TAG, "onSuccess: Get deep link from result (may be null if no link is found)");
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            Log.i(TAG, "onSuccess: " + deepLink.toString());
                            fetchUserProfileData(deepLink);
                        }
                    }

                })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "getDynamicLink: onFailure" + e.getMessage());
                    Log.e(TAG, "getDynamicLink: onFailure" + e.getLocalizedMessage());
                });

    }

    /**
     * In case of activity finishes its lifecycle before the Handler executes the code.
     */
    private void removeHandlerCallbacks() {
        if (mHandlerShowProgressDlg != null && mRunnableShowProgressDlg != null) {
            mHandlerShowProgressDlg.removeCallbacks(mRunnableShowProgressDlg);
        }
    }

    /**
     * initHandler
     */
    private void initHandler() {
        mHandlerShowProgressDlg = new Handler();
    }
}
