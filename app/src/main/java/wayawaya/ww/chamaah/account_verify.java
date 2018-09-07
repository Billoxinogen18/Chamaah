package wayawaya.ww.chamaah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 22/06/2018.
 */

public class account_verify extends AppCompatActivity {

    private EditText smscode;

    TextView  phone_label, main_status;


    String useridGlob="";

    String deviceidthis="";

    String usernameGlob="";

    String chatidGlob="Friend";

    String strSeperator="|#|";

    String strSeperatorMain="@%@";


    private ProgressBar pb;
    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;


    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error_message";

    private final String KEY_MSG = "message";

    private ProgressBar Pb;



    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;


    //for firebase verification

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    //end for firebase verification


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_verify);

        phone_label=(TextView)findViewById(R.id.phone_label);


        main_status=(TextView)findViewById(R.id.main_status);

        smscode=(EditText) findViewById(R.id.smscode);

        pb=(ProgressBar)findViewById(R.id.verifyProgress);

        pb.setVisibility(View.GONE);

        //verifyProgress

        Intent intent = getIntent();


        // 2. get message value from intent

        String useridgot = intent.getStringExtra("useridmain");




        useridGlob=useridgot;



        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        if(!TextUtils.isEmpty(useridGlob)) {

            phone_label.setText(useridgot);


        }





        getSupportActionBar().setTitle("Verify Phone"); // for set actionbar title

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // for add back arrow in action bar





        findViewById(R.id.exit_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {

                            //show alert dialog


                            Intent intent = new Intent(account_verify.this, signupstart.class);


                            startActivity(intent);

                        }else {


                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }


                    }
                });

        findViewById(R.id.resend_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {


                            //show alert dialog

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


                            new resendTokenRequest().execute(useridGlob, loginstring_this, deviceidthis, ipaddress, device_platform, app_version, app_version);


                            //end of no other request is being processed
                        }else {

                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });



        findViewById(R.id.continue_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {
                            //show alert dialog

                        if (TextUtils.isEmpty(smscode.getText().toString())) {


                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    account_verify.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please enter One time Password sent to your Phone Number to Continue");

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
                        } else {

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




                            new SendMessageRequest().execute(useridGlob, smscode.getText().toString(), loginstring_this, deviceidthis, ipaddress, device_platform, app_version, app_version);

                        }

//end of no other request is being processed
                    }else {


                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }

                    }
                });




    }




    public class SendMessageRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            loadingData=true;

            String username = (String)params[0];
            String smscode= (String)params[1];
            String loginstring= (String)params[2];
            String deviceid= (String)params[3];
            String ipaddress = (String)params[4];
            String platform= (String)params[5];
            String version= (String)params[6];




            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/smsverify.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("smscode", smscode);
                postDataParams.put("loginstring", loginstring);
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


            pb.setVisibility(View.GONE);

            loadingData=false;


            Log.e("onotpverifyresults",result);

          getInfo(result);

            // Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();


        }
    }


    public class resendTokenRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            loadingData=true;

            String username = (String)params[0];

            String loginstring= (String)params[1];
            String deviceid= (String)params[2];
            String ipaddress = (String)params[3];
            String platform= (String)params[4];
            String version= (String)params[5];




            try {

                URL url = new URL("https://www.wayawaya.com/chamah_front/smsverifyjson.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);

                postDataParams.put("loginstring", loginstring);
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

            pb.setVisibility(View.GONE);

            loadingData=false;


            Log.e("onotpverifyresults",result);

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
            if (jsonObject.getString(KEY_SUCCESS).equals("1")) {
//REQUEST WAS PROCESSED SUCCESFULLY

                // 1. create an intent pass class name or intnet action name
                Intent intent = new Intent(account_verify.this, choosename_signup.class);

                // 2. put key/value data
                intent.putExtra("useridmain", useridGlob);




                // 3. or you can add data to a bundle
                Bundle extras = new Bundle();
                extras.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent.putExtras(extras);

                // 5. start the activity
                startActivity(intent);



            }  else {
                //signup process had an Error

                String error =jsonObject.getString(KEY_ERROR) ;




                AlertDialog alertDialog = new AlertDialog.Builder(
                        account_verify.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Verification Error");

                // Setting Dialog Message
                alertDialog.setMessage(error);


                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Re-try", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed




                        // Toast.makeText(getApplicationContext(), "Continuing ", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(account_verify.this, loginscreen.class);

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



}
