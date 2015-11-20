package se.sebring.avgwhat;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import se.sebring.avgwhat.databinding.SequenceListItemBinding;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.ViewHolder>
{
    private View mEmpty;
    private List<Sequence> mList;
    private SequenceManager.SequenceHandler mHandler;

    public SequenceAdapter(View empty, List<Sequence> list, SequenceManager.SequenceHandler handler) {
        mEmpty = empty;
        mList = list;
        mHandler = handler;
        if (mList == null)
            mList = new ArrayList<Sequence>();
        setDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SequenceListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.sequence_list_item, parent, false);

        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<Sequence> list) {
        mList = list;
        setDataSetChanged();
    }

    private void setDataSetChanged() {
        this.notifyDataSetChanged();
        if (mList == null || mList.isEmpty()) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        SequenceListItemBinding mBinding;
        public ViewHolder(View itemView, SequenceListItemBinding binding) {
            super(itemView);
            mBinding = binding;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(final Sequence sequence) {
            mBinding.setItem(sequence);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Sequence s = mList.get(position);
            // We can access the data within the views
            Log.v("Viewholder", "Thau clicked on " + s.getName());
            mHandler.openSequence(s);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Sequence s = mList.get(position);
            // We can access the data within the views
            Log.v("Viewholder", "Thau longclicked on " + s.getName());
            mHandler.selectSequence(s);
            mHandler.showContextMenu();
            v.setSelected(true);
            return true;
        }
    }



}
