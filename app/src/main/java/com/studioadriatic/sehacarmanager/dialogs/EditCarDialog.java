package com.studioadriatic.sehacarmanager.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.studioadriatic.sehacarmanager.models.Car;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by kristijandraca@gmail.com
 */
public class EditCarDialog extends Dialog {

    @Bind(R.id.et_car_name)
    EditText etCarName;
    @Bind(R.id.wrapper_car_name)
    TextInputLayout wrapperCarName;
    @Bind(R.id.et_registration_plates)
    EditText etRegistrationPlates;

    private Car mCar;

    public EditCarDialog(Context context, Car car) {
        super(context);
        this.mCar = car;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_car);
        ButterKnife.bind(this);
        setTitle(R.string.edit_car);

        etCarName.setText(mCar.getName());
        etRegistrationPlates.setText(mCar.getRegistration());
    }

    @OnClick({R.id.bt_delete, R.id.bt_save})
    public void onClick(View view) {
        String token = App.getCurrentToken(getContext());
        switch (view.getId()) {
            case R.id.bt_delete:
                RequestParams params = Api.getRequestParams(token);
                params.put(Api.PARAM_CAR_ID, mCar.getId());
                Api.post(Api.DELETE_CAR, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {
                            Toast.makeText(getContext(), R.string.car_deleted, Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.bt_save:
                String carName = etCarName.getText().toString();
                String carRegistration = etRegistrationPlates.getText().toString();
                if (carName.length() > 1) {
                    RequestParams params2 = Api.getRequestParams(token);
                    params2.put(Api.PARAM_CAR_ID, mCar.getId());
                    params2.put(Api.PARAM_CAR_NAME, carName);
                    params2.put(Api.PARAM_CAR_REGISTRATION, carRegistration);
                    Api.post(Api.UPDATE_CAR, params2, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                Toast.makeText(getContext(), R.string.car_edited, Toast.LENGTH_SHORT).show();
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
                break;
        }
    }
}
