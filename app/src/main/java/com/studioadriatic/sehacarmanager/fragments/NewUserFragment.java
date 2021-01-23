package com.studioadriatic.sehacarmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.soundcloud.android.crop.Crop;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.api.Api;
import com.studioadriatic.sehacarmanager.api.ApiError;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by kristijandraca@gmail.com
 */
public abstract class NewUserFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 500;
    @Bind(R.id.iv_image)
    CircularImageView ivImage;
    @Bind(R.id.et_user_name)
    EditText etDriverName;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.wrapper_email)
    TextInputLayout wrapper_email;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.password_wrapper)
    TextInputLayout passwordWrapper;
    @Bind(R.id.et_password2)
    EditText etPassword2;
    @Bind(R.id.password_wrapper2)
    TextInputLayout passwordWrapper2;
    @Bind(R.id.bt_save)
    Button btSave;
    @Bind(R.id.wrapper_user_name)
    TextInputLayout wrapperUserName;

    public File imageFile = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentTitle();
    }

    protected abstract void setFragmentTitle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_new_user, container, false);
        ButterKnife.bind(this, inflate);

        setUpSpinner();

        return inflate;
    }

    protected abstract void setUpSpinner();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            ivImage.setImageBitmap(imageBitmap);

        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Seha");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFile = new File(
                    mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"
            );

            Uri uri = data.getData();
            Uri imageUri = Uri.fromFile(imageFile);
            Crop.of(uri, imageUri).asSquare().start(getContext(), this);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.iv_image)
    public void onClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_pic)), PICK_IMAGE_REQUEST);
    }

    public boolean checkForm() {
        wrapperUserName.setError("");
        wrapper_email.setError("");
        passwordWrapper.setError("");
        passwordWrapper2.setError("");
        if (etDriverName.getText().toString().length() < 1) {
            wrapperUserName.setError(getResources().getString(R.string.name_empty));
            return false;
        }
        if (!isEmailValid(etEmail.getText().toString())) {
            wrapper_email.setError(getResources().getString(R.string.error_valid_email));
            return false;
        }
        if (etPassword.getText().toString().length() < 4) {
            passwordWrapper.setError(getResources().getString(R.string.password_short));
            return false;
        }
        if (!etPassword.getText().toString().equals(etPassword2.getText().toString())) {
            passwordWrapper2.setError(getResources().getString(R.string.password_not_same));
            return false;
        }
        return true;
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void registerUser(RequestParams params) {
        Api.post(Api.USER_REGISTRATION, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("TAG", String.valueOf(statusCode));
                if (statusCode == 200) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("TAG", String.valueOf(statusCode));
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
}
