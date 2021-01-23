package com.studioadriatic.sehacarmanager.fragments;

import android.view.View;
import android.widget.ArrayAdapter;

import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.models.User;

import java.io.FileNotFoundException;

import butterknife.OnClick;

/**
 * Created by kristijandraca@gmail.com
 */
public class NewDriverFragment extends NewUserFragment {

    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.create_new_driver));
    }

    @Override
    protected void setUpSpinner() {
        String[] driver_type = {getResources().getString(R.string.driver), getResources().getString(R.string.driver_referee)};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, driver_type);
        spinner.setAdapter(stringArrayAdapter);
    }

    @OnClick(R.id.bt_save)
    public void onClick(View view) {
        if (checkForm()) {
            RequestParams params = new RequestParams();
            params.put(Api.PARAM_EMAIL, etEmail.getText().toString());
            params.put(Api.PARAM_NAME, etEmail.getText().toString());
            params.put(Api.PARAM_PASSWORD, etEmail.getText().toString());
            int type = -1;
            if (spinner.getSelectedItemPosition() == 0) {
                type = User.DRIVER;
            } else if (spinner.getSelectedItemPosition() == 1) {
                type = User.REFEREE_DRIVER;
            }
            params.put(Api.PARAM_TYPE, type);
            if (imageFile != null) {
                try {
                    params.put(Api.PARAM_IMAGE, imageFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            registerUser(params);
        }
    }
}
