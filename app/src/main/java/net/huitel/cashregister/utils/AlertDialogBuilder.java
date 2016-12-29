package net.huitel.cashregister.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.huitel.cashregister.MainApp;
import net.huitel.cashregister.R;
import net.huitel.cashregister.ui.interfaces.IQuantityListener;

/**
 * Created by Alan on 22/04/2016.
 * Class to create all the alert dialogs the app needs.
 */
public class AlertDialogBuilder extends DialogFragment {
    /** Reference to the app to be able to reach string resources*/
    private static final Context mContext = MainApp.getContext();

    private static IQuantityListener mQuantityListener;

    /** Bundle key linked to an alert dialog's id*/
    private static final String KEY_ALERT_ID = "ALERT_ID";
    /** Bundle key linked to a product id*/
    private static final String KEY_PRODUCT_ID = "PRODUCT_ID";

    // IDs of alert dialogs.
    private static final int ALERT_ID_DELETE_CONFIRMATION = 0;
    private static final int ALERT_ID_MAX_QUANTITY_REACHED = 1;

    //Texts to be displayed in dialogs
    private static final String DELETE_CONFIRMATION_TITLE = mContext.getString(R.string.alert_delete_confirm_title);
    private static final String DELETE_CONFIRMATION_TEXT = mContext.getString(R.string.alert_delete_confirm_text);
    private static final String MAX_QUANTITY_TITLE = mContext.getString(R.string.alert_max_quantity_title);
    private static final String MAX_QUANTITY_TEXT = mContext.getString(R.string.alert_max_quantity_text);


    /**
     * Method called to create an alert pop-up, asking the user if he wants to delete an item
     * from his basket
     *
     * @return AlertDialogBuilder
     */
    public static AlertDialogBuilder getDeleteConfirmation(IQuantityListener listener, long productId) {
        mQuantityListener = listener;
        AlertDialogBuilder alertDialogBuilder = new AlertDialogBuilder();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ALERT_ID, ALERT_ID_DELETE_CONFIRMATION);
        bundle.putLong(KEY_PRODUCT_ID, productId);
        alertDialogBuilder.setArguments(bundle);
        return alertDialogBuilder;
    }

    /**
     * Method called to create an alert pop-up, warning the user that he cannot take more of a product
     * because of stock limit reached
     *
     * @return AlertDialogBuilder
     */
    public static AlertDialogBuilder getMaxQuantityWarning() {
        AlertDialogBuilder alertDialogBuilder = new AlertDialogBuilder();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ALERT_ID, ALERT_ID_MAX_QUANTITY_REACHED);
        alertDialogBuilder.setArguments(bundle);
        return alertDialogBuilder;
    }

    /**
     * @param savedInstanceState Bundle
     * @return Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int alertId = getArguments().getInt(KEY_ALERT_ID);
        switch (alertId){
            case ALERT_ID_DELETE_CONFIRMATION:
                return buildDeleteConfirmation(getArguments()).create();
            case ALERT_ID_MAX_QUANTITY_REACHED:
                return buildMaxQuantityReachedAlert().create();
            default:
                return null;
        }
    }

    /**
     * Builds the dialog called when the user wants to remove an item from its basket
     * @param bundle Bundle
     * @return initialized builder
     */
    private AlertDialog.Builder buildDeleteConfirmation(final Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(DELETE_CONFIRMATION_TITLE)
                .setMessage(DELETE_CONFIRMATION_TEXT)
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mQuantityListener.onRemoveRequired(bundle.getLong(KEY_PRODUCT_ID));
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder;
    }

    /**
     * Builds the dialog called when the user asks more products than allowed
     * @return initialized builder
     */
    private AlertDialog.Builder buildMaxQuantityReachedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(MAX_QUANTITY_TITLE)
                .setMessage(MAX_QUANTITY_TEXT)
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder;
    }
}
