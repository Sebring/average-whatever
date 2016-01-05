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

public class EditDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private static final String TAG = EditDialog.class.getCanonicalName();
    private EditDialogListener mListener;
    private int mLayout;
    private String mTitle;
    private int mId;
    private int[] mEditData;
    private TextView mEditText;

    public interface EditDialogListener {
        public void onEditDialogFinished(String name, int id);
    }

    public static EditDialog newInstance(EditDialogListener listener, int layout, int id) {

        Bundle args = new Bundle();
        EditDialog fragment = new EditDialog();
        fragment.setListener(listener);
        fragment.setLayout(layout);
        fragment.setId(id);
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener(EditDialogListener listener) { mListener = listener; }
    private void setLayout(int layout) { mLayout = layout; }
    private void setId(int id){ mId = id; }
    public void setTitle(String title) {mTitle = title; }

    public void setEditData(int id, int type) {
        mEditData = new int[] {id, type};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mLayout, container);
        mEditText = (EditText) view.findViewById(mEditData[0]);
        mEditText.setInputType(mEditData[1]);
        getDialog().setTitle(mTitle);

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            mListener.onEditDialogFinished(mEditText.getText().toString(), mId);
            this.dismiss();
            return true;
        }
        return false;
    }
}
