package com.studioadriatic.sehacarmanager.fragments;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.Utils;
import com.studioadriatic.sehacarmanager.activities.MessageActivity;
import com.studioadriatic.sehacarmanager.adapters.UsersAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.models.Chats;
import com.studioadriatic.sehacarmanager.models.User;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kristijandraca@gmail.com
 */
public class SelectContactFragment extends ListFragment {
    @Override
    protected void setFragmentTitle() {
        ((MessageActivity) getActivity()).setActionBarTitle(getString(R.string.select_contact));
    }

    @Override
    public void onStart() {
        super.onStart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void loadListData() {
        Api.get(Api.LIST_CONTACTS, Api.getRequestParams(App.getCurrentToken(getContext())), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
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
                                setUpNewChat(user);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setUpNewChat(User user) {
        String token = App.getCurrentToken(getContext());
        RequestParams params = Api.getRequestParams(token);
        Api.post(Api.MESSAGES_ID, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        String jsonOutput = new String(responseBody, "UTF-8");
                        Chats chat = new Gson().fromJson(jsonOutput, Chats.class);
                        getFragmentManager().beginTransaction().replace(R.id.container, MessageFragment.newInstance(chat.getId(), Utils.getOtherUser(chat).getName())).commitAllowingStateLoss();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
