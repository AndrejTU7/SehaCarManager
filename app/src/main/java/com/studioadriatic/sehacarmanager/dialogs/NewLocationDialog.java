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
 * Created by Andrej on 17.2.2016..
 */
public class NewLocationDialog extends Dialog{
    @Bind(R.id.et_location)
    EditText etNewLocation;
    @Bind(R.id.wrapper_dialog_location)
    TextInputLayout wrapperDialogLocation;
    @Bind(R.id.et_location_address)
    EditText et_location_address;
    public NewLocationDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_location);
        ButterKnife.bind(this);
        setTitle(R.string.enter_new_location);
    }

    @OnClick(R.id.bt_save)
    public void onClick() {
        String location_name = etNewLocation.getText().toString();
        String location_address = et_location_address.getText().toString();
        if (location_name.length() > 1) {
            String token = App.getCurrentToken(getContext());
            RequestParams params = Api.getRequestParams(token);
            params.put(Api.PARAM_LOCATION_NAME, location_name);
            params.put(Api.PARAM_LOCATION_ADDRESS, location_address);
            Api.post(Api.NEW_LOCATION, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        Toast.makeText(getContext(), R.string.location_added, Toast.LENGTH_SHORT).show();
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
            wrapperDialogLocation.setError(getContext().getResources().getString(R.string.location_name_empty));
        }
    }
}

