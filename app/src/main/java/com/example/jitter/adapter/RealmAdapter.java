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

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class RealmAdapter extends RealmBaseAdapter<TweetRealm> implements ListAdapter {

    private static class ViewHolder {
        private ImageView ivAvatar;
        private ImageView ivIsRetweet;
        private TextView tvUsername;
        private TextView tvText;
    }

    public RealmAdapter(Context context, RealmResults<TweetRealm> realmResults) {
        super(context, realmResults, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.avatar_image);
            viewHolder.ivIsRetweet = (ImageView) convertView.findViewById(R.id.retweet_image);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.author);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tweet);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TweetRealm item = realmResults.get(position);
        viewHolder.tvUsername.setText(item.getUserName());
        viewHolder.tvText.setText(item.getMessage());
        Picasso.with(context)
                .load(item.getImageUrl())
                .into(viewHolder.ivAvatar);

        boolean isAvatar = item.getIsRetweet();
        if (isAvatar) {
            viewHolder.ivIsRetweet.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivIsRetweet.setVisibility(View.GONE);
        }

        return convertView;
    }
}
