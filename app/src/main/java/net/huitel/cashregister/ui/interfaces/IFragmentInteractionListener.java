package net.huitel.cashregister.ui.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by Alan on 09/04/2016.
 * This interface must be implemented by activities that contain fragments to allow
 * an interaction in this fragment to be communicated to the activity and
 * potentially other fragments contained in this activity.
 */
public interface IFragmentInteractionListener {

    /**
     * Asks for a fragment to be changed
     *
     * @param fragment Fragment to be displayed
     * @param TAG      Fragment's TAG
     */
    void onChangeFragment(Fragment fragment, String TAG);
}
