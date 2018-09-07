package wayawaya.ww.chamaah;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by teddyogallo on 08/06/2018.
 */

public class messageContentAdapter extends RecyclerView.Adapter  {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    private List<messagechat_summary> contentList;

    private Context context;


    @Override
    public int getItemViewType(int position) {
        messagechat_summary message = contentList.get(position);


        if (message.getTitle().equals("Teddy Ogallo")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public SentMessageHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.text_message_body);
            timeText = (TextView) view.findViewById(R.id.text_message_time);



        }



        void bind(messagechat_summary message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getYear());
        }

    }


    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        public ReceivedMessageHolder(View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.text_message_name);
            messageText = (TextView) view.findViewById(R.id.text_message_body);
            timeText = (TextView) view.findViewById(R.id.text_message_time);

            profileImage = view.findViewById(R.id.image_message_profile);



        }

        void bind(messagechat_summary message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getYear());

            nameText.setText(message.getTitle());

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }



    public messageContentAdapter(List<messagechat_summary> contentsList) {
        this.contentList = contentsList;
    }


    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        messagechat_summary chat_content = contentList.get(position);



        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chat_content);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chat_content);
        }

      /*
        holder.title.setText(chat_content.getTitle());
        holder.content.setText(chat_content.getContent());
        holder.year.setText(chat_content.getYear());
*/

        //for image
/*
        Glide.with(context)
                .load(chat_content.getThumbnail())
                .into(holder.thumbnail);

                */
        //end for image
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }


    /*
    public void removeItem(int position) {
        contentList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(chat_summary item, int position) {
        contentList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


*/

}



  /**  Old Code here

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<messagechat_summary> contentList;

    private Context mContext;


    public messageContentAdapter(Context context, List<messagechat_summary> contentsList) {

        mContext = context;
        contentList = contentsList;
    }



    @Override
    public int getItemCount() {
        return contentList.size();
    }


    @Override
    public int getItemViewType(int position) {
        messagechat_summary chat_content = contentList.get(position);



        if (chat_content.getTitle().equals("Teddy Ogallo")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        messagechat_summary chat_content = contentList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chat_content);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chat_content);
        }


        //end for image
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(messagechat_summary message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText((message.getYear()));
        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(messagechat_summary message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getYear());

            nameText.setText(message.getTitle());

            // Insert the profile image from the URL into the ImageView.
           // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }





}  **/