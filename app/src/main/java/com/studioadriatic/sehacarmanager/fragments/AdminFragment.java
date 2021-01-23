package com.studioadriatic.sehacarmanager.fragments;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.adapters.UsersAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.models.User;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public class AdminFragment extends ListFragment {
    public static final String TAG = "AdminFragment";

    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.administrators));
    }

    @Override
    protected void loadListData() {
        String token = App.getCurrentToken(getContext());
        Api.get(Api.LIST_ADMINS, Api.getRequestParams(token), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    String jsonOutput = new String(responseBody, "UTF-8");
                    List<User> userList = gson.fromJson(jsonOutput, listType);

                    UsersAdapter adapter = new UsersAdapter(getContext(), userList);
                    adapter.setOnUserClickListener(new UsersAdapter.onUserClickListener() {
                        @Override
                        public void onUserClick(User user) {

                        }
                    });
                    recyclerView.setAdapter(adapter);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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
    }

    @OnClick(R.id.fab)
    public void onClick() {
        getFragmentManager().beginTransaction().replace(R.id.content, new NewAdminFragment()).addToBackStack(TAG).commitAllowingStateLoss();
    }
}
