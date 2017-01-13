package fukuchi.junpou.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class CompleteDialogFragment extends DialogFragment {

    public static String DIALOG_TAG = "dialog_complete";

    public interface DialogListener {
        void onPreview();

        void onSendMail();
    }

    private DialogListener mListener;

    public void setListener(DialogListener listener){
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] items = {"プレビューの表示", "メールに添付"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mListener.onPreview();
                        break;
                    case 1:
                        mListener.onSendMail();
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}
