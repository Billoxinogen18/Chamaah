package wayawaya.ww.chamaah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import javax.net.ssl.HttpsURLConnection;

public class new_group extends AppCompatActivity {

    private EditText group_name,group_description, target_amount;

    ToggleButton loan_option_button;

    Spinner settlement_account;


    String useridGlob="";

    String groupridGlob="";

    String groupnameGlob="";

    String deviceidthis="";

    String usernameGlob="";

    String select_accountidGlob="";

    String chatidGlob="Friend";

    String strSeperator="|#|";

    String strSeperatorMain="@%@";


    private ProgressBar pb;

    private ImageView profile_image;
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

    int GET_FROM_GALLERY=1;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group);

        group_name = (EditText) findViewById(R.id.groupname_textfield);



        group_description = (EditText) findViewById(R.id.group_description);

        target_amount = (EditText) findViewById(R.id.target_amount_text);

        loan_option_button=(ToggleButton) findViewById(R.id.loan_option_button);

        settlement_account=(Spinner) findViewById(R.id.settlement_account_option);

        pb = (ProgressBar) findViewById(R.id.group_progressBar);

        pb.setVisibility(View.GONE);


        getSupportActionBar().setTitle("New Group"); // for set actionbar title

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

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






        findViewById(R.id.savegroup_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        if(loadingData==false) {
                            //show alert dialog

                            if (TextUtils.isEmpty(group_name.getText().toString())||TextUtils.isEmpty(group_description.getText().toString())) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        new_group.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Group Name and Group Descriptions cannot be empty");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.ic_launcher);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        Toast.makeText(getApplicationContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                                // Toast.makeText(this, "please enter Userid", Toast.LENGTH_LONG).show();
                            }else if (TextUtils.isEmpty(target_amount.getText().toString())) {


                                //show alert dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        new_group.this).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Input Error");

                                // Setting Dialog Message
                                alertDialog.setMessage("Target Amount Cannot be Empty");

                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.ic_launcher);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        Toast.makeText(getApplicationContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
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


                                ToggleButton simpleToggleButton = (ToggleButton) findViewById(R.id.loan_option_button); // initiate a toggle button
                                Boolean ToggleButtonState = simpleToggleButton.isChecked(); // check current state of a toggle button (true or false).


                                String loan_option="YES";
                                if(!ToggleButtonState)
                                {
                                    loan_option="NO";
                                }
                                else
                                {
                                    loan_option="YES";
                                }




                                groupnameGlob=group_name.getText().toString();



                                new SendMessageRequest().execute(useridGlob,loginstring_this, group_name.getText().toString(),group_description.getText().toString(),target_amount.getText().toString(),loan_option, select_accountidGlob,  deviceidthis, ipaddress, app_version);






                            }

//end of no other request is being processed
                        }else {


                            Toast.makeText(getApplicationContext(), "Please wait while current request is completed", Toast.LENGTH_SHORT).show();


                        }





                    }
                });







        //end of on create options
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(new_group.this, groupslist.class);



                startActivity(intent);


                break;








        }
        return true;
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



    public class SendMessageRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            loadingData=true;

            String username = (String)params[0];
            String logintoken= (String)params[1];
            String group_name= (String)params[2];

            String group_description= (String)params[3];
            String limit_amount= (String)params[4];
            String loan_to_members = (String)params[5];
            String settle_account_id= (String)params[6];

            String device_id= (String)params[7];
            String device_ip= (String)params[8];
            String app_version= (String)params[9];




            try {

                URL url = new URL("https://www.wayawaya.co.ke/chama_registergroupjson.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("logintoken", logintoken);
                postDataParams.put("group_name", group_name);
                postDataParams.put("description", group_description);
                postDataParams.put("limit_amount", limit_amount);
                postDataParams.put("loan_to_members_status", loan_to_members);
                postDataParams.put("settlement_account_id", settle_account_id);

                postDataParams.put("device_id", device_id);
                postDataParams.put("device_ip", device_ip);
                postDataParams.put("limit_status", "NULL");
                postDataParams.put("rotational_status", "NULL");
                postDataParams.put("rotational_type", "NULL");






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

                //parse value for group id

                String group_id_this = jsonObject.getString("group_id");


                groupridGlob=group_id_this;


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        new_group.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Success ");

                // Setting Dialog Message
                alertDialog.setMessage("New Group Created");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed



                        // 1. create an intent pass class name or intnet action name
                        Intent intent = new Intent(new_group.this, setgroup_profile_image.class);

                        // 2. put key/value data
                        intent.putExtra("useridmain", useridGlob);

                        intent.putExtra("group_id", groupridGlob);

                        intent.putExtra("group_name", groupnameGlob);




                        // 3. or you can add data to a bundle
                        Bundle extras = new Bundle();
                        extras.putString("status", "useridvariablereceived!");

                        // 4. add bundle to intent
                        intent.putExtras(extras);

                        // 5. start the activity
                        startActivity(intent);





                        //Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();





            }  else {
                //signup process had an Error

                String error =jsonObject.getString(KEY_ERROR) ;




                AlertDialog alertDialog = new AlertDialog.Builder(
                        new_group.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Setup Error");

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
                        Intent intent = new Intent(new_group.this, loginscreen.class);

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
