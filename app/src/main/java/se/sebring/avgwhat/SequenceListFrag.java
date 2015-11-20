package se.sebring.avgwhat;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SequenceListFrag extends Fragment implements SequenceManager.SequenceHandler {
    private static final String TAG = SequenceListFrag.class.getCanonicalName();

    private SequenceAdapter mAdapter;
    private CreateSequenceDialog.NewSequenceListener mListener;
    private SequenceManager.SequenceHandler mHandler;
    private Sequence mSequence;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "inAttach");
        if (context instanceof CreateSequenceDialog.NewSequenceListener) {
            Log.v(TAG, " - listener");
            mListener = (CreateSequenceDialog.NewSequenceListener) context;
        }
        if (context instanceof SequenceManager.SequenceHandler) {
            Log.v(TAG, " - handler");
            mHandler = (SequenceManager.SequenceHandler) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sequence_list, container, false);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);



        List<Sequence> allActive = mHandler.getAllActive();
        // specify an adapter (see also next example)
        mAdapter = new SequenceAdapter(v.findViewById(R.id.empty), allActive, this);
        mRecyclerView.setAdapter(mAdapter);

        // fab
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "FAB!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                onFabClick(view);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Sequence> allActive = mHandler.getAllActive();
        mAdapter.notifyDataSetChanged();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        String[] subtitles = getResources().getStringArray(R.array.app_subtitle);
        int i = (int) Math.floor(Math.random()*subtitles.length);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(subtitles[i]);

    }

    private void onFabClick(View view) {
        CreateSequenceDialog dlg = CreateSequenceDialog.newInstance(mListener);
        dlg.show(getChildFragmentManager(), "dialog");
    }


    @Override
    public List<Sequence> getAllActive() {
        return mHandler.getAllActive();
    }

    @Override
    public void updateSequence(Sequence sequence) {
        mHandler.updateSequence(sequence);
    }

    @Override
    public void addSequence(Sequence sequence) {
        mHandler.addSequence(sequence);
        mAdapter.addAll(getAllActive());
    }

    @Override
    public void selectSequence(Sequence sequence) {
        mSequence = sequence;
        mHandler.selectSequence(sequence);
    }

    @Override
    public void showContextMenu() {
        ((AppCompatActivity)getActivity()).startSupportActionMode(mActionModeCallback);
    }

    @Override
    public void openSequence(Sequence sequence) {
        mHandler.openSequence(sequence);
    }

    @Override
    public void deleteSequence(Sequence sequence) {
        mHandler.deleteSequence(sequence);
        mAdapter.addAll(getAllActive());
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.sequence_context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSequence(mSequence);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // mActionMode = null;
        }
    };
}
