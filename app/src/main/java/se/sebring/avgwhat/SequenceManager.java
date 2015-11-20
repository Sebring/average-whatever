package se.sebring.avgwhat;

import android.app.SearchManager;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SequenceManager {
    private static final String TAG = SearchManager.class.getCanonicalName();

    private static final String DEFAULT_SEQUENCE = "0#0#";
    private static final String PREF_CURRENT = "current";
    private static final String PREF_ALL_ACTIVE = "sequence_set";
    private static final String DATE_PATTERN = "yy.MM.dd";

    private SharedPreferences mPref;

    public interface SequenceHandler {
        public List<Sequence> getAllActive();
        public void updateSequence(Sequence sequence);
        public void addSequence(Sequence sequence);
        public void selectSequence(Sequence sequence);
        public void showContextMenu();
        public void openSequence(Sequence sequence);
        public void deleteSequence(Sequence sequence);

    }

    public SequenceManager(SharedPreferences pref) {
        mPref = pref;
    }

    /**
     * Update a sequence in the list of active sequences
     * @param sequence to be updated
     */
    public void updateSequence(Sequence sequence) {
        Log.v(TAG, "updateSequence");
        List<Sequence> all = getAll(PREF_ALL_ACTIVE);
        List<Sequence> copy = new ArrayList<>(all);
        boolean match = false;
        int pos = -1;
        for (Sequence s : all) {
            if (s.equals(sequence)) {
                Log.v(TAG, " - found sequence");
                pos = all.indexOf(s);
                match = true;
                break;
            }
        }
        if (pos!=-1) {
            all.remove(pos);
        }
        if (match) {
            all.add(sequence);
        }
        saveAll(PREF_ALL_ACTIVE, all);
    }

    /**
     * Delete a sequence from active list.
     * @param sequence to be deleted
     */
    public void deleteSequence(Sequence sequence) {
        deleteSequence(PREF_ALL_ACTIVE, sequence);
    }

    /**
     * Get a list of all active sequences.
     * @return list of all active sequences
     */
    public List<Sequence> getAllActive() {
        return getAll(PREF_ALL_ACTIVE);
    }

    /**
     * Save a sequence to list of active sequences.
     * @param sequence to be saved
     */
    public void addSequence(Sequence sequence) {
        addSequence(PREF_ALL_ACTIVE, sequence);
    }

    private List<Sequence> getAll(final String key) {
        Log.v(TAG, "getAll");
        Set<String> sequenceString = new HashSet<>(mPref.getStringSet(key, new HashSet<String>()));
        ArrayList<Sequence> sequences = new ArrayList<>(sequenceString.size());
        Gson gson = new Gson();
        for (String s : sequenceString) {
            sequences.add(gson.fromJson(s, Sequence.class));
        }
        Log.v(TAG, " - size: " + sequences.size());
        return sequences;
    }

    private void saveAll(String key, List<Sequence> list) {
        Log.v(TAG, "saveAll");
        Set<String> json = new HashSet<>(list.size());
        Gson gson = new Gson();
        for (Sequence sequence : list) {
            json.add(gson.toJson(sequence));
        }
        mPref.edit().putStringSet(key, json).apply();
    }

    private void addSequence(String key, Sequence sequence) {
        Set<String> sequences = new HashSet<>(mPref.getStringSet(key, new HashSet<String>()));
        sequences.add(new Gson().toJson(sequence));
        mPref.edit().putStringSet(key, sequences).apply();
    }

    private void deleteSequence(String key, Sequence sequence) {
        List<Sequence> sequences =  getAll(key);
        sequences.remove(sequence);
        saveAll(key, sequences);
    }
}
