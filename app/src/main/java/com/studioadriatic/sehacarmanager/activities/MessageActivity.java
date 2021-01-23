package com.studioadriatic.sehacarmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.fragments.MessageFragment;
import com.studioadriatic.sehacarmanager.fragments.SelectContactFragment;

/**
 * Created by kristijandraca@gmail.com
 */
public class MessageActivity extends AppCompatActivity {
    public static final String ARG_CHAT_ID = "arg_chat_id";
    public static final String ARG_OTHER_NAME = "arg_other_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            fragmentTransaction.replace(R.id.container, new SelectContactFragment()).commitAllowingStateLoss();
        } else {
            fragmentTransaction.replace(R.id.container, MessageFragment.newInstance(extras.getInt(ARG_CHAT_ID), extras.getString(ARG_OTHER_NAME))).commitAllowingStateLoss();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
