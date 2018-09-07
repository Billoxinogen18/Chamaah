package wayawaya.ww.chamaah;

/**
 * Created by teddyogallo on 13/06/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import javax.net.ssl.HttpsURLConnection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by teddyogallo on 10/06/2018.
 */

public class groupChatActivity extends AppCompatActivity {


    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapterGroup adapter;
    private ArrayList<ChatMessageGroup> chatHistory;

    private int group_id_glob;

    private ArrayList<ChatMessageGroup> chatInboxHistory;

    private ArrayList<ChatMessageGroup> chatRefreshHistory;
    private ArrayList<ChatMessageGroup> chatMoneysentHistory;

    private ArrayList<ChatMessageGroup> chatPayRequestHistory;


    private ArrayList<chat_summary> chatGlobalHistory;




    private String card_account_id="";

    private String card_account_no="";

    private String mpesa_account_no="";

    private String message_id="";

    private String payment_amount = "";

    Button extra_menu_button;

    RelativeLayout notificationCount1;

    TextView friend_label,Self_label;

    int useridGlob_num=0;


    String useridGlob="";

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


    int GET_FROM_GALLERY=1;

    private  Double balanceGlob=0.00;





    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupactivity_chat);



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

        int groupidgotthis=intent.getIntExtra("groupidmain",0);



        group_id_glob=groupidgotthis;
        conversationidGlob=conversationididgot;



        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

       // Log.d("MYINT", "Group Id Value: " +  group_id_glob);

        if(!TextUtils.isEmpty(conversationidGlob)) {

            chatidGlob=conversationidGlob;
        }

        //load the History here
        initControls();

        //end of load the history here

        friend_label.setText(chatidGlob);

        /*
        getSupportActionBar().setTitle(chatidGlob); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar


        getSupportActionBar().setIcon(R.drawable.icon_80);

*/

        String groupid_got=""+group_id_glob;
        Bitmap bitmap2= getThumbnail(groupid_got);


        Drawable drawablet= getResources().getDrawable(R.drawable.icon_87);
        Bitmap bitmap_t = ((BitmapDrawable) drawablet).getBitmap();

        Bitmap emptyBitmap = Bitmap.createBitmap(bitmap_t.getWidth(), bitmap_t.getHeight(), bitmap_t.getConfig());

        Drawable newdrawable;


        if (bitmap2== null || bitmap2.sameAs(emptyBitmap)) {
            // myBitmap is empty/blank

            Drawable drawable= getResources().getDrawable(R.drawable.icon_87);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
             newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,  90, 90, true));



        }else {

            Bitmap bmp2 = bitmap2.copy(bitmap2.getConfig(), true);


            newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bmp2,  90, 90, true));



        }


        TextView actionBartitle;

        TextView balanceBartitle;

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
        actionBartitle.setText(chatidGlob);


        balanceBartitle = (TextView) v.findViewById(R.id.userlastseen);
        balanceBartitle.setText("Balance: "+balanceGlob);

        getSupportActionBar().setCustomView(v);



        findViewById(R.id.side_menu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(groupChatActivity.this, extra_menu_button);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.group_message_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(groupChatActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


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




                                }else if(item.getTitle().equals("Request Loan")){

                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompt_loanrequest, null);

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

                                                            chatPayRequestHistory = new ArrayList<ChatMessageGroup>();

                                                            chatPayRequestHistory=getArrayList(chatidGlob);


                                                            payment_amount=userInput.getText().toString();

                                                            String messageText="Loan Request of KES "+payment_amount+" Sent";

                                                            String message_type="payment_request";

                                                            new SendMessageRequest().execute(usernameGlob,chatidGlob,messageText,message_type);

                                                            //go to complete bill payment
                                                            ChatMessageGroup ChatMessageGroup = new ChatMessageGroup();
                                                            ChatMessageGroup.setId(122);//dummy
                                                            ChatMessageGroup.setMessage(messageText);
                                                            ChatMessageGroup.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                                                            ChatMessageGroup.setMe(true);

                                                            chatPayRequestHistory.add(ChatMessageGroup);

                                                            saveArrayList(chatPayRequestHistory,chatidGlob);

                                                            chatPayRequestHistory.clear();

                                                            messageET.setText("");

                                                            displayMessage(ChatMessageGroup);
                                                            //end of normal processing

                                                            Toast.makeText(groupChatActivity.this,"Your "+payment_amount+"Loan request has been submitted to the Group for Approval ",Toast.LENGTH_SHORT).show();
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


                                    checkPermission();

                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu


                    }
                });

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
                String messageText = messageET.getText().toString();

                String message_type="text";

                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                new SendMessageRequest().execute(usernameGlob,chatidGlob,messageText,message_type);

                chatHistory = new ArrayList<ChatMessageGroup>();

                chatHistory = getArrayList(chatidGlob);


                if (chatHistory != null && !chatHistory.isEmpty()) {


                    ChatMessageGroup ChatMessageGroup = new ChatMessageGroup();
                    ChatMessageGroup.setId(useridGlob_num);//dummy
                    ChatMessageGroup.setMessage(messageText);
                    ChatMessageGroup.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    ChatMessageGroup.setMe(true);


                    chatHistory.add(ChatMessageGroup);

                    saveArrayList(chatHistory, chatidGlob);

                    chatHistory.clear();

                    messageET.setText("");

                    displayMessage(ChatMessageGroup);



                }else {


                    chatHistory = new ArrayList<ChatMessageGroup>();

                    chatHistory = getArrayList(chatidGlob);


                    ChatMessageGroup ChatMessageGroup = new ChatMessageGroup();
                    ChatMessageGroup.setId(useridGlob_num);//dummy
                    ChatMessageGroup.setMessage(messageText);
                    ChatMessageGroup.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    ChatMessageGroup.setMe(true);


                    chatHistory.add(ChatMessageGroup);

                    saveArrayList(chatHistory, chatidGlob);

                    chatHistory.clear();

                    messageET.setText("");

                    displayMessage(ChatMessageGroup);


                    //end of chat history is not empty
                }
            }
        });
    }

    public void displayMessage(ChatMessageGroup message) {

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

        chatHistory = new ArrayList<ChatMessageGroup>();


        //update from online sources
        // chatHistory =getInfo(useridGlob);

        chatHistory = getArrayList(chatidGlob);

        //update from online sources
        // chatHistory =getInfo(useridGlob);

        if (chatHistory != null && !chatHistory.isEmpty()) {



            //update from online sources
            // chatHistory =getInfo(useridGlob);

/*
            ChatMessageGroup msg = new ChatMessageGroup();
            msg.setId(1);
            msg.setMe(false);
            msg.setMessage("Hi");
            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg);




            saveArrayList(chatHistory,chatidGlob);
*/


            adapter = new ChatAdapterGroup(groupChatActivity.this, new ArrayList<ChatMessageGroup>());
            messagesContainer.setAdapter(adapter);

            for(int i=0; i<chatHistory.size(); i++) {
                ChatMessageGroup message = chatHistory.get(i);


                displayMessage(message);
            }

            chatHistory.clear();
        }else {
            //chat history is not null
            chatHistory = new ArrayList<ChatMessageGroup>();



            ChatMessageGroup msg = new ChatMessageGroup();
            msg.setId(group_id_glob);
            msg.setUserId(group_id_glob);
            msg.setMe(false);
            msg.setMessage("Welcome");
            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg);




            saveArrayList(chatHistory,chatidGlob);



            adapter = new ChatAdapterGroup(groupChatActivity.this, new ArrayList<ChatMessageGroup>());
            messagesContainer.setAdapter(adapter);

            for(int i=0; i<chatHistory.size(); i++) {
                ChatMessageGroup message = chatHistory.get(i);


                displayMessage(message);
            }

            chatHistory.clear();

        }



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_messages_top_menu, menu);

        MenuItem item1 = menu.findItem(R.id.actionbar_item);



        //MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(groupChatActivity.this, groupslist.class);



                startActivity(intent);


                break;

            case R.id.actionbar_item:


               // Toast.makeText(getApplicationContext(),"Manage Group Accounts", Toast.LENGTH_SHORT).show();


                Intent intent2 = new Intent(groupChatActivity.this, group_accounts.class);



                startActivity(intent2);



                break;


            case R.id.history_group:
                Toast.makeText(getApplicationContext(),"Manage Group History", Toast.LENGTH_SHORT).show();



                break;

            case R.id.manage_group:
               Toast.makeText(getApplicationContext(),"Manage and Edit group", Toast.LENGTH_SHORT).show();


                break;
        }
        return true;
    }



    /**
     *     Save and get ArrayList in SharedPreference
     */

    public void saveArrayList(ArrayList<ChatMessageGroup> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<ChatMessageGroup> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<ChatMessageGroup>>() {}.getType();
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




    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String token = (String)params[1];

            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_notifications_group.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("logintoken", token);
                postDataParams.put("group_id", group_id);
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

            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages_group.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);

                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);
                postDataParams.put("group_id", group_id);

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




    public class SendImageRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String recipient_id= (String)params[1];
            String message= (String)params[2];
            String message_type= (String)params[3];
            String image_string= (String)params[4];


            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_image_messages.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);

                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);
                postDataParams.put("image_string",image_string);
                postDataParams.put("group_id", group_id);

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


            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages_money.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);

                postDataParams.put("amount", message);
                postDataParams.put("message_type", message_type);
                postDataParams.put("group_id", group_id);

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

            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/c_messages_group.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("groupid", recipient_id);
                postDataParams.put("messages", message);
                postDataParams.put("message_type", message_type);

                postDataParams.put("group_id", group_id);

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




    public class confirmCardPaymentRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String cvv_this= (String)params[1];
            String card_id= (String)params[2];
            String message_id= (String)params[3];


            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_card_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("cvv", cvv_this);
                postDataParams.put("cardid", card_id);
                postDataParams.put("message_id", message_id);

                postDataParams.put("group_id", group_id);

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


            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_mpesa_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mpesa_id", mpesa_id);

                postDataParams.put("message_id", message_id);

                postDataParams.put("group_id", group_id);

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



            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/confirm_wallet_payment.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mobile_pin", mpesa_id);

                postDataParams.put("message_id", message_id);

                postDataParams.put("group_id", group_id);

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


            String  group_id= ""+group_id_glob;


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/add_mpesa_request.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mpesa_no", mpesa_no);

                postDataParams.put("message_id", message_id);

                postDataParams.put("group_id", group_id);

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


    public ArrayList<ChatMessageGroup> getInfo(String response) {
        ArrayList<ChatMessageGroup> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {

                chatRefreshHistory = new ArrayList<ChatMessageGroup>();

                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {



                    chatRefreshHistory = new ArrayList<ChatMessageGroup>();


                    //update from online sources
                    // chatHistory =getInfo(useridGlob);

                    chatRefreshHistory = getArrayList(chatidGlob);

                    //update from online sources
                    // chatHistory =getInfo(useridGlob);

                    if (chatRefreshHistory != null && !chatRefreshHistory.isEmpty()) {


                        //update from online sources
                        // chatHistory =getInfo(useridGlob);

                        ChatMessageGroup msg = new ChatMessageGroup();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        String sender_name = dataobj.getString("name");

                        if (!sender_name.isEmpty()){

                            msg.setId(dataobj.getLong("id"));
                            msg.setUserId(dataobj.getLong("id"));
                            msg.setMe(dataobj.getBoolean("identity"));
                            msg.setMessage(dataobj.getString("message"));
                            msg.setDate(dataobj.getString("datetime"));
                            chatRefreshHistory.add(msg);


                            saveArrayList(chatRefreshHistory, sender_name);



                            update_global(sender_name, dataobj.getString("message"),11, dataobj.getString("datetime"), "");
/*
                        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessageGroup>());
                        messagesContainer.setAdapter(adapter);
*/


                            for (int t = 0; t < chatRefreshHistory.size(); t++) {
                                ChatMessageGroup message = chatRefreshHistory.get(t);

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
                        chatRefreshHistory = new ArrayList<ChatMessageGroup>();

                        ChatMessageGroup msg = new ChatMessageGroup();
                        JSONObject dataobj = dataArray.getJSONObject(i);

                        String sender_name=dataobj.getString("name");

                        if (!sender_name.isEmpty()) {

                            msg.setId(dataobj.getLong("id"));
                            msg.setUserId(dataobj.getLong("id"));
                            msg.setMe(dataobj.getBoolean("identity"));
                            msg.setMessage(dataobj.getString("message"));
                            msg.setDate(dataobj.getString("datetime"));
                            chatRefreshHistory.add(msg);


                            saveArrayList(chatRefreshHistory, sender_name);

/*
                            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
                            messagesContainer.setAdapter(adapter);

*/

                            for (int t = 0; t < chatRefreshHistory.size(); t++) {
                                ChatMessageGroup message = chatRefreshHistory.get(t);

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
        ArrayList<ChatMessageGroup> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String return_desc=jsonObject.getString("desc");
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        groupChatActivity.this).create();

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
                        groupChatActivity.this).create();

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
        ArrayList<ChatMessageGroup> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String success_desc=jsonObject.getString("DESC");

            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {



                chatMoneysentHistory = new ArrayList<ChatMessageGroup>();

                chatMoneysentHistory=getArrayList(chatidGlob);




                String messageText="KES "+payment_amount+" Sent";

                String message_type="payment_sent";



                //go to complete bill payment
                ChatMessageGroup chatMessage = new ChatMessageGroup();
                chatMessage.setId(122);//dummy
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
                        groupChatActivity.this).create();

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
        ArrayList<ChatMessageGroup> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {

                int success_code =jsonObject.getInt("code");

                message_id=jsonObject.getString("message_id");

                card_account_id=jsonObject.getString("card_account_id");


                mpesa_account_no=jsonObject.getString("mpesa_account_no");

                if(success_code==1){



                    AlertDialog alertDialog = new AlertDialog.Builder(
                            groupChatActivity.this).create();

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
                            groupChatActivity.this).create();

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
                            Intent intent = new Intent(groupChatActivity.this, signupstart.class);

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
                            groupChatActivity.this).create();

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
                            Intent intent = new Intent(groupChatActivity.this, signupstart.class);

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
                            groupChatActivity.this).create();

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
                            Intent intent = new Intent(groupChatActivity.this, add_card.class);

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
                            groupChatActivity.this).create();

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
                            Intent intent = new Intent(groupChatActivity.this, add_card.class);

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




    //save image in external storage method
    public boolean saveImageToExternalStorage(Bitmap image) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, "desiredFilename.png");
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






}