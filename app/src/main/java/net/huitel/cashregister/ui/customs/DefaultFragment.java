package net.huitel.cashregister.ui.customs;

import android.content.Context;
import android.support.v4.app.Fragment;

import net.huitel.cashregister.ui.interfaces.IFragmentInteractionListener;

/**
 * Created by Alan on 09/04/2016.
 * Base fragment that every fragment in the app will extend
 */
public abstract class DefaultFragment extends Fragment {

    protected IFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        if(activity instanceof IFragmentInteractionListener)
            mListener = (IFragmentInteractionListener) activity;
        else
            throw new RuntimeException(activity.toString() + "must implement IFragmentInteractionListener");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }


    public abstract void setView();
}
