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
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 26/06/2018.
 */

public class add_card extends AppCompatActivity {

    private EditText card_number,card_cvv,card_expiry;

    private ProgressBar  pb;


    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;






    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error_message";

    private final String KEY_MSG = "message";



    String currencyGlob="";

    String useridGlob="";

    String  deviceidthis="";

    public static final String MyPREFERENCES = "ww_prefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);

        pb = (ProgressBar) findViewById(R.id.addProgress);

        pb.setVisibility(View.GONE);


        card_number=(EditText) findViewById(R.id.cardnumber_text);

        card_cvv=(EditText) findViewById(R.id.cvv_text);

        card_expiry=(EditText) findViewById(R.id.expiry_text);


        getSupportActionBar().setTitle("Add Card"); // for set actionbar title

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar










        findViewById(R.id.add_card_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {
                            //show alert dialog


                            if (TextUtils.isEmpty(card_number.getText().toString())) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        add_card.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Please enter Valid Card Number");

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
                            } else if (TextUtils.isEmpty(card_expiry.getText().toString()) ) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        add_card.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Please enter Valid Card Expiry Date in the Format MMYY");

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
                            } else if (TextUtils.isEmpty(card_cvv.getText().toString()) ) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        add_card.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Please enter a Valid Card Verification Code (The last 3 Digits at the Back of your Credit/Debit Card");

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

                                String token_this = "dsaerw";

                                String getSimSerialNumber = "NULL";
                                String getSimNumber = "NULL";

                                pb.setVisibility(View.VISIBLE);

                                //error=false;     String username = (String)params[0];

                                String request_level = "attach_card_internal";

                                String token= "";



                                new SendMessageRequest().execute(useridGlob,card_number.getText().toString(), card_expiry.getText().toString(), card_cvv.getText().toString(), request_level,token, deviceidthis, ipaddress, countryCode, device_platform, app_version, carrierName, manufacturer, model, getSimSerialNumber, getSimNumber, token_this);

                            }
//end of no other request is being processes
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
            String cardnumber= (String)params[1];
            String cardexpiry= (String)params[2];
            String cardcvv= (String)params[3];
            String request_level = (String)params[4];

            String token= (String)params[5];
            String deviceid = (String)params[6];
            String ipaddress= (String)params[7];
            String devicecountry= (String)params[8];
            String platform= (String)params[9];
            String app_version= (String)params[10];
            String telco = (String)params[11];
            String devicemanufacturer= (String)params[12];
            String devicemodel= (String)params[13];
            String sim_serial= (String)params[14];
            String sim_number= (String)params[15];



            try {

                URL url = new URL("https://www.wayawaya.com/chamah_front/attach_card.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("card_number", cardnumber);
                postDataParams.put("card_expiry", cardexpiry);
                postDataParams.put("card_cvv", cardcvv);
                postDataParams.put("request_level", request_level);

                postDataParams.put("token", token);

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


    public void getInfo(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals(1)) {
//REQUEST WAS PROCESSED SUCCESFULLY


                AlertDialog alertDialog = new AlertDialog.Builder(
                        add_card.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Account Created");

                // Setting Dialog Message
                alertDialog.setMessage("You have attached Account Successfully, You can Continue to Verify Account before using it");


                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Verify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed

                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(add_card.this, account_verify.class);

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
                        Intent intent = new Intent(add_card.this, loginscreen.class);

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



                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        add_card.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Failed Request");

                // Setting Dialog Message
                alertDialog.setMessage("Their Was an Error Processing Request :"+error);

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
