package com.google.eldeveloper13.photocabulary.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.eldeveloper13.photocabulary.R;
import com.google.eldeveloper13.photocabulary.models.Vocab;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eric on 27/12/2015.
 */
public class VocabListAdapter extends RecyclerView.Adapter<VocabListAdapter.VocabListViewHolder> {

    private List<Vocab> mList;
    private ItemClickListener mItemClickListener;

    public VocabListAdapter(List<Vocab> list) {
        mList = list;
    }

    @Override
    public VocabListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vocab_list, parent, false);
        return new VocabListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(VocabListViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position).getWord());
    }

    public Vocab getItem(int position) {
        return mList.get(position);
    }

    public ItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void updateList(List<Vocab> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class VocabListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.textView)
        TextView mTextView;

        public VocabListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClicked(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {

        void onItemClicked(View view, int position);
    }
}
