package wayawaya.ww.chamaah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 12/06/2018.
 */

public class loginscreen extends AppCompatActivity {

    private static final String TAG = MainActivitySearch.class.getSimpleName();

    RelativeLayout notificationCount1;

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    private LoginButton loginBtn;

    private AccessTokenTracker accessTokenTracker;

    private AccessToken accessToken;


    LoginManager loginManager;

    EditText usernameText, passwordText;




    private Button signingButton;

    private String imageURL="",emailGlob="",usernameGlob="",useridGlob="";

    private static final String EMAIL = "email";

    private String usernamethisG="";


    private ProgressBar pb;
    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;


    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error_message";

    private final String KEY_MSG = "message";


    String deviceidthis="";






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
        setContentView(R.layout.loginscreen);






        usernameText=(EditText) findViewById(R.id.username);

        passwordText=(EditText) findViewById(R.id.password_field);

        pb=(ProgressBar)findViewById(R.id.progressBar);

        pb.setVisibility(View.GONE);

        //check if user is logged in


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        if(isLoggedIn==true){





            Intent intent = new Intent(loginscreen.this, MainActivity.class);




            startActivity(intent);


        }






        findViewById(R.id.signupbutton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //show alert dialog


                        Intent intent = new Intent(loginscreen.this, signupstart.class);



                        startActivity(intent);


                    }
                });



        findViewById(R.id.loginbutton_n).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //check that the textfields are not empty

                        if (TextUtils.isEmpty(usernameText.getText().toString())||TextUtils.isEmpty(passwordText.getText().toString())) {


                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    loginscreen.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please enter username and password to Continue");

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
                        } else{

                            String username_this=usernameText.getText().toString();
                            String password_this=passwordText.getText().toString();






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

                            String token_this = "dsaerw";

                            String getSimSerialNumber = "NULL";
                            String getSimNumber = "NULL";

                            pb.setVisibility(View.VISIBLE);

                            //error=false;

                            String firebase_token=onTokenRefresh();


                            new SendMessageRequest().execute(usernameText.getText().toString(), passwordText.getText().toString(), countryCode, deviceidthis, ipaddress, countryCode, device_platform, app_version, carrierName, manufacturer, model, getSimSerialNumber, getSimNumber, token_this,firebase_token);




                        //end of textfields values have been entered
                    }


                    }
                });


        //CODE FOR FACEBOOK LOGIN


        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);


            // Callback registration
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code




                    final AccessToken accessToken = loginResult.getAccessToken();
                    GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                            LoginManager.getInstance().logOut();


                            //usernameGlob = (user.optString("name"));

                            usernameGlob = "Collins Odhiambo";


                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Username, usernameGlob);


                            editor.commit();


                            Intent intent = new Intent(loginscreen.this, MainActivitySearch.class);




                            startActivity(intent);




                        }
                    }).executeAsync();




                }


                @Override
            public void onCancel() {
                // App code
                Toast.makeText(loginscreen.this, "Facebook Login Canceled", Toast.LENGTH_LONG).show();




            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Toast.makeText(loginscreen.this, "Facebook login encountered an Error", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(loginscreen.this, MainActivitySearch.class);


                startActivity(intent);

            }
        });


        //END OF CODE FOR FACEBOOK LOGIN





    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }





    //login activity here

    public class SendMessageRequest extends AsyncTask<String, Void, String> {

        String usernameGlobThis="";

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            loadingData=true;

            String username = (String)params[0];

            String password= (String)params[1];
            String country= (String)params[2];
            String deviceid = (String)params[3];
            String ipaddress= (String)params[4];
            String devicecountry= (String)params[5];
            String platform= (String)params[6];
            String app_version= (String)params[7];
            String telco = (String)params[8];
            String devicemanufacturer= (String)params[9];
            String devicemodel= (String)params[10];
            String sim_serial= (String)params[11];
            String sim_number= (String)params[12];
            String token= (String)params[13];
            String firebase_token= (String)params[14];

            usernameGlobThis=username;

            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/chamah_login_session.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);

                postDataParams.put("password", password);
                postDataParams.put("country", country);
                postDataParams.put("deviceid", deviceid);
                postDataParams.put("ipaddress", ipaddress);
                postDataParams.put("devicecountry", devicecountry);
                postDataParams.put("platform", platform);
                postDataParams.put("app_version", app_version);
                postDataParams.put("telco", telco);
                postDataParams.put("devicemanufacturer", devicemanufacturer);
                postDataParams.put("devicemodel", devicemodel);
                postDataParams.put("simserial", sim_serial);
                postDataParams.put("simnumber", sim_number);
                postDataParams.put("token", token);
                postDataParams.put("firebase_token", firebase_token);

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

            pb.setVisibility(View.GONE);

            loadingData=false;


            Log.e("onmessagesentresults",result);

            getInfo(result,usernameGlobThis);

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


    public ArrayList<ChatMessage> getInfo(String response,String username) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("1")) {
//REQUEST WAS PROCESSED SUCCESFULLY



                usernameGlob = username;


                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Username, usernameGlob);


                editor.commit();


                // 1. create an intent pass class name or intnet action name
                Intent intent = new Intent(loginscreen.this, MainActivitySearch.class);

                // 2. put key/value data
                intent.putExtra("useridmain", useridGlob);











                // 3. or you can add data to a bundle
                Bundle extras = new Bundle();
                extras.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent.putExtras(extras);

                // 5. start the activity
                startActivity(intent);



            }else if(jsonObject.getString(KEY_SUCCESS).equals("2")){



                AlertDialog alertDialog = new AlertDialog.Builder(
                        loginscreen.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Verification Required");

                // Setting Dialog Message
                alertDialog.setMessage("To Continue to Use Account Please Verify ");


                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed

                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(loginscreen.this, account_verify.class);

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

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(loginscreen.this, loginscreen.class);

                        // 2. put key/value data


                        intent.putExtra("useridmain", useridGlob);






                        // 3. or you can add data to a bundle
                        Bundle extras = new Bundle();
                        extras.putString("status", "useridvariablereceived!");

                        // 4. add bundle to intent
                        intent.putExtras(extras);

                        // 5. start the activity
                        startActivity(intent);

                        // Intent intent = new Intent(Mainactivity.this, dashboard.class);
                        // startActivity(intent);

                        //Toast.makeText(getApplicationContext(), "Continueing", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDialog.show();

            }  else {
                //signup process had an Error

                String error =jsonObject.getString(KEY_ERROR) ;




                AlertDialog alertDialog = new AlertDialog.Builder(
                        loginscreen.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Setup Error");

                // Setting Dialog Message
                alertDialog.setMessage(error);


                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed




                        // Toast.makeText(getApplicationContext(), "Continuing ", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(loginscreen.this, loginscreen.class);

                        // 2. put key/value data


                        intent.putExtra("useridmain", useridGlob);






                        // 3. or you can add data to a bundle
                        Bundle extras = new Bundle();
                        extras.putString("status", "useridvariablereceived!");

                        // 4. add bundle to intent
                        intent.putExtras(extras);

                        // 5. start the activity
                        startActivity(intent);

                        // Intent intent = new Intent(Mainactivity.this, dashboard.class);
                        // startActivity(intent);

                        //Toast.makeText(getApplicationContext(), "Continueing", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDialog.show();

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


    //end of login activity
}
