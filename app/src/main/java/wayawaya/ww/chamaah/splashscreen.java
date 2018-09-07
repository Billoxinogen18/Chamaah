package wayawaya.ww.chamaah;

/**
 * Created by teddyogallo on 12/06/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by teddyogallo on 04/02/2018.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashscreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;



    String userprefGlob="";


    public static final String MyPREFERENCES = "ww_prefs" ;
    public static final String Username = "usernamepref";

    SharedPreferences sharedpreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        SharedPreferences  mySharedPreferences ;
        mySharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Retrieve the saved values.

        String usernamepref = mySharedPreferences.getString("usernamepref", "");



        userprefGlob=usernamepref;



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(userprefGlob.length() == 0)
                {
                    Intent i = new Intent(splashscreen.this, loginscreen.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }else
                {
                    Intent i = new Intent(splashscreen.this, MainActivitySearch.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }




            }
        }, SPLASH_TIME_OUT);
    }

}