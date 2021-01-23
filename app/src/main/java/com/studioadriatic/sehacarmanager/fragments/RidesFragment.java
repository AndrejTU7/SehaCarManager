package com.studioadriatic.sehacarmanager.fragments;

import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;

/**
 * Created by kristijandraca@gmail.com
 */
public class RidesFragment extends ListFragment {
    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.rides));
    }

    @Override
    protected void loadListData() {

    }
}
