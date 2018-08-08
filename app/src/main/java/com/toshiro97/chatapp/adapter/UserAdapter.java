package com.toshiro97.chatapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toshiro97.chatapp.R;
import com.toshiro97.chatapp.listener.UserClickListener;
import com.toshiro97.chatapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private UserClickListener userClickListener;

    public UserAdapter(List<User> userList,UserClickListener userClickListener) {
        this.userList = userList;
        this.userClickListener = userClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_layout, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
        User user = userList.get(position);
        holder.imgAvatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
        holder.tvNameUser.setText(user.getName());
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickListener.onClickUser(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgAvatar;
        public TextView tvNameUser;
        public CardView cardItem;

        public UserViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            cardItem = itemView.findViewById(R.id.cardItem);
        }
    }
}
