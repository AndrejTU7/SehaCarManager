package com.studioadriatic.sehacarmanager.fragments;

import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.activities.MainActivity;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public class GuestsFragment extends ListFragment {
    @Override
    protected void setFragmentTitle() {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.vip_guests));
    }

    @Override
    protected void loadListData() {

    }
}
