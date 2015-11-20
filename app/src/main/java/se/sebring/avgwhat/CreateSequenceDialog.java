package se.sebring.avgwhat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

public class CreateSequenceDialog extends DialogFragment implements TextView.OnEditorActionListener {
    private static final String TAG = CreateSequenceDialog.class.getCanonicalName();

    private NewSequenceListener mListener;
    private TextView mTitle;
    private TextView mGoal;

    public interface NewSequenceListener {
        public void onNewSequence(Sequence newSequence);
    }

    public static CreateSequenceDialog newInstance(NewSequenceListener listener) {
        CreateSequenceDialog dlg = new CreateSequenceDialog();
        dlg.setListener(listener);

        return dlg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sequence_create_dialog, container);
        getDialog().setTitle("Create new");

        mTitle = (TextView) v.findViewById(R.id.title);
        mGoal = (TextView) v.findViewById(R.id.goal);
        mGoal.setOnEditorActionListener(this);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return v;
    }

    private void setListener(NewSequenceListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            Log.v(TAG, "Actrion done");
            // Return input text to activity
            Sequence s = new Sequence();
            s.setName(mTitle.getText().toString());
            s.setGoal(Integer.parseInt(mGoal.getText().toString()));

            mListener.onNewSequence(s);
            this.dismiss();
            return true;
        }
        return false;
    }
}
