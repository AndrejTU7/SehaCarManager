package com.studioadriatic.sehacarmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.models.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mList;
    private onUserClickListener mOnUserClickListener;

    public UsersAdapter(Context context, List<User> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.universal_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = mList.get(position);

        holder.subtitle.setText(getUserTypeText(user.getType()));
        holder.title.setText(user.getName());
        Glide.with(mContext).load(user.getPicture()).placeholder(R.drawable.def_profile).into(holder.image);

        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnUserClickListener != null) {
                    mOnUserClickListener.onUserClick(user);
                }
            }
        });

    }

    private String getUserTypeText(int type) {
        switch (type) {
            case User.ADMIN:
                return mContext.getResources().getString(R.string.administrator);
            case User.MODERATOR:
                return mContext.getResources().getString(R.string.moderator);
            case User.DRIVER:
                return mContext.getResources().getString(R.string.driver);
            case User.REFEREE_DRIVER:
                return mContext.getResources().getString(R.string.driver_referee);
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_image)
        CircularImageView image;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tv_subtitle)
        TextView subtitle;
        @Bind(R.id.item_root)
        RelativeLayout item_root;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnUserClickListener(onUserClickListener onUserClickListener) {
        this.mOnUserClickListener = onUserClickListener;
    }

    public interface onUserClickListener {
        void onUserClick(User User);
    }
}
