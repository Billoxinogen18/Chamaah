package wayawaya.ww.chamaah;

/**
 * Created by teddyogallo on 06/06/2018.
 */


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.widget.RecyclerView;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import com.bumptech.glide.Glide;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {

    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    int GET_FROM_GALLERY=1;

    String active_group="";

    private List<chat_summary> contentList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, content;

        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            year = (TextView) view.findViewById(R.id.year);

            thumbnail = view.findViewById(R.id.chatimage);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public ContentAdapter(List<chat_summary> contentsList) {
        this.contentList = contentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        chat_summary chat_content = contentList.get(position);
        holder.title.setText(chat_content.getTitle());
        holder.content.setText(chat_content.getContent());
        holder.year.setText(chat_content.getYear());

        int groupid=chat_content.getId();

        Log.e("Group ID Got",""+groupid);


        String groupid_got=""+groupid;

        Log.e("Group ID String",groupid_got);
        Bitmap bitmap2= getThumbnail(groupid_got);



        if (bitmap2== null) {
            // myBitmap is empty/blank

            holder.thumbnail.setImageResource(R.drawable.user_placeholder);;

            Log.e("Bitmap Status","Bit map is null");


        }else {


            holder.thumbnail.setImageBitmap(bitmap2);

            Log.e("Bitmap Status","Bit map is not null");

        }

                //load image from file


        //for image
/*
        Glide.with(context)
                .load(chat_content.getThumbnail())  https://www.wayawaya.co.ke/app_serve/5b7c164c8dd0a.png
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




    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            String url_img = (String) params[0];

            String group_id= (String) params[1];

            active_group=group_id;

            Log.e("URL:Image request",url_img);
            return getBitmapFromURL(url_img);
        }

        @Override
        protected void onPostExecute(Bitmap result) {


            //save bitmap here
            boolean savestatus = saveImageToInternalStorage(result, active_group);

          /*  if(savestatus)
            {
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