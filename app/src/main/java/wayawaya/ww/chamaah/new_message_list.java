package wayawaya.ww.chamaah;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by teddyogallo on 05/07/2018.
 */

public class new_message_list extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {
    private static final String TAG = MainActivitySearch.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;

    public static final int PERMISSION_REQUEST_CONTACT = 79;



    // url to fetch contacts json
    //private static final String URL = "https://www.wayawaya.co.ke/chamah_front/home_notifications_test.php?";


    private String URL2 = "https://www.wayawaya.co.ke/chamah_front/home_notifications_test.php";


    RelativeLayout notificationCount1;

    TextView name_label;


    String useridGlob="";

    String usernameGlob="";

    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;


    public static final String Username = "usernamepref";


    public static final String Usercurrency = "usercurrencypref";
    public static final String Usercountry = "usercountrypref";
    public static final String Userrate = "userratepref";
    public static final String Userlogintype = "userlogintypepref";
    public static final String Usersocialid = "usersocialidpref";
    public static final String Usersocialimage = "usersocialimagepref";





    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_list);


        // Retrieve the saved values.


        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String usernamepref = mySharedPreferences.getString("usernamepref", "");
        String usercurrencypref = mySharedPreferences.getString("usercurrencypref", "");

        String userlogintypepref = mySharedPreferences.getString("userlogintypepref", "");
        String usersocialidpref = mySharedPreferences.getString("usersocialidpref", "");
        String usersocialpref = mySharedPreferences.getString("usersocialimagepref", "");


        usernameGlob = usernamepref;
        useridGlob = usernamepref;
        currencyGlob = usercurrencypref;



        getSupportActionBar().setTitle("New Message"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        // loadOfflineData();

        fetchContacts(URL2+"?userid="+useridGlob);











        findViewById(R.id.invite_contact_text).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                new_message_list.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Request Cannot be Completed");

                        // Setting Dialog Message
                        alertDialog.setMessage("Cannot invite new Contacts at the Moment");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                Toast.makeText(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();





                    }
                });






        //end of oncreate option
    }



    /**
     * fetches json by making http calls
     */
    private void fetchContacts(String url_this) {
        JsonArrayRequest request = new JsonArrayRequest(url_this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the Data! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());

                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);


                        saveGlobalArrayList(contactList);


                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    private void loadOfflineData(){

        contactList.clear();

        contactList=getGlobalArrayList();

        if (contactList != null && !contactList.isEmpty()) {

            /*
        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
        }.getType());

            contactList.addAll(items);



            // adding contacts to contacts list

        */

            mAdapter.notifyDataSetChanged();

        }else {

            contactList.clear();

        }

    }



    public String onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);

        return refreshedToken;
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed


        Intent intent = new Intent(new_message_list.this, MainActivitySearch.class);

        // 3. or you can add data to a bundle
        Bundle extras = new Bundle();
        extras.putString("status", "useridvariablereceived!");

        // 4. add bundle to intent
        intent.putExtras(extras);

        startActivity(intent);



    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Contact contact) {


        conversationidGlob=contact.getName() ;




        Intent intent = new Intent(new_message_list.this, ChatActivity.class);

// 2. put key/value data


        intent.putExtra("conversationidmain",  conversationidGlob);



        // 3. or you can add data to a bundle
        Bundle extras = new Bundle();
        extras.putString("status", "useridvariablereceived!");

        // 4. add bundle to intent
        intent.putExtras(extras);

        startActivity(intent);



        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }



    public void saveGlobalArrayList(List<Contact> list){

        String key="cmahglob";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<Contact> getGlobalArrayList(){

        String key="cmahglob";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
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


//end of function to upload contacts to server

}
