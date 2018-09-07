package wayawaya.ww.chamaah;




import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by teddyogallo on 10/06/2018.
 */

public class ChatAdapter extends BaseAdapter {

    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private final List<ChatMessage> chatMessages;
    private Activity context;

    String msgdata="";

    String imageData="";

    String file_Id="";

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages) {
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
    public ChatMessage getItem(int position) {
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
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String msgType=chatMessage.getMsgType();

        msgdata=chatMessage.getMessage();

        imageData=chatMessage.getImageData();

        if(TextUtils.isEmpty(msgType)){

            msgType="NOT_IMAGE";
        }
        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check
        //to simulate whether it me or other sender
        setAlignment(holder, myMsg,msgType);
        holder.txtMessage.setText(chatMessage.getMessage());
        holder.txtInfo.setText(chatMessage.getDate());

        return convertView;
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe,String msgType) {
        if (!isMe) {

            if(msgType.equals("IMAGE_SEND")){

                //file image name

               Bitmap bitmap2=getThumbnail(msgdata);



                //download image here



                if (bitmap2== null ) {
                    // myBitmap is empty/blank
                    //holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);
                    holder.messageImage.setImageResource(R.drawable.no_image_available);

                    //try to download image then update

                    new DownloadImagesTask().execute(imageData,msgdata);

                }else {

                    holder.messageImage.setImageBitmap(bitmap2);

                    //holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);

                }

                    holder.contentWithBG.setBackgroundResource(R.drawable.money_background_hollow_end);

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

               /*
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);

                */


                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.height = 1;
                layoutParams.width = 1;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtInfo.setLayoutParams(layoutParams);



                float factor2 = holder.contentWithBG.getContext().getResources().getDisplayMetrics().density;
                layoutParams.width = (int)(140 * factor2);
                layoutParams.height = (int)(100 * factor2);

                layoutParams.gravity = Gravity.CENTER;

                holder.messageImage.setLayoutParams(layoutParams);





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

//end of this is not an image
            }
        } else {

            if(msgType.equals("IMAGE_SEND")){
             //start of this is an image type of message


                Bitmap bitmap2=getThumbnail(msgdata);



                if (bitmap2== null ) {
                    // myBitmap is empty/blank
                    //holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);
                    holder.messageImage.setImageResource(R.drawable.no_image_available);

                }else {

                    holder.messageImage.setImageBitmap(bitmap2);

                    //holder.contentWithBG.setBackgroundResource(R.drawable.image_msg_back);

                }

                holder.contentWithBG.setBackgroundResource(R.drawable.money_in_background_hollow);

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

                 /*
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);

                */


                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                layoutParams.height = 1;
                layoutParams.width = 1;
                holder.txtMessage.setLayoutParams(layoutParams);

                layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtInfo.setLayoutParams(layoutParams);



                float factor2 = holder.contentWithBG.getContext().getResources().getDisplayMetrics().density;
                layoutParams.width = (int)(140 * factor2);
                layoutParams.height = (int)(100 * factor2);

                layoutParams.gravity = Gravity.CENTER;
                holder.messageImage.setLayoutParams(layoutParams);



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

                //end of this is not an image type of message
            }
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);

        holder.messageImage = (ImageView) v.findViewById(R.id.message_image);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;

        public ImageView  messageImage;
    }


    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            String url_img = (String) params[0];
            String uid = (String) params[1];

            file_Id=uid;

            Log.e("URL:Image request",url_img);
            return getBitmapFromURL(url_img);
        }

        @Override
        protected void onPostExecute(Bitmap result) {


            //save bitmap here
            boolean savestatus = saveImageToInternalStorage(result, file_Id);
/*  if(savestatus)
            {


                ViewHolder holder;
                holder.messageImage.setImageBitmap(result);
                Drawable newdrawable;

                newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(result,  90, 90, true));

                getSupportActionBar().setHomeAsUpIndicator(newdrawable); //you can set here image

            }

*/


            Log.v("Save Status:", Boolean.toString(savestatus));
        }

//end of download image async task
    }


    //save image in external storage method
    public boolean saveImageToExternalStorage(Bitmap image, String filename) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, filename+"dp.png");
            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    //save iamge in internal storage method

    //save image to internal storage
    public boolean saveImageToInternalStorage(Bitmap image,String filename) {

        try {
// Use the compress method on the Bitmap object to write image to
// the OutputStream
            FileOutputStream fos = context.openFileOutput(filename+"dp.png", Context.MODE_PRIVATE);

// Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }
//end of method to save image in internal storage


//method for reading from external storage

    public Bitmap getThumbnail(String filename) {

        filename=filename+"dp.png";

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
        Bitmap thumbnail = null;

// Look for the file on the external storage

     /*
        try {
            if (this.isSdReadable() == true) {
                thumbnail = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("img on external storage", e.getMessage());
        }
*/
        thumbnail=null;
// If no file on external storage, look in internal storage
        if (thumbnail == null) {
            try {
                File filePath = context.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("img on internal storage", ex.getMessage());
            }
        }
        return thumbnail;
    }


    //end of method for reading from external storage

//method for checking that external storage is avaialble

    public boolean isSdReadable() {

        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
// We can read and write the media
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
// We can only read the media
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
// Something else is wrong. It may be one of many other
// states, but all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }


    //end of method for checking external storage is available






//download bitmap from URL HERE

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            Log.e("Get Bitmap URL:",src);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //End of Download Bitmap from URL here

    //method to resize bitmap

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    //end of method to resize bitmap


}