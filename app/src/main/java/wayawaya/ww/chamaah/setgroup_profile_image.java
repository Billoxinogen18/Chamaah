package wayawaya.ww.chamaah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

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

public class setgroup_profile_image extends AppCompatActivity {


    public final static String APP_PATH_SD_CARD = "/chamaah_f/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private TextView group_name;

    ToggleButton loan_option_button;

    Spinner settlement_account;


    String useridGlob="";

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

    String groupridGlob="";

    String groupnameGlob="";

    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;

    int GET_FROM_GALLERY=1;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setgroup_profile_image);

        group_name = (TextView) findViewById(R.id.group_name_label);

        profile_image = (ImageView) findViewById(R.id.profile_picture);



        pb = (ProgressBar) findViewById(R.id.group_progressBar);

        pb.setVisibility(View.GONE);


        getSupportActionBar().setTitle("Edit Profile"); // for set actionbar title

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







        Intent intent = getIntent();


        // 2. get message value from intent

        String groupidgot = intent.getStringExtra("group_id");

        String groupnamegot = intent.getStringExtra("group_name");




        groupridGlob=groupidgot;

        groupnameGlob=groupnamegot;



        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        group_name.setText(groupnamegot);



        usernameGlob=usernamepref;
        useridGlob=usernamepref;
        currencyGlob=usercurrencypref;








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




        findViewById(R.id.savegroup_button).setOnClickListener(
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

                                //prepare image data here

                                BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();

                                Bitmap bitmap = drawable.getBitmap();

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                                byte[] byteArray = baos.toByteArray();

                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                //end of prepare image data here

                            //save image to internbal storage here


                            //end of save image to internal storage

                            saveImageToInternalStorage(bitmap,groupridGlob);



                            //new SendMessageRequest().execute(useridGlob,loginstring_this, group_name.getText().toString(),group_description.getText().toString(),target_amount.getText().toString(),loan_option, select_accountidGlob, encodedImage, deviceidthis, ipaddress, app_version);


                            pb.setVisibility(View.VISIBLE);

                            new onbuttonclickHttpPost().execute(useridGlob,loginstring_this,groupridGlob,encodedImage,encodedImage);




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


                Intent intent = new Intent(setgroup_profile_image.this, groupslist.class);



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




    private class onbuttonclickHttpPost extends AsyncTask<String, Integer, String> {

        String result = "";
        String request="";
        String RphoneGlob="";
        String RamountGlob="";
        String RcountryGlob="";

        ProgressDialog Asycdialog = new ProgressDialog(setgroup_profile_image.this);

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

            String Ruserid = (String)params[0];
            String Rtoken = (String)params[1];
            String Rgroupid = (String)params[2];
            String Rimagepayload = (String)params[3];
            String Rimagename = (String)params[4];







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
            HttpPost httppost = new HttpPost("https://www.wayawaya.co.ke/chamah_front/chama_group_image_add.php");
            try {

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("userid", Ruserid));
                nameValuePairs.add(new BasicNameValuePair("token", Rtoken));
                nameValuePairs.add(new BasicNameValuePair("group_id", Rgroupid));

                nameValuePairs.add(new BasicNameValuePair("image_payload", Rimagepayload));
                nameValuePairs.add(new BasicNameValuePair("image_name", Rimagename));

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
                        setgroup_profile_image.this).create();

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
                        setgroup_profile_image.this).create();

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

                        String groupid_this = jsonObject.getString("group_id");



                        //((mimiappmain) this.getApplication()).setTransactionid(transactionid);






                        Intent intent = new Intent(setgroup_profile_image.this, groupslist.class);

                        // 2. put key/value data
                        intent.putExtra("useridmain", useridGlob);

                        intent.putExtra("groupd", groupid_this );




                        // 3. or you can add data to a bundle
                        // Bundle extras = new Bundle();
                        //extras.putString("status", "useridvariablereceived!");

                        // 4. add bundle to intent
                        //intent.putExtras(extras);

                        startActivity(intent);



                    }
                    else if(successStatus.equals("2")){

                        String errorStatus = jsonObject.getString("message");
                        //Toast.makeText(getApplicationContext(), "Login Error:"+errorStatus+"userid:"+useridthis+"password:"+passwordthis+"token:"+tokenthis+"deviceid:"+deviceidthis, Toast.LENGTH_LONG).show();

                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                setgroup_profile_image.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Image Upload Failed");

                        // Setting Dialog Message
                        alertDialog.setMessage(""+errorStatus+" ");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                error = false;
                                pb.setVisibility(View.VISIBLE);
                                //new logoutaction().execute(userGlob);


                                Toast.makeText(getApplicationContext(), "Please loggin and try again ", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                        //verify account to continue


                    }

                    else{
                        //get error code
                        String errorStatus = jsonObject.getString("message");
                        //Toast.makeText(getApplicationContext(), "Login Error:"+errorStatus+"userid:"+useridthis+"password:"+passwordthis+"token:"+tokenthis+"deviceid:"+deviceidthis, Toast.LENGTH_LONG).show();

                        //show alert dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                setgroup_profile_image.this).create();

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
        try {
            if (this.isSdReadable() == true) {
                thumbnail = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("img on external storage", e.getMessage());
        }

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
