package se.sebring.avgwhat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class RenameDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private static final String TAG = RenameDialog.class.getCanonicalName();
    private RenameDialogListener mListener;
    private TextView mEditText;

    public interface RenameDialogListener {
        public void onRenameDialogFinished(String name);
    }

    public static RenameDialog newInstance(RenameDialogListener listener) {

        Bundle args = new Bundle();
        RenameDialog fragment = new RenameDialog();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener(RenameDialogListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.string_edit_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        getDialog().setTitle(getString(R.string.action_rename));

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            mListener.onRenameDialogFinished(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
