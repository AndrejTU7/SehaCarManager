package com.studioadriatic.sehacarmanager.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Andrej on 13.2.2016..
 */
public class ResetPasswordDialog extends Dialog {
    @Bind(R.id.et_resetEmail)
    EditText mEtResetEmail;
    @Bind(R.id.wrapper_resetEmail)
    TextInputLayout wrapperResetEmail;

    public ResetPasswordDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resetpassword);
        ButterKnife.bind(this);
        setTitle(R.string.reset_password);
    }

    @OnClick(R.id.bt_resetPasword)
    public void onClick() {
        String email = mEtResetEmail.getText().toString();
        if (isEmailValid(email)) {
            RequestParams params = new RequestParams();
            params.put(Api.PARAM_EMAIL, email);
            Api.post(Api.USER_PASSWORD_RESET, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        Toast.makeText(getContext(), R.string.password_reset_ok, Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (statusCode == 400) {
                        try {
                            String jsonOutput = new String(responseBody, "UTF-8");
                            ApiError apiError = new Gson().fromJson(jsonOutput, ApiError.class);
                            wrapperResetEmail.setError(apiError.getError());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            wrapperResetEmail.setError(getContext().getResources().getString(R.string.error_valid_email));
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
