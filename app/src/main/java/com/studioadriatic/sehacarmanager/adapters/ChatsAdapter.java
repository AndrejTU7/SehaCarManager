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
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.models.Chats;
import com.studioadriatic.sehacarmanager.models.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kristijandraca@gmail.com
 */
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private Context mContext;
    private List<Chats> mChatList;
    private OnChatClick mOnChatClick;

    public ChatsAdapter(Context context, List<Chats> chatList) {
        mChatList = chatList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.universal_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Chats chat = mChatList.get(position);
        User otherUser = getOtherUser(chat);
        Glide.with(mContext).load(otherUser.getPicture()).placeholder(R.drawable.def_profile).into(holder.image);
        holder.title.setText(otherUser.getName());
        if (chat.getLast_message().getMessage() != null) {
            holder.subtitle.setText(chat.getLast_message().getMessage().replace("<br>", "\n"));
        }
        holder.subtitle.setMaxLines(2);
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChatClick != null) {
                    mOnChatClick.onChatClick(chat);
                }
            }
        });
    }

    private User getOtherUser(Chats chat) {
        User currentUser = App.getCurrentUser();
        if (currentUser.getId() == chat.getUser_1().getId()) {
            return chat.getUser_2();
        }
        return chat.getUser_1();
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
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

    public void setOnChatClick(OnChatClick mOnChatClick) {
        this.mOnChatClick = mOnChatClick;
    }

    public interface OnChatClick {
        void onChatClick(Chats chat);
    }
}
