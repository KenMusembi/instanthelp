package com.example.instanthelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/*
Recycler View for the Chat feature
What does a recycler view do?
 */
public class ChatRVAdapter extends RecyclerView.Adapter {

    //ArrayList and context can be final? what does final mean anyway
    private ArrayList<ChatsModel> chatsModelArrayList;
    private Context context;

    //Condtructor for the ChatRVAdapter class, loaded with arraylist and context
    public ChatRVAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context) {
        this.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
        // below code is to switch our layout type along with view holder.
       switch(viewType){
           //case 0 is when it is the user
           case 0:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item, parent, false);
               return new UserViewHolder(view);
           //case 1 is when it is the bot
           case 1:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
               return new BotViewHolder(view);
       }
       //why am i returning null when there is @NonNull
       return null;
    }

    //here we bind the content to the specific view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //we use chats model, which has sender parameter we use to switch the layout
        ChatsModel chatsModel = chatsModelArrayList.get(position);
        switch(chatsModel.getSender()){
            //if sender is user, switch the view to the user Text view, userTv
            case "user":
                ((UserViewHolder)holder).userTv.setText(chatsModel.getMessage());
                break;
            //if the sender is the bot, switch the view to the bot message view, botMsgTv
            case "bot":
                ((BotViewHolder)holder).botMsgTV.setText(chatsModel.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        //this method returns the number of items on the chats array list
        return chatsModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //here we get the type of view, whether user or bot, we default to -1 if neither
        switch(chatsModelArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    //the user view holder
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userTv;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    //the bot view holder
    public static class BotViewHolder extends RecyclerView.ViewHolder{

        TextView botMsgTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
