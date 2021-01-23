package com.studioadriatic.sehacarmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.dialogs.ResetPasswordDialog;
import com.studioadriatic.sehacarmanager.models.User;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Andrej on 13.2.2016..
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Bind(R.id.et_password)
    EditText mEtPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_login, R.id.tv_forgotPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                RequestParams params = new RequestParams();
                params.put(Api.PARAM_EMAIL, mEtEmail.getText().toString());
                params.put(Api.PARAM_PASSWORD, mEtPassword.getText().toString());
                Api.post(Api.USER_LOGIN, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(statusCode==200){
                            try {
                                String jsonOutput = new String(responseBody, "UTF-8");
                                User user = new Gson().fromJson(jsonOutput, User.class);
                                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(App.SP_TOKEN, user.getToken()).commit();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(statusCode==400){
                            try {
                                String jsonOutput = new String(responseBody, "UTF-8");
                                ApiError apiError = new Gson().fromJson(jsonOutput, ApiError.class);
                                Toast.makeText(LoginActivity.this, apiError.getError(), Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case R.id.tv_forgotPassword:
                ResetPasswordDialog dialog = new ResetPasswordDialog(LoginActivity.this);
                dialog.show();
                break;
        }
    }
}
