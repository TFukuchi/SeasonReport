package fukuchi.junpou.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DataDetailDialogFragment extends DialogFragment {
    public static String DETAIL_DIALOG_TAG = "detail_dialog";

    public static String DETAIL_ITEMS_KEY = "detail_items_key";
    public static String DETAIL_CHECKED_STATE_KEY = "detail_checked_state_key";

    private static DialogInterface.OnMultiChoiceClickListener mMultiChoiceClickListener =
            new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // nop
                }
            };

    public static DataDetailDialogFragment newInstance(String[] items, boolean[] checkedState,
                                                       @NonNull DialogInterface.OnMultiChoiceClickListener listener) {
        DataDetailDialogFragment fragment = new DataDetailDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(DETAIL_ITEMS_KEY, items);
        args.putBooleanArray(DETAIL_CHECKED_STATE_KEY, checkedState);
        fragment.setArguments(args);

        mMultiChoiceClickListener = listener;

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] items = getArguments().getStringArray(DETAIL_ITEMS_KEY);
        boolean[] checkedState = getArguments().getBooleanArray(DETAIL_CHECKED_STATE_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Date details");
        builder.setMultiChoiceItems(items, checkedState, mMultiChoiceClickListener);
        return builder.create();
    }
}