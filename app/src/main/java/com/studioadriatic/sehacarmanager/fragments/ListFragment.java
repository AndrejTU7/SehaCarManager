package com.studioadriatic.sehacarmanager.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public abstract class ListFragment extends Fragment {
    public User mCurrentUser;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_list_with_fab, container, false);
        ButterKnife.bind(this, inflate);

        mCurrentUser = App.getCurrentUser();
        if (mCurrentUser.getType() != User.ADMIN && mCurrentUser.getType() != User.MODERATOR) {
            fab.setVisibility(View.GONE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return inflate;
    }

    protected abstract void setFragmentTitle();

    @Override
    public void onResume() {
        super.onResume();
        loadListData();
    }

    protected abstract void loadListData();

}
