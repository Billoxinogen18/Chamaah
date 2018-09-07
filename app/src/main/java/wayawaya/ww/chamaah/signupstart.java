package wayawaya.ww.chamaah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
 * Created by teddyogallo on 20/06/2018.
 */

public class signupstart extends AppCompatActivity {

    private EditText emailtext,phonetext,password,password2;
    private TextView country_code_label;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;



    private ArrayList<chatSummaryMethods> chatGlobalHistory;

    private ArrayList<ChatMessage> chatPayRequestHistory;


    String useridGlob="";

    String deviceidthis="";

    String usernameGlob="";

    String country="";

    String countrycodeM;

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



    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;


    String[] arraySpinner = new String[] {
            "KENYA", "USA", "UGANDA", "SOUTH AFRICA", "NIGERIA"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupstart);

        pb=(ProgressBar)findViewById(R.id.signupProgress);

        pb.setVisibility(View.GONE);

        final Spinner countryspinnerthis;

        countryspinnerthis = (Spinner)findViewById(R.id.country_name);

        countryspinnerthis.setPrompt("Select Country");



//the error here doest seem to be valid
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spin‌​ner_dropdown_item);
        countryspinnerthis.setAdapter(adapter);


        //end of the ero above doesnt seem to be valid




        emailtext=(EditText) findViewById(R.id.email_address);

        phonetext=(EditText) findViewById(R.id.phone_number);



        password=(EditText) findViewById(R.id.password);

        password2=(EditText) findViewById(R.id.password2);

        country_code_label=(TextView)findViewById(R.id.country_code_label);





        getSupportActionBar().setTitle("Create Account"); // for set actionbar title

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar





        countryspinnerthis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (loadingData == false)    {
                    // RUN THE ASYNCTASK AGAIN


                    Object item = parent.getItemAtPosition(pos);

                    country= countryspinnerthis.getSelectedItem().toString();

                    TextView selectedText = (TextView) parent.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                    }

                    if(country.equals("USA")){


                        //phonetext.setText("1");

                        country_code_label.setText("+1");

                        countrycodeM="1";

                    }else if(country.equals("KENYA")){


                        country_code_label.setText("+254");
                        //phonetext.setText("254");

                        countrycodeM="254";

                    }else if(country.equals("SOUTH AFRICA")){

                        country_code_label.setText("+27");
                        //phonetext.setText("27");

                        countrycodeM="27";

                    }
                    else if(country.equals("NIGERIA")){

                        country_code_label.setText("+234");
                        //phonetext.setText("234");

                        countrycodeM="234";

                    }else if(country.equals("UGANDA")){

                        country_code_label.setText("+256");
                        //phonetext.setText("234");

                        countrycodeM="234";

                    }



                    //end of loading data is false
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //get currency selected item



        findViewById(R.id.signup_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(loadingData==false) {
                            //show alert dialog


                        if (TextUtils.isEmpty(emailtext.getText().toString()) || TextUtils.isEmpty(phonetext.getText().toString())) {


                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    signupstart.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please enter email and phone number to Continue");

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
                        } else if( country .equals("Country")||TextUtils.isEmpty(country)){



                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    signupstart.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please select country to Continue");

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


                            }else if ( TextUtils.isEmpty(password.getText().toString())) {


                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    signupstart.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please enter Password to Continue");

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
                        } else if (!password.getText().toString().equals(password2.getText().toString())) {


                            //show alert dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    signupstart.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Input Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("The Password entered doe not match with Confirmation password, please enter similar values to continue");

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

                            //error=false;




                            String phone_number_final=countrycodeM+""+phonetext.getText().toString();

                            useridGlob=phone_number_final;


                            new SendMessageRequest().execute(phone_number_final, emailtext.getText().toString(), password.getText().toString(), country, deviceidthis, ipaddress, countryCode, device_platform, app_version, carrierName, manufacturer, model, getSimSerialNumber, getSimNumber, token_this);

                        }
//end of no other request is being processes
                    }else {



                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }


                    }
                });






    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(signupstart.this, loginscreen.class);



                startActivity(intent);


                break;
        }
        return true;
    }




    public class SendMessageRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            loadingData=true;

            String username = (String)params[0];
            String email= (String)params[1];
            String password= (String)params[2];
            String country= (String)params[3];
            String deviceid = (String)params[4];
            String ipaddress= (String)params[5];
            String devicecountry= (String)params[6];
            String platform= (String)params[7];
            String app_version= (String)params[8];
            String telco = (String)params[9];
            String devicemanufacturer= (String)params[10];
            String devicemodel= (String)params[11];
            String sim_serial= (String)params[12];
            String sim_number= (String)params[13];
            String token= (String)params[14];



            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/chamahsignup_start_json.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("email", email);
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


    public ArrayList<ChatMessage> getInfo(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(KEY_SUCCESS).equals("1")) {
//REQUEST WAS PROCESSED SUCCESFULLY


                Intent intent = new Intent(signupstart.this, account_verify.class);

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
                        signupstart.this).create();

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
                        Intent intent = new Intent(signupstart.this, account_verify.class);

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
                        Intent intent = new Intent(signupstart.this, loginscreen.class);

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
                        signupstart.this).create();

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
                        Intent intent = new Intent(signupstart.this, loginscreen.class);

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
