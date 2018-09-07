package wayawaya.ww.chamaah;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by teddyogallo on 05/07/2018.
 */

public class ChatAdapterGroup extends BaseAdapter {

    private final List<ChatMessageGroup> chatMessages;
    private Activity context;

    public ChatAdapterGroup(Activity context, List<ChatMessageGroup> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessageGroup getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChatAdapterGroup.ViewHolder holder;
        ChatMessageGroup chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.group_list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChatAdapterGroup.ViewHolder) convertView.getTag();
        }
        String msgType=chatMessage.getMsgType();

        if(TextUtils.isEmpty(msgType)){

            msgType="NOT_IMAGE";
        }
        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check
        //to simulate whether it me or other sender

        long sender_id=  chatMessage.getId();
        String sender_id_string=""+sender_id;
        setAlignment(holder, myMsg,msgType);
        holder.txtMessage.setText(chatMessage.getMessage());
        holder.txtInfo.setText(chatMessage.getDate());
        holder.textTitle.setText(sender_id_string);

        return convertView;
    }

    public void add(ChatMessageGroup message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessageGroup> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ChatAdapterGroup.ViewHolder holder, boolean isMe, String msgType) {
        if (!isMe) {

            if(msgType.equals("IMAGE_SEND")){



                holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);

                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();


                float factor = holder.contentWithBG.getContext().getResources().getDisplayMetrics().density;
                layoutParams.width = (int)(140 * factor);
                layoutParams.height = (int)(100 * factor);

                layoutParams.gravity = Gravity.LEFT;
                holder.contentWithBG.setLayoutParams(layoutParams);



                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtInfo.setLayoutParams(layoutParams);


                layoutParams = (LinearLayout.LayoutParams) holder.textTitle.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.textTitle.setLayoutParams(layoutParams);


//end of this is not an image



            }else {
                //start of this is not an image type of message
                holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.contentWithBG.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtInfo.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.textTitle.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.textTitle.setLayoutParams(layoutParams);

//end of this is not an image
            }
        } else {

            if(msgType.equals("IMAGE_SEND")){
                //start of this is an image type of message

                holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);

                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();


                float factor = holder.contentWithBG.getContext().getResources().getDisplayMetrics().density;
                layoutParams.width = (int)(140 * factor);
                layoutParams.height = (int)(100 * factor);

                layoutParams.gravity = Gravity.RIGHT;
                holder.contentWithBG.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtInfo.setLayoutParams(layoutParams);


                layoutParams = (LinearLayout.LayoutParams) holder.textTitle.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.textTitle.setLayoutParams(layoutParams);

               // holder.textTitle.setText("");



                //end of this is an image type of message
            }else {
                holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.contentWithBG.setLayoutParams(layoutParams);

                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.content.setLayoutParams(lp);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtInfo.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.textTitle.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.textTitle.setLayoutParams(layoutParams);

                //holder.textTitle.setText("");


                //end of this is not an image type of message
            }
        }
    }

    private ChatAdapterGroup.ViewHolder createViewHolder(View v) {
        ChatAdapterGroup.ViewHolder holder = new ChatAdapterGroup.ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);

        holder.textTitle = (TextView) v.findViewById(R.id.textTitle);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;

        public TextView textTitle;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }

}
