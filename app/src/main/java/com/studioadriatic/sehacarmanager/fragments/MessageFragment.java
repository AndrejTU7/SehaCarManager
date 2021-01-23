package com.studioadriatic.sehacarmanager.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MessageActivity;
import com.studioadriatic.sehacarmanager.adapters.MessageAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.models.Message;
import com.studioadriatic.sehacarmanager.models.User;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by kristijandraca@gmail.com
 */
public class MessageFragment extends Fragment {

    private static final String ARG_MESSAGE_ID = "arg_message_id";
    private static final String ARG_RECEIVER_NAME = "arg_receiver_name";
    @Bind(R.id.et_message)
    EditText etMessage;
    private int mMessageId;
    private User mCurrentUser;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    public static MessageFragment newInstance(int message_id, String receiver_name) {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MESSAGE_ID, message_id);
        bundle.putString(ARG_RECEIVER_NAME, receiver_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mMessageId = bundle.getInt(ARG_MESSAGE_ID);
            ((MessageActivity) getActivity()).setActionBarTitle(bundle.getString(ARG_RECEIVER_NAME));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCurrentUser = App.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadChat();

    }

    private void loadChat() {
        String token = App.getCurrentToken(getContext());
        RequestParams requestParams = Api.getRequestParams(token);
        requestParams.put(Api.PARAM_CHAT_ID, mMessageId);
        Api.get(Api.LIST_MESSAGES, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Message>>() {
                    }.getType();
                    String jsonOutput = new String(responseBody, "UTF-8");
                    List<Message> messageList = gson.fromJson(jsonOutput, listType);

                    MessageAdapter adapter = new MessageAdapter(getContext(), messageList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(messageList.size() - 1);
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.ib_send)
    public void onClick() {
        if (etMessage.getText().toString().length() > 0) {
            String token = App.getCurrentToken(getContext());
            RequestParams params = Api.getRequestParams(token);
            params.put(Api.PARAM_MESSAGE, etMessage.getText().toString());
            params.put(Api.PARAM_CHAT_ID, mMessageId);
            Api.post(Api.MESSAGES_SEND, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    etMessage.setText("");
                    loadChat();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }
}
