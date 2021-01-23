package com.studioadriatic.sehacarmanager.fragments;


import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.adapters.LocationAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.dialogs.EditLocationDialog;
import com.studioadriatic.sehacarmanager.dialogs.NewLocationDialog;
import com.studioadriatic.sehacarmanager.models.LocationModel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Andrej on 15.2.2016..
 */
public class LocationFragment extends ListFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.locations));
    }

    @Override
    protected void loadListData() {
        String token = App.getCurrentToken(getContext());
        {
            Api.get(Api.LIST_LOCATIONS, Api.getRequestParams(token), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<LocationModel>>() {
                        }.getType();
                        String jsonOutput = new String(responseBody, "UTF-8");
                        final List<LocationModel> locationModelList = gson.fromJson(jsonOutput, listType);

                        LocationAdapter adapter = new LocationAdapter(getContext(), locationModelList);
                        adapter.setOnLocationClickListener(new LocationAdapter.onLocationClickListener() {
                            @Override
                            public void onLocationClick(LocationModel locationModel) {
                                EditLocationDialog editlocationDialog = new EditLocationDialog(getContext(), locationModel);
                                editlocationDialog.show();
                                editlocationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        loadListData();
                                    }
                                });
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
        }}
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.fab)
        public void onClick() {
            NewLocationDialog newLocationDialog = new NewLocationDialog(getContext());
            newLocationDialog.show();
            newLocationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loadListData();
                }
            });
        }


    }




