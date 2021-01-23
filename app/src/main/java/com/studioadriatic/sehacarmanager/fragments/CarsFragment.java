package com.studioadriatic.sehacarmanager.fragments;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;
import com.studioadriatic.sehacarmanager.adapters.CarsAdapter;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;
import com.studioadriatic.sehacarmanager.dialogs.EditCarDialog;
import com.studioadriatic.sehacarmanager.dialogs.NewCarDialog;
import com.studioadriatic.sehacarmanager.models.Car;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarsFragment extends ListFragment {

    public CarsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.cars));
    }

    /**
     * Load data to RecyclerView
     */
    protected void loadListData() {
        String token = App.getCurrentToken(getContext());
        Api.get(Api.LIST_CARS, Api.getRequestParams(token), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Car>>() {
                    }.getType();
                    String jsonOutput = new String(responseBody, "UTF-8");
                    List<Car> carList = gson.fromJson(jsonOutput, listType);

                    CarsAdapter adapter = new CarsAdapter(getContext(), carList);
                    adapter.setOnCarClickListener(new CarsAdapter.onCarClickListener() {
                        @Override
                        public void onCarClick(Car car) {
                            EditCarDialog editCarDialog = new EditCarDialog(getContext(), car);
                            editCarDialog.show();
                            editCarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        NewCarDialog newCarDialog = new NewCarDialog(getContext());
        newCarDialog.show();
        newCarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadListData();
            }
        });
    }


}
