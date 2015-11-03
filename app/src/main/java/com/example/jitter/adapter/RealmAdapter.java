package com.example.jitter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.jitter.R;
import com.example.jitter.data.TweetRealm;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class RealmAdapter extends RealmBaseAdapter<TweetRealm> implements ListAdapter {

    static class ViewHolder {
        @Bind(R.id.avatar_image) ImageView ivAvatar;
        @Bind(R.id.retweet_image) ImageView ivIsRetweet;
        @Bind(R.id.author) TextView tvUsername;
        @Bind(R.id.tweet) TextView tvText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public RealmAdapter(Context context, RealmResults<TweetRealm> realmResults) {
        super(context, realmResults, true);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // set or get ViewHolder instance
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.list_row_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // set values to views retrieved from Realm
        TweetRealm item = realmResults.get(position);
        viewHolder.tvUsername.setText(String.format("@%s", item.getUserName()));
        viewHolder.tvText.setText(item.getMessage());
        Picasso.with(context)
                .load(item.getImageUrl())
                .into(viewHolder.ivAvatar);

        // if the tweet is retweet, show appropriate image
        if (item.getIsRetweet()) {
            viewHolder.ivIsRetweet.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivIsRetweet.setVisibility(View.GONE);
        }

        return view;
    }
}
