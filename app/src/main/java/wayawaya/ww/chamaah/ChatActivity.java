package wayawaya.ww.chamaah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by teddyogallo on 10/06/2018.
 */

public class ChatActivity extends AppCompatActivity {


    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    private ArrayList<ChatMessage> chatInboxHistory;

    private ArrayList<ChatMessage> chatRefreshHistory;
    private ArrayList<ChatMessage> chatMoneysentHistory;

    private ArrayList<chat_summary> chatGlobalHistory;

    private ArrayList<ChatMessage> chatPayRequestHistory;

    private String payment_amount = "";

    private String card_account_id="";

    private String card_account_no="";

    private String mpesa_account_no="";

    private String message_id="";

    Button extra_menu_button;

    RelativeLayout notificationCount1;

    TextView  friend_label,Self_label;


    String useridGlob="";

    int useridGlob_num=0;

    String usernameGlob="";

    String chatidGlob="Friend";

    String strSeperator="|#|";

    String strSeperatorMain="@%@";


    private ProgressBar pb;
    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;


    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;



    String currencyGlob="";

    String conversationidGlob="";

     String profileurlGlob="";

    String conversationnameGlob="";

    int GET_FROM_GALLERY=1;

    public static final String MyPREFERENCES = "ww_prefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



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


        try {
            useridGlob_num = Integer.parseInt(useridGlob);
        } catch(NumberFormatException nfe) {
            // Handle parse error.

            Log.e("Userid to Int Error", String.valueOf(nfe));

        }



        extra_menu_button=(Button) findViewById(R.id.side_menu);;
        notificationCount1 = (RelativeLayout) findViewById(R.id.notification_layout);

        friend_label=(TextView) findViewById(R.id.friendLabel);

        Intent intent = getIntent();


        // 2. get message value from intent

        String conversationididgot = intent.getStringExtra("conversationidmain");

        String conversationnamegot = intent.getStringExtra("conversationnamemain");

        String profileurlgot = intent.getStringExtra("profileurlmain");


        profileurlGlob=profileurlgot;

        conversationidGlob=conversationididgot;

        conversationnameGlob=conversationnamegot;



        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        if(!TextUtils.isEmpty(conversationidGlob)) {

            chatidGlob=conversationidGlob;
        }

        //load the History here
        initControls();

        //end of load the history here






      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable(R.drawable.user_placeholder_round) );


        //getSupportActionBar().setIcon(R.drawable.user_placeholder_round);

        getSupportActionBar().setTitle(chatidGlob); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));

        */
/*
      Drawable drawable= getResources().getDrawable(R.drawable.user_placeholder_round);
      Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
      Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,  90, 90, true));
      newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

       */

        //getSupportActionBar().setHomeAsUpIndicator(newdrawable);



        String groupid_got=""+chatidGlob;
        Bitmap bitmap2= getThumbnail(groupid_got);


        Drawable drawablet= getResources().getDrawable(R.drawable.icon_87);
        Bitmap bitmap_t = ((BitmapDrawable) drawablet).getBitmap();

        Bitmap emptyBitmap = Bitmap.createBitmap(bitmap_t.getWidth(), bitmap_t.getHeight(), bitmap_t.getConfig());

        Drawable newdrawable;


        if (bitmap2== null || bitmap2.sameAs(emptyBitmap)) {
            // myBitmap is empty/blank

            Bitmap bitmap;

            if(!TextUtils.isEmpty(profileurlGlob)) {

               new DownloadImagesTask().execute(profileurlGlob);

               //load image from file

                String groupid_g=""+chatidGlob;
                bitmap= getThumbnail(groupid_g);

                if (bitmap== null){

                    Drawable drawable= getResources().getDrawable(R.drawable.user_placeholder_round);
                    bitmap = ((BitmapDrawable) drawable).getBitmap();

                }else {
                 //save bitmap here
                    boolean savestatus=saveImageToInternalStorage(bitmap,chatidGlob);

                }

            }else {

                Drawable drawable= getResources().getDrawable(R.drawable.user_placeholder_round);
                bitmap = ((BitmapDrawable) drawable).getBitmap();

            }




            newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,  90, 90, true));



        }else {

            Bitmap bmp2 = bitmap2.copy(bitmap2.getConfig(), true);


            newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bmp2,  90, 90, true));



        }





        TextView actionBartitle;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(newdrawable); //you can set here image
        getSupportActionBar().setIcon(null);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.color.colorPrimary));
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_layout, null);// custom layout
        actionBartitle = (TextView) v.findViewById(R.id.usertitle);


        getSupportActionBar().setCustomView(v);


        if(!TextUtils.isEmpty(conversationnameGlob)) {

            friend_label.setText(conversationnameGlob);

            actionBartitle.setText(conversationnameGlob);

        }else {


            friend_label.setText(chatidGlob);

            actionBartitle.setText(chatidGlob);

        }


        findViewById(R.id.side_menu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(ChatActivity.this, extra_menu_button);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.message_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(ChatActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


                                if(item.getTitle().equals("Send Payment")){

                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompts, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    // set prompts.xml to alertdialog builder
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText) promptsView
                                            .findViewById(R.id.amount_input);

                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            // get user input and set it to result
                                                            // edit text



                                                            payment_amount=userInput.getText().toString();

                                                            String messageText="KES "+payment_amount+" Sent";

                                                            String message_type="payment_sent";

                                                            new SendMoneyRequest().execute(usernameGlob,chatidGlob,payment_amount,message_type);




                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();






                                }else if(item.getTitle().equals("Request Payment")){

                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompt_payrequest, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    // set prompts.xml to alertdialog builder
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText) promptsView
                                            .findViewById(R.id.amount_input);

                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            // get user input and set it to result
                                                            // edit text

                                                            chatPayRequestHistory = new ArrayList<ChatMessage>();

                                                            chatPayRequestHistory=getArrayList(chatidGlob);


                                                            payment_amount=userInput.getText().toString();

                                                            String messageText="Request of KES "+payment_amount+" Sent";

                                                            String message_type="payment_request";

                                                            new SendMessageRequest().execute(usernameGlob,chatidGlob,messageText,message_type);

                                                            //go to complete bill payment
                                                            ChatMessage chatMessage = new ChatMessage();
                                                            chatMessage.setId(useridGlob_num);//dummy
                                                            chatMessage.setMessage(messageText);
                                                            chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                                                            chatMessage.setMe(true);

                                                            chatPayRequestHistory.add(chatMessage);

                                                            saveArrayList(chatPayRequestHistory,chatidGlob);

                                                            chatPayRequestHistory.clear();

                                                            messageET.setText("");

                                                            displayMessage(chatMessage);
                                                            //end of normal processing


                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();


                                }else  if(item.getTitle().equals("Send Image/Video")){

                                    //Toast.makeText(ChatActivity.this,"You Selected : " + item.getTitle()+" which is unsupported on Offline mode ",Toast.LENGTH_SHORT).show();

                                    checkPermission();



                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu


                    }
                });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                //profile_image.setImageBitmap(bitmap);

                chatPayRequestHistory = new ArrayList<ChatMessage>();

                chatPayRequestHistory=getArrayList(chatidGlob);


               // payment_amount=userInput.getText().toString();

                //generate unique file name


                String uuid = UUID.randomUUID().toString();

                String image_file_name=uuid+"pi";



                boolean savestatus=saveImageToInternalStorage(bitmap,image_file_name);

                String messageText=image_file_name;

                String message_type="IMAGE_SEND";

                //convert image to string here


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                byte[] byteArray = baos.toByteArray();

                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //end of convert Image to string


                new SendImageRequest().execute(useridGlob,usernameGlob,chatidGlob,encodedImage,message_type,encodedImage);



                //go to complete bill payment
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(useridGlob_num);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setMsgType(message_type);
                chatMessage.setImageData(encodedImage);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                chatPayRequestHistory.add(chatMessage);

                saveArrayList(chatPayRequestHistory,chatidGlob);

                chatPayRequestHistory.clear();

                messageET.setText("");

                displayMessage(chatMessage);


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        companionLabel.setText(chatidGlob);// Hard Coded
        loadDummyHistory();

        new SendPostRequest().execute(usernameGlob,useridGlob);



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                chatInboxHistory = new ArrayList<ChatMessage>();

                chatInboxHistory=getArrayList(chatidGlob);

                if (chatInboxHistory != null && !chatInboxHistory.isEmpty()) {

                    String messageText = messageET.getText().toString();
                    String message_type="text";
                    if (TextUtils.isEmpty(messageText)) {
                        return;
                    }




                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(useridGlob_num);//dummy
                    chatMessage.setMessage(messageText);
                    chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    chatMessage.setMe(true);


                    chatInboxHistory.add(chatMessage);

                    saveArrayList(chatInboxHistory, chatidGlob);

                    chatInboxHistory.clear();

                    messageET.setText("");

                    displayMessage(chatMessage);

                    new SendMessageRequest().execute(usernameGlob,chatidGlob,messageText,message_type);

                    new SendPostRequest().execute(usernameGlob, useridGlob);

                }else {

                    String messageText = messageET.getText().toString();
                    String message_type="text";
                    if (TextUtils.isEmpty(messageText)) {
                        return;
                    }


                    chatInboxHistory = new ArrayList<ChatMessage>();

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(useridGlob_num);//dummy
                    chatMessage.setMessage(messageText);
                    chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    chatMessage.setMe(true);


                    chatInboxHistory.add(chatMessage);

                    saveArrayList(chatInboxHistory, chatidGlob);

                    chatInboxHistory.clear();

                    messageET.setText("");

                    displayMessage(chatMessage);

                    new SendMessageRequest().execute(usernameGlob,chatidGlob,messageText,message_type);

                    new SendPostRequest().execute(usernameGlob, useridGlob);


                }
            }
        });
    }

    public void displayMessage(ChatMessage message) {

        if (message != null ) {
            adapter.add(message);
            adapter.notifyDataSetChanged();
            scroll();

        }
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }




    private void loadDummyHistory() {

        chatHistory = new ArrayList<ChatMessage>();


        //update from online sources
        // chatHistory =getInfo(useridGlob);

        chatHistory = getArrayList(chatidGlob);

        //update from online sources
       // chatHistory =getInfo(useridGlob);

        if (chatHistory != null && !chatHistory.isEmpty()) {



            //update from online sources
            // chatHistory =getInfo(useridGlob);

/*
            ChatMessage msg = new ChatMessage();
            msg.setId(1);
            msg.setMe(false);
            msg.setMessage("Hi");
            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg);




            saveArrayList(chatHistory,chatidGlob);

*/

            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
            messagesContainer.setAdapter(adapter);

            for(int i=0; i<chatHistory.size(); i++) {
                ChatMessage message = chatHistory.get(i);


                displayMessage(message);
            }

            chatHistory.clear();
    }else {
       //chat history is null


//prepare for filling up of values

            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
            messagesContainer.setAdapter(adapter);


           /*
            chatHistory = new ArrayList<ChatMessage>();

            ChatMessage msg = new ChatMessage();
            msg.setId(1);
            msg.setMe(false);
            msg.setMessage("Hi");
            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg);
            ChatMessage msg1 = new ChatMessage();
            msg1.setId(2);
            msg1.setMe(false);
            msg1.setMessage("How r u doing???");
            msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg1);



            saveArrayList(chatHistory,chatidGlob);



            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
            messagesContainer.setAdapter(adapter);

            for(int i=0; i<chatHistory.size(); i++) {
                ChatMessage message = chatHistory.get(i);


                displayMessage(message);
            }

            chatHistory.clear();

            */


        }





    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.messages_top_menu, menu);

        //MenuItem item1 = menu.findItem(R.id.actionbar_item);



        //MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        //notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(ChatActivity.this, MainActivitySearch.class);



                startActivity(intent);


                break;
        }
        return true;
    }



    /**
     *     Save and get ArrayList in SharedPreference
     */

    public void saveArrayList(ArrayList<ChatMessage> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<ChatMessage> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<ChatMessage>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public void saveGlobalArrayList(List<chat_summary> list){

        String key="chmah";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<chat_summary> getGlobalArrayList(){

        String key="chmah";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public void setUpdateItem(ArrayList<chat_summary> dataEntity, chat_summary item) {
        int itemIndex = dataEntity.indexOf(item);
        if (itemIndex != -1) {
            dataEntity.set(itemIndex, item);

            saveGlobalArrayList(chatGlobalHistory);

            chatGlobalHistory.clear();
        }
    }


   public String update_global(String usertitle, String content_this, int notification_no, String year_this, String profile_thumbnail){



       //chatGlobalHistory = new ArrayList<chat_summary>();

       List<chat_summary> contentList = new ArrayList<>();


       //update from online sources
       // chatHistory =getInfo(useridGlob);

      // chatGlobalHistory = getGlobalArrayList();

       contentList = getGlobalArrayList();





       //update from online sources
       // chatHistory =getInfo(useridGlob);

       if (contentList != null && !contentList.isEmpty()) {


           for(int i=0; i<contentList.size(); i++) {
               chat_summary message = contentList.get(i);

 String titlethis=message.getTitle();

               int index_this = i;

               if (titlethis.equals(usertitle) ) {


                   int id_this=message.getId();

                   String message_this=message.getContent();
                   String Date_this=message.getYear();
                   String profile_url_this=message.getThumbnail();
                   long notifications_num=message.getnotificationsNum();

                   //create new values here

                   chat_summary content = new chat_summary(usertitle, content_this, year_this,id_this, profile_thumbnail, notification_no);
                   //contentList.add(content);

                   contentList.set( index_this, content );

                   saveGlobalArrayList(contentList);

                   contentList.clear();



               }



           }

/*


           for (chat_summary row : chatGlobalHistory) {
               String userIdThis=row.getTitle();

               int index_this = chatGlobalHistory.indexOf(row);

               if (userIdThis.equals(usertitle) ) {
                   //read contents here
                   int id_this=row.getId();

                   String message_this=row.getContent();
                   String Date_this=row.getYear();
                   String profile_url_this=row.getThumbnail();
                   long notifications_num=row.getnotificationsNum();

                   //create new values here

                   chat_summary content = new chat_summary(usertitle, content_this, year_this,id_this, profile_thumbnail, notification_no);
                   //contentList.add(content);

                   chatGlobalHistory.set( index_this, content );

                   saveGlobalArrayList(chatGlobalHistory);

                   chatGlobalHistory.clear();

               }
           }

*/

       }else {
           //chat history is  null


            chatGlobalHistory = new ArrayList<chat_summary>();

            int id_random=123;


                    chat_summary content = new chat_summary(usertitle, content_this, year_this,id_random, profile_thumbnail, notification_no);
                    //contentList.add(content);

                    chatGlobalHistory.add(  content );

                    saveGlobalArrayList(chatGlobalHistory);



            chatGlobalHistory.clear();



       }




return  null;


   }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String token = (String)params[1];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_notifications.php"); // here is your URL path

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




    public class SendMessageRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String recipient_id= (String)params[1];
            String message= (String)params[2];
            String message_type= (String)params[3];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("recipientid", recipient_id);
                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);

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


            Log.e("onmessagesentresults",result);

           // getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }


    public class SendImageRequest_2 extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String recipient_id= (String)params[1];
            String message= (String)params[2];
            String message_type= (String)params[3];
            String image_string= (String)params[4];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_image_messages.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("recipientid", recipient_id);
                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);
                postDataParams.put("image_string",image_string);

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


            Log.e("onmessagesentresults",result);

            // getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }



    private class SendImageRequest extends AsyncTask<String, Integer, String> {

        String result = "";
        String request="";
        String RphoneGlob="";
        String RamountGlob="";
        String RcountryGlob="";

        ProgressDialog Asycdialog = new ProgressDialog(ChatActivity.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Asycdialog.setMessage("Uploading...");
            //Asycdialog.show();
        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            loadingData = true;

            String Ruserid = (String)params[0];
            String Rtoken = (String)params[1];
            String recipient_id= (String)params[2];
            String Rimagepayload = (String)params[3];
            String message_type = (String)params[4];
            String messages = (String)params[5];










            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);




            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://10.0.2.2/chotu/index.php");
            HttpPost httppost = new HttpPost("https://www.wayawaya.co.ke/chamah_front/c_image_messages.php");
            try {

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("userid", Ruserid));
                nameValuePairs.add(new BasicNameValuePair("token", Rtoken));
                nameValuePairs.add(new BasicNameValuePair("recipientid", recipient_id));
                nameValuePairs.add(new BasicNameValuePair("image_payload", Rimagepayload));
                nameValuePairs.add(new BasicNameValuePair("message_type", message_type));
                nameValuePairs.add(new BasicNameValuePair("messages", messages));




                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                result = EntityUtils.toString(response.getEntity());

                request=EntityUtils.toString(httppost.getEntity());

                Log.d("TAG","From server:"+result);

                Log.d("TAG","To Server :"+request);

                // Log.v("tag","Request:" + request);
            }
            catch (ClientProtocolException e)
            {
                // TODO Auto-generated catch block
                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ChatActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Image Upload Failed");

                // Setting Dialog Message
                alertDialog.setMessage(" Request was not completed ");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            } catch (IOException e)
            {

                //show alert dialog
                error=true;

            }

            return result;
            //postData(params[0]);
            // return null;
        }
        protected void onPostExecute(String result){
            //pb.setVisibility(View.GONE);

            //Asycdialog.dismiss();

            loadingData = false;

            //PARSE JSON RESPONSE

            if(error==true){

                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ChatActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Request Failed");

                // Setting Dialog Message
                alertDialog.setMessage("There was a problem connecting to the server");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "Continue", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                //Toast.makeText(this, "please enter a password", Toast.LENGTH_LONG).show();


            }else{




                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);


                    String successStatus = jsonObject.getString("status");

                    if(successStatus.equals("1")){
                        //the request was successfull
                        //switch view to dashboard

                        String confirmation = jsonObject.getString("message");
                        String messageid = jsonObject.getString("message_id");




                        //((mimiappmain) this.getApplication()).setTransactionid(transactionid);








                    }


                    else{
                        //get error code
                        String errorStatus = jsonObject.getString("message");


                        String messageid = jsonObject.getString("message_id");

                        //Toast.makeText(getApplicationContext(), "Login Error:"+errorStatus+"userid:"+useridthis+"password:"+passwordthis+"token:"+tokenthis+"deviceid:"+deviceidthis, Toast.LENGTH_LONG).show();

                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                ChatActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Image Upload Failed");

                        // Setting Dialog Message
                        alertDialog.setMessage(" "+errorStatus+" ");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                    }



                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


        }







    }





    public class SendMoneyRequest extends AsyncTask<String, Void, String> {

       String amount_this="";

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String recipient_id= (String)params[1];
            String message= (String)params[2];
            String message_type= (String)params[3];

            amount_this=message;

            payment_amount=message;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages_money.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("recipientid", recipient_id);
                postDataParams.put("amount", message);
                postDataParams.put("message_type", message_type);

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


            Log.e("onmessagesentresults",result);

            getInfoMoney(result,amount_this);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }




    public class requestMoneyRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String recipient_id= (String)params[1];
            String message= (String)params[2];
            String message_type= (String)params[3];

            payment_amount=message;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("recipientid", recipient_id);
                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);

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


            Log.e("onmessagesentresults",result);

            // getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }






    public class confirmCardPaymentRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String cvv_this= (String)params[1];
            String card_id= (String)params[2];
            String message_id= (String)params[3];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_card_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("cvv", cvv_this);
                postDataParams.put("cardid", card_id);
                postDataParams.put("message_id", message_id);

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


            Log.e("onmessagesentresults",result);

             getInfoCardPay(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }



    public class confirmMpesaPaymentRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String mpesa_id= (String)params[1];

            String message_id= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_mpesa_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mpesa_id", mpesa_id);

                postDataParams.put("message_id", message_id);

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


            Log.e("onmessagesentresults",result);

            getInfoCardPay(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }



    public class confirmWalletPaymentRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String mpesa_id= (String)params[1];

            String message_id= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_wallet_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mobile_pin", mpesa_id);

                postDataParams.put("message_id", message_id);

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


            Log.e("onmessagesentresults",result);

            getInfoCardPay(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }


    public class addMpesaRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String mpesa_no= (String)params[1];

            String message_id= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/add_mpesa_request.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mpesa_no", mpesa_no);

                postDataParams.put("message_id", message_id);

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


            Log.e("onmessagesentresults",result);

            getInfoAddMpesa(result);

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


    public ArrayList<ChatMessage> getInfo(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {

                chatRefreshHistory = new ArrayList<ChatMessage>();

                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {



                    chatRefreshHistory = new ArrayList<ChatMessage>();


                    //update from online sources
                    // chatHistory =getInfo(useridGlob);

                    chatRefreshHistory = getArrayList(chatidGlob);

                    //update from online sources
                    // chatHistory =getInfo(useridGlob);

                    if (chatRefreshHistory != null && !chatRefreshHistory.isEmpty()) {


                        //update from online sources
                        // chatHistory =getInfo(useridGlob);

                        ChatMessage msg = new ChatMessage();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        String sender_name = dataobj.getString("name");

                        if (!sender_name.isEmpty()){

                            msg.setId(dataobj.getLong("id"));
                            msg.setMe(dataobj.getBoolean("identity"));
                            msg.setMessage(dataobj.getString("message"));
                            msg.setMsgType(dataobj.getString("msg_type"));
                            msg.setDate(dataobj.getString("datetime"));
                            msg.setImageData(dataobj.getString("file_address"));
                            chatRefreshHistory.add(msg);


                        saveArrayList(chatRefreshHistory, sender_name);



                            update_global(sender_name, dataobj.getString("message"),11, dataobj.getString("datetime"), "");
/*
                        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
                        messagesContainer.setAdapter(adapter);
*/


                        for (int t = 0; t < chatRefreshHistory.size(); t++) {
                            ChatMessage message = chatRefreshHistory.get(t);

                            if (chatidGlob.equals(sender_name)) {

                                displayMessage(message);
                            }



                        }
//end of response is not empty
                    }else {
                         //notification is not for this conversation


                                long message_id=(dataobj.getLong("id"));
                                boolean identity=(dataobj.getBoolean("identity"));
                                String messages_this=(dataobj.getString("message"));
                                String date_time_this=(dataobj.getString("datetime"));

                            Intent intent = new Intent(this, MainActivitySearch.class);
                            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                            // Build notification
                            // Actions are just fake
                            Notification noti = new Notification.Builder(this)
                                    .setContentTitle("Notification From " + sender_name)
                                    .setContentText(messages_this).setSmallIcon(R.drawable.ic_launcher)
                                    .setContentIntent(pIntent)
                                    .addAction(R.drawable.phone_icon_small, "Call", pIntent)
                                    .addAction(R.drawable.history_small_icon, "More", pIntent)
                                    .addAction(R.drawable.history_small_icon, "And more", pIntent).build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // hide the notification after its selected
                            noti.flags |= Notification.FLAG_AUTO_CANCEL;

                            notificationManager.notify(0, noti);

                         //end notification is not for this conversation
                        }

                 //end of previous chat history is not empty
                    }else {
                        //chat history is not null
                        chatRefreshHistory = new ArrayList<ChatMessage>();

                        ChatMessage msg = new ChatMessage();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        String sender_name=dataobj.getString("name");

                        if (!sender_name.isEmpty()) {

                            msg.setId(dataobj.getLong("id"));
                            msg.setMe(dataobj.getBoolean("identity"));
                            msg.setMessage(dataobj.getString("message"));

                            msg.setMsgType(dataobj.getString("msg_type"));
                            msg.setDate(dataobj.getString("datetime"));
                            msg.setImageData(dataobj.getString("file_address"));
                            chatRefreshHistory.add(msg);


                            saveArrayList(chatRefreshHistory, sender_name);

/*
                            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
                            messagesContainer.setAdapter(adapter);

*/

                            for (int t = 0; t < chatRefreshHistory.size(); t++) {
                                ChatMessage message = chatRefreshHistory.get(t);

                                if (chatidGlob.equals(sender_name)) {

                                    displayMessage(msg);
                                }


                            }


//end of response is not empty
                        }
//end of previous chat history is empty
                    }



                    playersModelArrayList.addAll(chatRefreshHistory);

                    chatRefreshHistory.clear();


                    //clear the Chat array here
                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playersModelArrayList;
    }



    public void getInfoAddMpesa(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String return_desc=jsonObject.getString("desc");
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ChatActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Request Succesful");

                // Setting Dialog Message
                alertDialog.setMessage("The Account was Added Succesfully");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();





            }else {


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ChatActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Failed Request");

                // Setting Dialog Message
                alertDialog.setMessage("Their Was an Error in Linking Account  Request :"+return_desc);

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void getInfoCardPay(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String success_desc=jsonObject.getString("DESC");

            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {



                chatMoneysentHistory = new ArrayList<ChatMessage>();

                chatMoneysentHistory=getArrayList(chatidGlob);




                String messageText="KES "+payment_amount+" Sent";

                String message_type="payment_sent";



                //go to complete bill payment
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(useridGlob_num);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                chatMoneysentHistory.add(chatMessage);

                saveArrayList(chatMoneysentHistory,chatidGlob);



                chatMoneysentHistory.clear();

                messageET.setText("");

                displayMessage(chatMessage);



            }else {
             //there was an error processing transaction


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ChatActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Failed Request");

                // Setting Dialog Message
                alertDialog.setMessage("Their Was an Error Processing Request :"+success_desc);

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getInfoMoney(String response,String amount) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {

                int success_code =jsonObject.getInt("code");

                message_id=jsonObject.getString("message_id");

                card_account_id=jsonObject.getString("card_account_id");


                mpesa_account_no=jsonObject.getString("mpesa_account_no");

                if(success_code==1){



                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Payment");

                    // Setting Dialog Message
                    alertDialog.setMessage("You can complete transaction by selecting your prefered account below Either Attached Primary Card or MPESA Account or Virtual Wallet");


                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_launcher);

                    // Setting OK Button
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Accounts", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed


                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.confirm_payment, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.cvv_number_text);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    // get user input and set it to result
                                                    // edit text


//process card payment here


                                                    String cvv_code=userInput.getText().toString();


                                                    new confirmCardPaymentRequest().execute(usernameGlob,cvv_code,card_account_id,message_id);


//end of process card payment here

                                                }
                                            })
                                    .setNegativeButton("M-PESA",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {


                                                    new confirmCardPaymentRequest().execute(usernameGlob,mpesa_account_no,message_id);



                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();






                            // Toast.makeText(getApplicationContext(), "Continuing ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Wallet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed

                           //ask for mobile PIN To COnfirm Wallet transaction




                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.prompt_mobilepin, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.password_text);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    // get user input and set it to result
                                                    // edit text



                                                    String mobile_pin=userInput.getText().toString();



                                                    new confirmWalletPaymentRequest().execute(usernameGlob,mobile_pin,message_id);





                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();










                            //end of ask for Mobile PIN to Confirm Wallet Balance

                        }
                    });


                    alertDialog.show();



                }else if(success_code==5){

                    //Ask User to Enter Mobile PIN To transact from Virtual Account

                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompt_mobilepin, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.password_text);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text



                                            String mobile_pin=userInput.getText().toString();



                                            new confirmWalletPaymentRequest().execute(usernameGlob,mobile_pin,message_id);







                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();






                }else if(success_code==6){

                    //Ask User to Enter CVV or Transacting pin to Pay Via Card or MPESA Since Virtual Wallet has insufficient Funds

                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.confirm_payment, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.cvv_number_text);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text


//process card payment here


                                            String cvv_code=userInput.getText().toString();


                                            new confirmCardPaymentRequest().execute(usernameGlob,cvv_code,card_account_id,message_id);


//end of process card payment here

                                        }
                                    })
                            .setNegativeButton("M-Pesa",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {




                                            new confirmMpesaPaymentRequest().execute(usernameGlob,mpesa_account_no,message_id);





                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }else if(success_code==7){

                    //Allow user to Pay via Card or MPESA


                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.confirm_payment, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.cvv_number_text);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

//process card payment here


                                            String cvv_code=userInput.getText().toString();


                                            new confirmCardPaymentRequest().execute(usernameGlob,cvv_code,card_account_id,message_id);


//end of process card payment here

                                        }
                                    })
                            .setNegativeButton("M-Pesa",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {





                                            new confirmMpesaPaymentRequest().execute(usernameGlob,mpesa_account_no,message_id);




                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();






                }










            }else if (jsonObject.getString(KEY_SUCCESS).equals("false")) {

                int Error_code =jsonObject.getInt("code");

                if(Error_code==0){

                    //COmplete Account Setup to COntinue

                    //show alert dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Payment Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please complete Account setup to continue");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_launcher);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                           // Toast.makeText(getApplicationContext(), "Fill in and Verify Account", Toast.LENGTH_SHORT).show();




                            // 1. create an intent pass class name or intnet action name
                            Intent intent = new Intent(ChatActivity.this, signupstart.class);

                            // 2. put key/value data


                            intent.putExtra("useridmain", useridGlob);






                            // 3. or you can add data to a bundle
                            Bundle extras = new Bundle();
                            extras.putString("status", "useridvariablereceived!");

                            // 4. add bundle to intent
                            intent.putExtras(extras);

                            // 5. start the activity
                            startActivity(intent);




                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();




                }else if(Error_code==2){

                    //setup account and link with social media

                    //show alert dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Payment Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please complete Facebook Linked Account setup to continue");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_launcher);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed



                            // 1. create an intent pass class name or intnet action name
                            Intent intent = new Intent(ChatActivity.this, signupstart.class);

                            // 2. put key/value data


                            intent.putExtra("useridmain", useridGlob);






                            // 3. or you can add data to a bundle
                            Bundle extras = new Bundle();
                            extras.putString("status", "useridvariablereceived!");

                            // 4. add bundle to intent
                            intent.putExtras(extras);

                            // 5. start the activity
                            startActivity(intent);




                            //Toast.makeText(getApplicationContext(), "Fill in and Verify Account", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();





                }else if(Error_code==3){

                    //Link Either Mpesa or Card Account


                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Payment Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("To Continue Please link a Debit/Credit Card Account or MPESA Account");


                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_launcher);

                    // Setting OK Button
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Add Card", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed

                            // 1. create an intent pass class name or intnet action name
                            Intent intent = new Intent(ChatActivity.this, add_card.class);

                            // 2. put key/value data
                            intent.putExtra("useridmain", useridGlob);











                            // 3. or you can add data to a bundle
                            Bundle extras = new Bundle();
                            extras.putString("status", "useridvariablereceived!");

                            // 4. add bundle to intent
                            intent.putExtras(extras);

                            // 5. start the activity
                            startActivity(intent);


                            // Toast.makeText(getApplicationContext(), "Continuing ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Add M-Pesa", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // 1. create an intent pass class name or intnet action name
                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.add_mpesa, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.mpesa_number_text);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    // get user input and set it to result
                                                    // edit text



                                                    String mpesa_no_this=userInput.getText().toString();



                                                    new addMpesaRequest().execute(usernameGlob,mpesa_no_this,message_id);





                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();





                            //Toast.makeText(getApplicationContext(), "Continueing", Toast.LENGTH_SHORT).show();
                        }
                    });


                    alertDialog.show();





                }else if(Error_code==4){
                  //insufficient funds in virtual Account link mpesa or Card Account




                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ChatActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Payment Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Virtual Wallet account doesnt have sufficient Funds to Continue Please link a Debit/Credit Card Account or MPESA Account");


                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_launcher);

                    // Setting OK Button
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Add Card", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed

                            // 1. create an intent pass class name or intnet action name
                            Intent intent = new Intent(ChatActivity.this, add_card.class);

                            // 2. put key/value data
                            intent.putExtra("useridmain", useridGlob);











                            // 3. or you can add data to a bundle
                            Bundle extras = new Bundle();
                            extras.putString("status", "useridvariablereceived!");

                            // 4. add bundle to intent
                            intent.putExtras(extras);

                            // 5. start the activity
                            startActivity(intent);


                            // Toast.makeText(getApplicationContext(), "Continuing ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Add M-Pesa", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed




                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.add_mpesa, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.mpesa_number_text);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    // get user input and set it to result
                                                    // edit text



                                                    String mpesa_no_this=userInput.getText().toString();



                                                    new addMpesaRequest().execute(usernameGlob,mpesa_no_this,message_id);




                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();








                        }
                    });


                    alertDialog.show();




                }












            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void createNotification(View view,String title, String details) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MainActivitySearch.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Notification From " + title)
                .setContentText(details).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .addAction(R.drawable.phone_icon_small, "Call", pIntent)
                .addAction(R.drawable.history_small_icon, "More", pIntent)
                .addAction(R.drawable.history_small_icon, "And more", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }


    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            //callMethod();

            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //callMethod();

                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
                break;

            default:
                break;
        }
    }


    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            String url_img = (String) params[0];

            Log.e("URL:Image request",url_img);
            return getBitmapFromURL(url_img);
        }

        @Override
        protected void onPostExecute(Bitmap result) {


            //save bitmap here
            boolean savestatus = saveImageToInternalStorage(result, conversationidGlob);

            if(savestatus)
            {
                Drawable newdrawable;

                newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(result,  90, 90, true));

                getSupportActionBar().setHomeAsUpIndicator(newdrawable); //you can set here image

            }




            Log.v("Save Status:", Boolean.toString(savestatus));
        }

//end of download image async task
    }

    //save image in external storage method
    public boolean saveImageToExternalStorage(Bitmap image,String filename) {
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