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
import com.studioadriatic.sehacarmanager.models.LocationModel;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;



/**
 * Created by Andrej on 17.2.2016..
 */
public class EditLocationDialog extends Dialog {

    @Bind(R.id.et_location)
    EditText etNewLocation;
    @Bind(R.id.wrapper_dialog_location)
    TextInputLayout wrapperDialogLocation;
    @Bind(R.id.et_location_address)
    EditText et_location_address;

    private LocationModel mLocationModel;

    public EditLocationDialog(Context context, LocationModel location) {
        super(context);
        this.mLocationModel = location;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_location);
        ButterKnife.bind(this);
        setTitle(R.string.enter_new_location);

        etNewLocation.setText(mLocationModel.getName());
        et_location_address.setText(mLocationModel.getAddress());

    }
    @OnClick({R.id.bt_delete, R.id.bt_save})
    public void onClick(View view) {
        String token = App.getCurrentToken(getContext());
        switch (view.getId()) {
            case R.id.bt_delete:
                RequestParams params = Api.getRequestParams(token);
                params.put(Api.PARAM_LOCATION_ID, mLocationModel.getId());
                Api.post(Api.DELETE_LOCATION, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {
                            Toast.makeText(getContext(), R.string.location_deleted, Toast.LENGTH_SHORT).show();
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
                String locationName = etNewLocation.getText().toString();
                String locationAddress = et_location_address.getText().toString();
                if (etNewLocation.length() > 1) {
                    RequestParams params2 = Api.getRequestParams(token);
                    params2.put(Api.PARAM_LOCATION_ID, mLocationModel.getId());
                    params2.put(Api.PARAM_LOCATION_NAME, locationName);
                    params2.put(Api.PARAM_LOCATION_ADDRESS, locationAddress);
                    Api.post(Api.UPDATE_LOCATION, params2, new AsyncHttpResponseHandler() {// da li tu ide UPDATE_LOCATION???
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                Toast.makeText(getContext(), R.string.location_edited, Toast.LENGTH_SHORT).show();
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
                break;
        }
    }
}


