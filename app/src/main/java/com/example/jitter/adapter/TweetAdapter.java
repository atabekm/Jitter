package com.example.jitter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jitter.R;
import com.example.jitter.data.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    private static List<Tweet> mData;
    private Context mContext;

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;
        private ImageView ivIsRetweet;
        private TextView tvUsername;
        private TextView tvText;

        public TweetViewHolder(View v) {
            super(v);
            ivAvatar = (ImageView) v.findViewById(R.id.avatar_image);
            ivIsRetweet = (ImageView) v.findViewById(R.id.retweet_image);
            tvUsername = (TextView) v.findViewById(R.id.author);
            tvText = (TextView) v.findViewById(R.id.tweet);
        }

        public ImageView getIvAvatar() {
            return ivAvatar;
        }

        public ImageView getIvIsRetweet() {
            return ivIsRetweet;
        }

        public TextView getTvUsername() {
            return tvUsername;
        }

        public TextView getTvText() {
            return tvText;
        }
    }

    public TweetAdapter(List<Tweet> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TweetViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_item, parent, false);

        vh = new TweetViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.getTvUsername().setText(mData.get(position).getAuthor());
        holder.getTvText().setText(mData.get(position).getText());
        Picasso.with(mContext)
                .load(mData.get(position).getAvatarUrl())
                .into(holder.getIvAvatar());

        boolean isAvatar = mData.get(position).isRetweet();
        if (isAvatar) {
            holder.getIvIsRetweet().setVisibility(View.VISIBLE);
        } else {
            holder.getIvIsRetweet().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateResults(List<Tweet> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
