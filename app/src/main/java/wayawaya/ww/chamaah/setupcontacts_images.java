package wayawaya.ww.chamaah;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class setupcontacts_images extends AppCompatActivity  {

    private static final String TAG = setupcontacts_images.class.getSimpleName();

    public static final int PERMISSION_REQUEST_CONTACT = 79;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    private ProgressBar pb;
    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;

    private List<chat_summary> contentList = new ArrayList<>();



    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error_message";

    private final String KEY_MSG = "message";



    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;

    String useridGlob="";

    String deviceidthis="";

    String usernameGlob="";





    public static final String Username = "usernamepref";


    public static final String Usercurrency = "usercurrencypref";
    public static final String Usercountry = "usercountrypref";
    public static final String Userrate = "userratepref";
    public static final String Userlogintype = "userlogintypepref";
    public static final String Usersocialid = "usersocialidpref";
    public static final String Usersocialimage = "usersocialimagepref";





    SharedPreferences sharedpreferences;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupcontacts_images);

        pb=(ProgressBar)findViewById(R.id.signupProgress);

        pb.setVisibility(View.VISIBLE);


        Intent intent = getIntent();


        // 2. get message value from intent

        String useridgot = intent.getStringExtra("useridmain");


        useridGlob = useridgot;


        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();



        askForContactPermission();







        //end of oncreate method
    }





    public void add_phone_contacts(){

        ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            ArrayList<String> alContacts = new ArrayList<String>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        //.DISPLAY_NAME
                        alContacts.add(contactNumber);


                        String number_clear=contactNumber.replaceAll("[\\D]", "");

                        String number_final=number_clear.replaceFirst("^0", "254");


                        number_final = number_final.replaceAll("\\s","");




                        new checkContactsRequest().execute(useridGlob,contactName,number_final);

                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }




        pb.setVisibility(View.GONE);

        //MOVE TO ANOTHER VIEW




        Intent intent = new Intent(setupcontacts_images.this, loginscreen.class);

        // 2. put key/value data
        intent.putExtra("useridmain", useridGlob);


        startActivity(intent);




    }


//function to upload contacts to Server

    public class checkContactsRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String name= (String)params[1];

            String phone_no= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/check_contacts.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("name", name);

                postDataParams.put("phone_no", phone_no);

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

            //contentList=new ArrayList<>();

            getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }



    public class uploadContactsRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String name= (String)params[1];

            String phone_no= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/refresh_contacts.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("name", name);

                postDataParams.put("phone_no", phone_no);

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

            //getInfoResult(result);

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
            if (jsonObject.getString(KEY_SUCCESS).equals("1")) {




                    String user_title = jsonObject.getString("name");

                    int user_id_this = jsonObject.getInt("account_id");

                    String user_account_name = jsonObject.getString("account_name");

                    String user_nickname = jsonObject.getString("nickname");

                    String user_image = jsonObject.getString("profile_picture");

                    String saveid="contacts" + useridGlob;

                    contentList = getGlobalArrayList(saveid);

                    //update from online sources
                    // chatHistory =getInfo(useridGlob);

                    if (contentList != null && !contentList.isEmpty()) {




                        chat_summary content = new chat_summary(user_title, user_nickname, user_account_name, user_id_this, user_image, 0);
                        contentList.add(content);

                        String save_id = "contacts" + user_id_this;

                        saveGlobalArrayList(contentList, save_id);


                    } else {

                        contentList = new ArrayList<chat_summary>();


                        chat_summary content = new chat_summary(user_title, user_nickname, user_account_name, user_id_this, user_image, 0);
                        contentList.add(content);

                        String save_id = "contacts" + user_id_this;

                        saveGlobalArrayList(contentList, save_id);


                    }

                    //update from online sources
                    // chatHistory =getInfo(useridGlob);



                    // playersModelArrayList.addAll(chatRefreshHistory);






               // mAdapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playersModelArrayList;
    }




//check for contacts permission

    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                add_phone_contacts();
            }
        }
        else{
            add_phone_contacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    add_phone_contacts();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    // ToastMaster.showMessage(this,"No permission for contacts");

                    Toast.makeText(setupcontacts_images.this,"Permissions for contacts were not granted , unable to add new contacts",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //end of check for contacts permission



    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    public void saveGlobalArrayList(List<chat_summary> list,String key){



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<chat_summary> getGlobalArrayList(String key){



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
    }




}
