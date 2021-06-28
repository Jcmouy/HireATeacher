package com.ejemplo.appdocente;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Remote.DocenteService;
import com.ejemplo.appdocente.Remote.SmsService;
import com.ejemplo.appdocente.Service.HttpService;
import com.ejemplo.appdocente.Util.CheckPermission;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = SmsActivity.class.getSimpleName();

    private static final String OTP_DELIMITER = ":";

    private static final int READ_SMS = 100;
    private static final int RECEIVE_SMS = 101;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp, btnTeacherAccess, btnTeacher, btnStudent;
    private EditText inputName, inputEmail, inputMobile, inputOtp, inputUsername, inputUserNameTeacher, inputPassTeacher;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    private CountryCodePicker ccp;
    private String mobile;
    private CheckPermission checKPer;
    private DocenteService mDocenteService;

    private SmsService mSmsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setAutoDetectedCountry(true);

        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputUsername = (EditText) findViewById(R.id.inputUserName);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        inputUserNameTeacher = (EditText) findViewById(R.id.inputUserName_teacher);
        inputPassTeacher = (EditText) findViewById(R.id.inputPass_teacher);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);
        btnTeacher = (FButton) findViewById(R.id.f_teacher_button);
        btnStudent = (FButton) findViewById(R.id.f_student_button);
        btnTeacherAccess = (Button) findViewById(R.id.btn_teacher_access);

        mSmsService = RemoteUtils.getSmsService();
        mDocenteService = RemoteUtils.getDocenteService();

        ccp.registerCarrierNumberEditText(inputMobile);

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnTeacherAccess.setOnClickListener(this);

        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = new PrefManager(this);

        // pref.clearSession();

        boolean prueba = pref.isLoggedIn();

        // Checking for user session
        // if user is already logged in, take him to main activity

        //Habilitar para andorid emulator
        ///////////////////////////////////////////////////////

        /*
        Intent intent = new Intent(SmsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

         */

        /////////////////////////////////////////////////////////

        //Habilitar para smartphone
        /////////////////////////////////////////////////////////


        if (pref.isLoggedIn()) {
            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }


        /////////////////////////////////////////////////////////

        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {

                // Toast.makeText(getApplicationContext(), "Updated " + ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();

                if (isValidNumber){
                    mobile = ccp.getFullNumberWithPlus();
                }

                Log.i("SmsActivity","Country Code Picker");

            }
        });

        adapter = new ViewPagerAdapter();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(3);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewPager.setCurrentItem(0, true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.f_teacher_button:
                viewPager.setCurrentItem(1);
                break;

            case R.id.f_student_button:
                viewPager.setCurrentItem(2);
                break;

            case R.id.btn_request_sms:
                checKPer = new CheckPermission();
                checKPer.checkPermission(Manifest.permission.READ_SMS, READ_SMS, getApplicationContext(), this);
                checKPer.checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS, getApplicationContext(), this);

                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(2);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;

            case R.id.btn_teacher_access:
                validateTeacher();
                break;
        }
    }

    /**
     * Validating user details form
     */
    private void validateForm() {
        String username = inputUsername.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        // validating empty name and email
        if (name.length() == 0 || username.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_details_err), Toast.LENGTH_SHORT).show();
            return;
        }

        // validating mobile number
        // it should be of 12 digits length
        if (ccp.isValidFullNumber()) {

            // request for sms
            progressBar.setVisibility(View.VISIBLE);

            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);

            // requesting for sms
            requestForSMS(username, name, email, mobile);

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_student_mobile_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void validateTeacher() {
        String username = inputUserNameTeacher.getText().toString().trim();
        String password = inputPassTeacher.getText().toString().trim();

        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_details_err), Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        getTeacher(username, password);
    }

    private void getTeacher(String username, String password) {
        mDocenteService.authenticateTeacher(username, password, true).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    JsonObject json = new JsonObject();
                    json = response.body();

                    String id = json.get("Id").getAsString();
                    String username = json.get("Username").getAsString();
                    String name = json.get("Nombre").getAsString();
                    String email = json.get("Email").getAsString();
                    String mobile = json.get("Mobile").getAsString();

                    pref.createLogin(id, name, username, email, mobile, ConstantTipoUsuario.DOCENTE);

                    Intent intent = new Intent(SmsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else if(response.errorBody() != null){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.log_teacher_error), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);

                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_get_rest), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    /**
     * Method initiates the SMS request on the server
     *
     * @param name   user name
     * @param email  user email address
     * @param mobile user valid mobile number
     */
    private void requestForSMS(final String username, final String name, final String email, final String mobile) {

        mSmsService.sent_sms(username,name,email,mobile).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("SmsActivity", "RequestSMS successful");

                    // boolean flag saying device is waiting for sms
                    pref.setIsWaitingForSms(true);

                    // moving the screen to next pager item i.e otp screen
                    viewPager.setCurrentItem(3);
                    txtEditMobile.setText(pref.getMobileNumber());
                    layoutEditMobile.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();


                }else if(response.errorBody() != null){
                    progressBar.setVisibility(View.GONE);

                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);

                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

            }
        });

    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }
}

class ViewPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }


    public Object instantiateItem(View collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.layout_start_app;
                break;
            case 1:
                resId = R.id.layout_teacher_start;
                break;
            case 2:
                resId = R.id.layout_sms;
                break;
            case 3:
                resId = R.id.layout_otp;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}


