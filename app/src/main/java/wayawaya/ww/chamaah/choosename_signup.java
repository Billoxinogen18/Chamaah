package wayawaya.ww.chamaah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 25/06/2018.
 */

public class choosename_signup extends AppCompatActivity {

    private EditText  first_name,last_name;

    private TextView phone_label,username_label;

    private EditText username_entry;

    private Button complete_button;




    String useridGlob="";

    String deviceidthis="";

    String usernameGlob="";

    String country;

    String countrycodeM;

    private ImageView username_valid_status;

    String chatidGlob="Friend";

    String strSeperator="|#|";

    String strSeperatorMain="@%@";


    private ProgressBar pb;

    private ImageView profile_image;
    private boolean error=false;

    private boolean loadingData = false;

    private boolean username_valid = false;


    final Context context = this;



    private final String KEY_STATUS = "status";

    private final String KEY_CODE = "code";
    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error_message";

    private final String KEY_MSG = "message";

    private ProgressBar Pb;



    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;

    int GET_FROM_GALLERY=1;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosename_signup);

        final Spinner countryspinnerthis;

        countryspinnerthis = (Spinner)findViewById(R.id.country_name);

        phone_label = (TextView) findViewById(R.id.phone_label);

        username_label= (TextView) findViewById(R.id.usernamelabel);

        profile_image=(ImageView) findViewById(R.id.profile_picture);

        first_name = (EditText) findViewById(R.id.full_names_text);

        last_name = (EditText) findViewById(R.id.last_name);

        username_entry = (EditText) findViewById(R.id.username_textfield);

        complete_button=(Button) findViewById(R.id.complete_signup);

        username_valid_status=(ImageView) findViewById(R.id.username_allowed_image);

        pb = (ProgressBar) findViewById(R.id.signup_progressBar);

        pb.setVisibility(View.GONE);

        complete_button.setEnabled(false);



        username_valid_status.setImageResource(R.drawable.no_image);


        username_entry.setFocusable(true);
        username_entry.setEnabled(true);
        username_entry.setCursorVisible(true);
        //username_entry.setKeyListener(null);
       // username_entry.setBackgroundColor(Color.TRANSPARENT);

        //verifyProgress

        Intent intent = getIntent();


        // 2. get message value from intent

        String useridgot = intent.getStringExtra("useridmain");


        useridGlob = useridgot;


        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        if (!TextUtils.isEmpty(useridGlob)) {

            phone_label.setText(useridgot);


        }


        getSupportActionBar().setTitle("Complete"); // for set actionbar title

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // for add back arrow in action bar





        findViewById(R.id.add_image_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {


//pick image from gallery

                            checkPermission();


                        }else {


                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }


                    }
                });

        ((EditText)findViewById(R.id.username_textfield)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {

                    String ipaddress = getLocalIpAddress();

                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String countryCode = tm.getSimCountryIso();

                           /*
                            String getSimSerialNumber = tm.getSimSerialNumber();
                            String getSimNumber = tm.getLine1Number();

                            */

                    // Get carrier name (Network Operator Name)
                    String carrierName = tm.getNetworkOperatorName();

                    // Get Phone model and manufacturer name
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;

                    deviceidthis = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    String device_platform = "Android";

                    String app_version = "1.0.0";




                    new checkusername().execute(username_entry.getText().toString(),deviceidthis,ipaddress, device_platform, app_version, app_version);



                }

            }
        });
/*
        ((EditText)findViewById(R.id.username_textfield)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {



                a.toLowerCase();


                if (!hasFocus) {


                    String ipaddress = getLocalIpAddress();

                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String countryCode = tm.getSimCountryIso();



                    // Get carrier name (Network Operator Name)
                    String carrierName = tm.getNetworkOperatorName();

                    // Get Phone model and manufacturer name
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;

                    deviceidthis = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    String device_platform = "Android";

                    String app_version = "1.0.0";

              


                    new checkusername().execute(username_entry.getText().toString(),deviceidthis,ipaddress, device_platform, app_version, app_version);


                }
            }
        });

*/




        findViewById(R.id.complete_signup).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        if(loadingData==false) {
                            //show alert dialog

                            if (TextUtils.isEmpty(first_name.getText().toString())||TextUtils.isEmpty(last_name.getText().toString())) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        choosename_signup.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Please enter Full Names in the First Name and Last Name Field");

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

                                // Toast.makeText(this, "please enter Userid", Toast.LENGTH_LONG).show();
                            }else if( TextUtils.isEmpty(username_entry.getText().toString())){


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        choosename_signup.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Please select username to continue");

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


                            } else {



                                if (username_valid == false) {

                                    username_label.setText("Enter Username");

                                    username_entry.setHint("Choose available username");


                                    username_entry.setBackgroundColor( getResources().getColor(R.color.pale_blue));



                                } else {
//start of selected user id is valid
                                    String ipaddress = getLocalIpAddress();

                                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                    String countryCode = tm.getSimCountryIso();

                           /*
                            String getSimSerialNumber = tm.getSimSerialNumber();
                            String getSimNumber = tm.getLine1Number();

                            */

                                    // Get carrier name (Network Operator Name)
                                    String carrierName = tm.getNetworkOperatorName();

                                    // Get Phone model and manufacturer name
                                    String manufacturer = Build.MANUFACTURER;
                                    String model = Build.MODEL;

                                    deviceidthis = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                                    String device_platform = "Android";

                                    String app_version = "1.0.0";


                                    String loginstring_this = "NULL";

                                    pb.setVisibility(View.VISIBLE);

                                    error = false;

                                    //prepare image data here

                                    BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();

                                    Bitmap bitmap = drawable.getBitmap();

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                                    byte[] byteArray = baos.toByteArray();

                                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                    //end of prepare image data here



                                    new onbuttonclickHttpPost().execute(username_entry.getText().toString(),first_name.getText().toString(), last_name.getText().toString(), loginstring_this, deviceidthis, ipaddress, device_platform, app_version,  encodedImage, useridGlob);

                                }


                                //end of user id is valid

                            }
//end of no other request is being processed
                        }else {


                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }





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

                profile_image.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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


    private class onbuttonclickHttpPost extends AsyncTask<String, Integer, String> {

        String result = "";
        String request="";
        String RphoneGlob="";
        String RamountGlob="";
        String RcountryGlob="";

        ProgressDialog Asycdialog = new ProgressDialog(choosename_signup.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Asycdialog.setMessage("Uploading...");
            Asycdialog.show();
        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            loadingData = true;

            String usercode = (String)params[0];
            String firstnames= (String)params[1];
            String lastnames= (String)params[2];

            String loginstring= (String)params[3];
            String deviceid= (String)params[4];
            String ipaddress = (String)params[5];
            String platform= (String)params[6];
            String version= (String)params[7];
            String imagecode= (String)params[8];
            String username = (String)params[9];







            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);




            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://10.0.2.2/chotu/index.php");
            HttpPost httppost = new HttpPost("https://www.wayawaya.co.ke/chamah_front/completesignup.php");
            try {

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("imagecode", imagecode));
                nameValuePairs.add(new BasicNameValuePair("usercode", usercode));
                nameValuePairs.add(new BasicNameValuePair("firstname", firstnames));

                nameValuePairs.add(new BasicNameValuePair("lastname", lastnames));
                nameValuePairs.add(new BasicNameValuePair("loginstring", loginstring));
                nameValuePairs.add(new BasicNameValuePair("deviceid", deviceid));

                nameValuePairs.add(new BasicNameValuePair("ipaddress", ipaddress));
                nameValuePairs.add(new BasicNameValuePair("platform", platform));
                nameValuePairs.add(new BasicNameValuePair("version", version));

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
                        choosename_signup.this).create();

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
            pb.setVisibility(View.GONE);

            Asycdialog.dismiss();

            loadingData = false;

            //PARSE JSON RESPONSE

            if(error==true){

                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        choosename_signup.this).create();

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

                        String userid_this = jsonObject.getString("account_phone");

                        String accountid_this = jsonObject.getString("account_id");



                        //((mimiappmain) this.getApplication()).setTransactionid(transactionid);






                        Intent intent = new Intent(choosename_signup.this, setupcontacts_images.class);

                        // 2. put key/value data
                        intent.putExtra("useridmain", useridGlob);






                        // 3. or you can add data to a bundle
                        // Bundle extras = new Bundle();
                        //extras.putString("status", "useridvariablereceived!");

                        // 4. add bundle to intent
                        //intent.putExtras(extras);

                        startActivity(intent);



                    }
                    else if(successStatus.equals("2")){

                        String errorStatus = jsonObject.getString("desc");
                        //Toast.makeText(getApplicationContext(), "Login Error:"+errorStatus+"userid:"+useridthis+"password:"+passwordthis+"token:"+tokenthis+"deviceid:"+deviceidthis, Toast.LENGTH_LONG).show();

                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                choosename_signup.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Signup Failed");

                        // Setting Dialog Message
                        alertDialog.setMessage(""+errorStatus+" ");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                error = false;
                                pb.setVisibility(View.GONE);
                                //new logoutaction().execute(userGlob);


                                Toast.makeText(getApplicationContext(), "Please login and try again ", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                        //verify account to continue


                    }

                    else{
                        //get error code
                        String errorStatus = jsonObject.getString("desc");
                        //Toast.makeText(getApplicationContext(), "Login Error:"+errorStatus+"userid:"+useridthis+"password:"+passwordthis+"token:"+tokenthis+"deviceid:"+deviceidthis, Toast.LENGTH_LONG).show();

                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                choosename_signup.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Signup Failed");

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






    public class checkusername extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

           // loadingData=true;

            String username = (String)params[0];
            String deviceid= (String)params[1];
            String ipaddress = (String)params[2];
            String platform= (String)params[3];
            String version= (String)params[4];




            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/checkusername.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("deviceid", deviceid);
                postDataParams.put("ipaddress", ipaddress);
                postDataParams.put("platform", platform);
                postDataParams.put("version", version);


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


            //pb.setVisibility(View.GONE);

            //loadingData=false;


            Log.e("on usercheck result",result);

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


    public ArrayList<ChatMessage> getInfo(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_STATUS).equals("1")) {
//notify that username is not available
                complete_button.setEnabled(true);

                username_valid=true;

                username_valid_status.setImageResource(R.drawable.yes_image);

                //disable textfield here

                username_entry.setFocusable(false);
                username_entry.setEnabled(false);
                username_entry.setCursorVisible(false);
                username_entry.setKeyListener(null);
                username_entry.setBackgroundColor(Color.TRANSPARENT);

                //disable textfield here






//notify that user


            }  else {
                //signup process had an Error

                complete_button.setEnabled(false);

                String error =jsonObject.getString(KEY_ERROR) ;

                username_valid=false;


                username_label.setText("Failed:"+error);


                username_entry.setBackgroundColor( getResources().getColor(R.color.pale_blue));




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playersModelArrayList;
    }





    public String getLocalIpAddress()  {
        String IPAddress = null;
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        IPAddress = inetAddress.getHostAddress().toString();

                        //Log.e("IP1:", IPAddress);
                    }
                }
            }

        }
        catch (SocketException ex)
        {
            Log.e("ServerActivity", ex.toString());
            return null;
        }

        // Log.e("IP2:", IPAddress);
        return IPAddress;

    }

//Load public Key

    public String getmyIpaddress(){

        String  IPAddress= null;

        try{
            InetAddress ownIP=InetAddress.getLocalHost();
            //System.out.println("IP of my Android := "+ownIP.getHostAddress());

            IPAddress=ownIP.toString();
        }catch (Exception e){
            // System.out.println("Exception caught ="+e.getMessage());
        }


        return IPAddress;


    }



    private static byte[] encode(MessageDigest mda, byte[] pwdBytes,
                                 byte[] saltBytes) {
        mda.update(pwdBytes);
        byte [] digesta = mda.digest(saltBytes);
        return digesta;
    }






}
