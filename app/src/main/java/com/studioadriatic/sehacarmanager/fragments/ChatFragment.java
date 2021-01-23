package com.studioadriatic.sehacarmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.Utils;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.activities.MessageActivity;
import com.studioadriatic.sehacarmanager.adapters.ChatsAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.models.Chats;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class ChatFragment extends ListFragment {
    public static final String TAG = "ChatFragment";
    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.chat));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fab.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    protected void loadListData() {
        String token = App.getCurrentToken(getContext());
        Api.get(Api.LIST_CHATS, Api.getRequestParams(token), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Chats>>() {
                    }.getType();
                    String jsonOutput = new String(responseBody, "UTF-8");
                    List<Chats> chatList = gson.fromJson(jsonOutput, listType);
                    ChatsAdapter adapter = new ChatsAdapter(getContext(), chatList);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnChatClick(new ChatsAdapter.OnChatClick() {
                        @Override
                        public void onChatClick(Chats chat) {
                            Intent intent = new Intent(getContext(), MessageActivity.class);
                            intent.putExtra(MessageActivity.ARG_CHAT_ID, chat.getId());
                            intent.putExtra(MessageActivity.ARG_OTHER_NAME, Utils.getOtherUser(chat).getName());
                            startActivity(intent);
                        }
                    });
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
        startActivity(new Intent(getContext(), MessageActivity.class));
    }
}
