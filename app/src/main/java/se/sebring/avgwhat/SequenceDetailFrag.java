package se.sebring.avgwhat;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import se.sebring.avgwhat.databinding.SequenceDetailsBinding;

public class SequenceDetailFrag extends Fragment implements AddTotalDialog.AddTotalDialogListener, EditDialog.EditDialogListener, NewDateDialog.DateDialogListener {
    private static final String TAG = SequenceDetailFrag.class.getCanonicalName();

    public static final int EXTRA_NAME = 1;
    public static final int EXTRA_GOAL = 2;

    private Sequence mSequence;
    private SequenceManager.SequenceHandler mHandler;

    public static SequenceDetailFrag newInstance(Sequence sequence) {
        SequenceDetailFrag frag = new SequenceDetailFrag();
        Bundle args = new Bundle();
        args.putSerializable(Sequence.KEY, sequence);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SequenceManager.SequenceHandler) {
            mHandler = (SequenceManager.SequenceHandler) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");

        mSequence = (Sequence) getArguments().getSerializable(Sequence.KEY);
        final SequenceDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.sequence_details, container, false);

        binding.setItem(mSequence);
        View view = binding.getRoot();

        // FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "FAB!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                onFabClick(view);
            }
        });

        updateTitle();
        setHasOptionsMenu(true);
        return view;
    }

    private void updateTitle() {
        String name = mSequence.getName();
        if (!name.isEmpty()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(name);
        }
        String title = getString(R.string.aiming_goal, mSequence.getGoal()); // FIXME: use period
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(title);
    }

    public void onFabClick(View view) {
        Log.v(TAG, "onFabClick");
        FragmentManager fm = getChildFragmentManager();
        AddTotalDialog dlg = AddTotalDialog.newInstance(this, AddTotalDialog.FLAG_COUNT);
        dlg.show(fm, "dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current, menu);
        int show = mSequence.getName().isEmpty() ? MenuItem.SHOW_AS_ACTION_ALWAYS : MenuItem.SHOW_AS_ACTION_NEVER;
        menu.findItem(R.id.action_name).setShowAsAction(show);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Snackbar.make(getView(), "Settings!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.action_name:
                EditDialog dlg = EditDialog.newInstance(this, R.layout.string_edit_dialog, EXTRA_NAME);
                dlg.setTitle(getString(R.string.action_rename));
                dlg.setEditData(R.id.text, InputType.TYPE_CLASS_TEXT);
                dlg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.action_goal:
                EditDialog goalDlg = EditDialog.newInstance(this, R.layout.string_edit_dialog, EXTRA_GOAL);
                goalDlg.setTitle(getString(R.string.action_goal));
                goalDlg.setEditData(R.id.text, InputType.TYPE_CLASS_NUMBER);
                goalDlg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.action_date:
                NewDateDialog dateDlg = NewDateDialog.newInstance(this);
                dateDlg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.action_save:
                Snackbar.make(getView(), "Save!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddTotalDialogFinished(int inputValue, int flag) {
        switch(flag) {
            case AddTotalDialog.FLAG_COUNT:
                mSequence.incrementCount(inputValue);
                break;
        }
        mHandler.updateSequence(mSequence);
    }

    @Override
    public void onEditDialogFinished(String name, int extra) {
        switch(extra) {
            case EXTRA_NAME:
                mSequence.setName(name);
                break;
            case EXTRA_GOAL:
                mSequence.setGoal(Integer.parseInt(name));
                break;
        }
        updateTitle();
        mHandler.updateSequence(mSequence);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onNewDateDialogFinished(String name) {
        mSequence.setStartDate(new Date(Long.parseLong(name)));
        mHandler.updateSequence(mSequence);
    }
}
