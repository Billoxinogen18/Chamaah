package wayawaya.ww.chamaah;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 10/06/2018.
 */

public class groupslist  extends AppCompatActivity{

    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private List<chat_summary> contentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContentAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    String useridGlob="";

    String usernameGlob="";

    String active_group="";

    private int group_id_glob;

    RelativeLayout notificationCount1;




    private boolean loadingData = false;


    final Context context = this;


    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;



    String currencyGlob="";

    String conversationidGlob="";

    int GET_FROM_GALLERY=1;

    public static final String MyPREFERENCES = "ww_prefs";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplist);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        notificationCount1 = (RelativeLayout) findViewById(R.id.notification_layout);

        getSupportActionBar().setTitle("Groups"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar




        //get Userid here



        //get saved values in preference file

        SharedPreferences mySharedPreferences ;
        mySharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Retrieve the saved values.

        String usernamepref = mySharedPreferences.getString("usernamepref", "");
        String usercurrencypref = mySharedPreferences.getString("usercurrencypref", "");

        String userlogintypepref = mySharedPreferences.getString("userlogintypepref", "");
        String usersocialidpref = mySharedPreferences.getString("usersocialidpref", "");
        String usersocialpref = mySharedPreferences.getString("usersocialimagepref", "");


        usernameGlob=usernamepref;
        useridGlob=usernamepref;
        currencyGlob=usercurrencypref;




        contentList=new ArrayList<>();
        mAdapter = new ContentAdapter(contentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareContentData();


        new SendPostRequest().execute(usernameGlob,useridGlob);



        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));


// set the adapter
        recyclerView.setAdapter(mAdapter);

        //on touch adapter connecting recyclerTouchListener.java

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                chat_summary content_chat = contentList.get(position);
                //Toast.makeText(getApplicationContext(), content_chat.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                conversationidGlob=content_chat.getTitle() ;

                group_id_glob=content_chat.getId() ;




                Intent intent = new Intent(groupslist.this, groupChatActivity.class);



// 2. put key/value data


                intent.putExtra("conversationidmain",  conversationidGlob);

                intent.putExtra("groupidmain",  group_id_glob);



                // 3. or you can add data to a bundle
                Bundle extras = new Bundle();
                extras.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent.putExtras(extras);

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //end of On Touch

//endd of oncreate
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_list_top_menu, menu);

        MenuItem item1 = menu.findItem(R.id.actionbar_item);



       // MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(groupslist.this, MainActivitySearch.class);



                startActivity(intent);


                break;

            case R.id.actionbar_item:




                Intent intent3 = new Intent(groupslist.this, new_group.class);



// 2. put key/value data


                intent3.putExtra("conversationidmain",  conversationidGlob);



                // 3. or you can add data to a bundle
                Bundle extras1 = new Bundle();
                extras1.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent3.putExtras(extras1);

                startActivity(intent3);


                break;







        }
        return true;
    }



    private void prepareContentData() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());





            chat_summary content = new chat_summary("Chamaah Support", "Welcome  ", currentDateandTime,10000001, "null", 2);
            contentList.add(content);





        mAdapter.notifyDataSetChanged();
    }



    //function to load data remotely


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String token = (String)params[1];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_groups_list.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("logintoken", token);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();

                    Log.e("sbtostring",sb.toString());


                    return sb.toString();

                }
                else {

                    //Log.e("HTTP_CODE_ERROR",responseCode);

                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {


            Log.e("onpostexecuteresults",result);

            getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }




    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }




    public ArrayList<chat_summary> getInfo(String response) {
        ArrayList<chat_summary> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {



                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);

                    String group_title = dataobj.getString("group_name");

                    int group_id_this=dataobj.getInt("group_id");

                    String group_description = dataobj.getString("group_description");

                    String group_time = dataobj.getString("date_created");

                    String group_image = dataobj.getString("group_image");

                    String group_image_url="https://www.wayawaya.co.ke/chamah_front/" + group_image;

                    chat_summary content = new chat_summary(group_title, group_description, group_time,group_id_this, group_image_url, 2);
                    contentList.add(content);

                  String save_id=""+group_id_this;

                    //saveArrayList(contentList,save_id);

                    //update from online sources
                    // chatHistory =getInfo(useridGlob);


                    Bitmap bitmap2= getThumbnail(save_id);

                    if (bitmap2== null ) {
                        // myBitmap is empty/blank
                        if (!TextUtils.isEmpty(group_image)) {

                            String imageurl = "https://www.wayawaya.co.ke/chamah_front/" + group_image;

                            new DownloadImagesTask().execute(imageurl,save_id);

                        }


//end of bitmap is null
                    }


                   // playersModelArrayList.addAll(chatRefreshHistory);




                    //clear the Chat array here
                }


                mAdapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playersModelArrayList;
    }



    ///function to load data remotely


    public void saveArrayList(List<chat_summary> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<chat_summary> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
    }





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

            boolean savestatus=false;


          if(result!=null)
            {
                savestatus = saveImageToInternalStorage(result, active_group);

            }





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
