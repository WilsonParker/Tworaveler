package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Fragment.Page.FragmentFeedSchedule;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.ScrollEndMethod;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hare on 2017-08-01.
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {
    private ArrayList<ScheduleModel> items;
    private OnListScrollListener onListScrollListenrer;
    private int scrollCount = 0;

    public FeedListAdapter(ArrayList<ScheduleModel> items, OnListScrollListener onListScrollListenrer) {
        this.items = items;
        this.onListScrollListenrer = onListScrollListenrer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
        ScrollEndMethod.getInstance().actionAfterScrollEnd(items.size(), position, onListScrollListenrer);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView IV_cover;
        private CircleImageView CV_profile;
        private TextView TV_nickname, TV_message, TV_title, TV_date, TV_like, TV_comment;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_feed$IV_cover);
            CV_profile = uiFactory.createView(R.id.item_feed$CV_profile);
            TV_date = uiFactory.createView(R.id.item_feed$TV_date);
            //        TV_nickname, TV_message, TV_title, TV_date, TV_like, TV_comment
            TV_nickname = uiFactory.createView(R.id.item_feed$TV_nickname);
            TV_message = uiFactory.createView(R.id.item_feed$TV_message);
            TV_title = uiFactory.createView(R.id.item_feed$TV_title);
            TV_date = uiFactory.createView(R.id.item_feed$TV_date);
            TV_like = uiFactory.createView(R.id.item_feed$TV_like);
            TV_comment = uiFactory.createView(R.id.item_feed$TV_comment);

            ArrayList<TextView> textlist1 = new ArrayList<>();
            ArrayList<TextView> textlist2 = new ArrayList<>();
            textlist1.add(TV_nickname);
            textlist1.add(TV_date);
            textlist1.add(TV_like);
            textlist1.add(TV_comment);
            FontManager.getInstance().setFont(textlist1, "Roboto-Medium.ttf");
            textlist2.add(TV_message);
            textlist2.add(TV_title);
            FontManager.getInstance().setFont(textlist2, "NotoSansCJKkr-Regular.otf");

        }

        public void toBind(ScheduleModel model) {
            IV_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager.getInstance().setFragmentContent(FragmentFeedSchedule.newInstance(model));
                }
            });
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getProfile_pic_thumbnail(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_history_profile).centerCrop(), CV_profile);
            TV_nickname.setText(model.getNickname());
            TV_message.setText(model.getStatus_message());
            TV_title.setText(model.getTripName());
            TV_like.setText(model.getLikeCount());
            TV_comment.setText(model.getCommentCount());
            TV_date.setText(model.getStart_date() + " ~ " + model.getEnd_date());
        }
    }

}
