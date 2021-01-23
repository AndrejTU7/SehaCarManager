package com.studioadriatic.sehacarmanager.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by kristijandraca@gmail.com
 */
public class NewCarDialog extends Dialog {
    @Bind(R.id.et_car_name)
    EditText etCarName;
    @Bind(R.id.wrapper_car_name)
    TextInputLayout wrapperCarName;
    @Bind(R.id.et_registration_plates)
    EditText etRegistrationPlates;

    public NewCarDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_car);
        ButterKnife.bind(this);
        setTitle(R.string.add_new_car);
    }

    @OnClick(R.id.button)
    public void onClick() {
        String carName = etCarName.getText().toString();
        String carRegistration = etRegistrationPlates.getText().toString();
        if (carName.length() > 1) {
            String token = App.getCurrentToken(getContext());
            RequestParams params = Api.getRequestParams(token);
            params.put(Api.PARAM_CAR_NAME, carName);
            params.put(Api.PARAM_CAR_REGISTRATION, carRegistration);
            Api.post(Api.NEW_CAR, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        Toast.makeText(getContext(), R.string.car_added, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (statusCode == 400) {
                        try {
                            String jsonOutput = new String(responseBody, "UTF-8");
                            ApiError apiError = new Gson().fromJson(jsonOutput, ApiError.class);
                            Toast.makeText(getContext(), apiError.getError(), Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            wrapperCarName.setError(getContext().getResources().getString(R.string.car_name_empty));
        }
    }
}
