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

public class AddTotalDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private static final String FLAG = "flag";
    public static final int FLAG_COUNT = 0;
    public static final int FLAG_GOAL = 1;

    public interface AddTotalDialogListener {
        void onAddTotalDialogFinished(int inputValue, int flag);
    }

    private EditText mEditText;
    private AddTotalDialogListener mListener;

    public static AddTotalDialog newInstance(AddTotalDialogListener listener, int flag) {
        Bundle args = new Bundle();
        args.putInt(FLAG, flag);
        AddTotalDialog frag = new AddTotalDialog();
        frag.setListener(listener);
        frag.setArguments(args);
        return frag;
    }

    public void setListener(AddTotalDialogListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.text);

        int flag = getArguments().getInt(FLAG);
        String title = flag == FLAG_COUNT ? getString(R.string.label_add) : getString(R.string.label_goal);
        getDialog().setTitle(title);

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

            mListener.onAddTotalDialogFinished(Integer.parseInt(mEditText.getText().toString()), getArguments().getInt(FLAG));
            this.dismiss();
            return true;
        }
        return false;
    }
}