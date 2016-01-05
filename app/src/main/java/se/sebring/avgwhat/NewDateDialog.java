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

public class NewDateDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private static final String TAG = NewDateDialog.class.getCanonicalName();

    private DateDialogListener mListener;
    private TextView mEditText;

    public interface DateDialogListener {
        public void onNewDateDialogFinished(String name);
    }

    public static NewDateDialog newInstance(DateDialogListener listener) {

        Bundle args = new Bundle();
        NewDateDialog fragment = new NewDateDialog();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener(DateDialogListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.text);
        getDialog().setTitle("Enter new epoc");

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
            mListener.onNewDateDialogFinished(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
