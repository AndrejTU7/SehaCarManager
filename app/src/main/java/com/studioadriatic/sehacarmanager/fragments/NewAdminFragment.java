package com.studioadriatic.sehacarmanager.fragments;

import android.view.View;
import android.widget.ArrayAdapter;

import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.api.Api;

import java.io.FileNotFoundException;

import butterknife.OnClick;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public class NewAdminFragment extends NewUserFragment {
    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.create_new_admin));
    }

    @Override
    protected void setUpSpinner() {
        String[] admin_type = {getResources().getString(R.string.administrator), getResources().getString(R.string.moderator)};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, admin_type);
        spinner.setAdapter(stringArrayAdapter);
    }

    @OnClick(R.id.bt_save)
    public void onClick(View view) {
        if (checkForm()) {
            RequestParams params = new RequestParams();
            params.put(Api.PARAM_EMAIL, etEmail.getText().toString());
            params.put(Api.PARAM_NAME, etEmail.getText().toString());
            params.put(Api.PARAM_PASSWORD, etEmail.getText().toString());
            params.put(Api.PARAM_TYPE, spinner.getSelectedItemPosition());
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
