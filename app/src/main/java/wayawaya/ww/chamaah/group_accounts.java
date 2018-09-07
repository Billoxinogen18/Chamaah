package wayawaya.ww.chamaah;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by teddyogallo on 11/06/2018.
 */

public class group_accounts extends AppCompatActivity {

    RelativeLayout notificationCount1;


    String useridGlob="";

    String usernameGlob="";

    String currencyGlob="";

    String conversationidGlob="";

    public static final String MyPREFERENCES = "ww_prefs" ;



    private ProgressBar pb;
    private boolean error=false;

    private boolean loadingData = false;


    final Context context = this;


    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_accounts);



        notificationCount1 = (RelativeLayout) findViewById(R.id.notification_layout);

        getSupportActionBar().setTitle("Account"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar











        findViewById(R.id.virtual_card_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        AlertDialog alertDialog = new AlertDialog.Builder(
                                group_accounts.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Alert");

                        // Setting Dialog Message
                        alertDialog.setMessage("Virtual Wallet account doesnt have sufficient Funds to Continue Please link a Debit/Credit Card Account or MPESA Account");


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Add Card", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                // 1. create an intent pass class name or intnet action name
                                Intent intent = new Intent(group_accounts.this, add_card.class);

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



                                                        new addMpesaRequest().execute(usernameGlob,mpesa_no_this,mpesa_no_this);




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
                });




        findViewById(R.id.request_loan_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                group_accounts.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Alert");

                        // Setting Dialog Message
                        alertDialog.setMessage("Account does not have a sufficient Loan Limit, To start add a Bank or MPESA account and start transacting");


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Add Card", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                // 1. create an intent pass class name or intnet action name
                                Intent intent = new Intent(group_accounts.this, add_card.class);

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



                                                        new addMpesaRequest().execute(usernameGlob,mpesa_no_this,mpesa_no_this);




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
                });





        findViewById(R.id.repay_loan_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Snackbar.make(view, "You do not have any Active Loans", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                });





        findViewById(R.id.savings_setup).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Link Either Mpesa or Card Account


                        AlertDialog alertDialog = new AlertDialog.Builder(
                                group_accounts.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Alert");

                        // Setting Dialog Message
                        alertDialog.setMessage("To Continue Please link a Debit/Credit Card Account or MPESA Account");


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_launcher);

                        // Setting OK Button
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Add Card", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                // 1. create an intent pass class name or intnet action name
                                Intent intent = new Intent(group_accounts.this, add_card.class);

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



                                                        new addMpesaRequest().execute(usernameGlob,mpesa_no_this,mpesa_no_this);





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



                    }
                });









        //end of oncreate

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.messages_top_menu, menu);

       // MenuItem item1 = menu.findItem(R.id.actionbar_item);
       // MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
       // notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(group_accounts.this, MainActivitySearch.class);



                startActivity(intent);


                break;
        }
        return true;
    }

    public class addMpesaRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... params) {

            String username = (String)params[0];
            String mpesa_no= (String)params[1];

            String message_id= (String)params[2];


            try {

                URL url = new URL("https://www.wayawaya.co.ke/chamah_front/add_mpesa_request.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", username);
                postDataParams.put("mpesa_no", mpesa_no);

                postDataParams.put("message_id", message_id);

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



    public void getInfoAddMpesa(String response) {
        ArrayList<ChatMessage> playersModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String return_desc=jsonObject.getString("desc");
            if (jsonObject.getString(KEY_SUCCESS).equals("true")) {


                //show alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(
                        group_accounts.this).create();

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
                        group_accounts.this).create();

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


}
