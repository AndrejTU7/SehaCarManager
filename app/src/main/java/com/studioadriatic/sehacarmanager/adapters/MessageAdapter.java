package com.studioadriatic.sehacarmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;
import com.studioadriatic.sehacarmanager.App;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.models.Message;
import com.studioadriatic.sehacarmanager.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kristijandraca@gmail.com
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_OTHER = 1;
    private List<Message> mMessageList;
    private Context mContext;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.mContext = context;
        this.mMessageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_USER:
                return new ViewHolderUser(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_right, parent, false));
            case VIEW_TYPE_OTHER:
                return new ViewHolderOther(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_left, parent, false));
            default:
                return new ViewHolderUser(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.message_text.setText(message.getMessage().replace("<br>", "\n"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(message.getTime());
        } catch (ParseException ignored) {
        }
        PrettyTime prettyTime = new PrettyTime();
        holder.time_text.setText(prettyTime.format(convertedDate));

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(mMessageList.get(position).getSender());
    }

    private int getViewType(User unknown) {
        User currentUser = App.getCurrentUser();
        if (currentUser.getId() == unknown.getId()) {
            return VIEW_TYPE_USER;
        }
        return VIEW_TYPE_OTHER;
    }

    public class ViewHolderUser extends ViewHolder {
        public ViewHolderUser(View view) {
            super(view);
        }
    }

    public class ViewHolderOther extends ViewHolder {
        public ViewHolderOther(View view) {
            super(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message_text)
        TextView message_text;
        @Bind(R.id.time_text)
        TextView time_text;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
