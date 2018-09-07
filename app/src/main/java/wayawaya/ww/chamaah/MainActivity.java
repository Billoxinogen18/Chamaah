package wayawaya.ww.chamaah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<chat_summary> contentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContentAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    private ArrayList<chatSummaryMethods> chatGlobalHistory;

    RelativeLayout notificationCount1;

    TextView name_label;


    String useridGlob="";

    String usernameGlob="";

    String currencyGlob="";

    String conversationidGlob="";

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get saved values in preference file

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

        //endof get saved values in preference file

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                conversationidGlob="New Message";




                Intent intent = new Intent(MainActivity.this, ChatActivity.class);

// 2. put key/value data


                intent.putExtra("conversationidmain", conversationidGlob);





                // 3. or you can add data to a bundle
                Bundle extras = new Bundle();
                extras.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent.putExtras(extras);

                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        //for recycler view here

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        notificationCount1 = (RelativeLayout) findViewById(R.id.notification_layout);

        //TextView nav_namelabel = navigationView.findViewById(R.id.username_label);



        getSupportActionBar().setTitle("Welcome"); // for set actionbar title

        //nav_namelabel.setText(usernameGlob);


        contentList=new ArrayList<>();
        mAdapter = new ContentAdapter(contentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        //contentList=getGlobalArrayList();

  prepareContentData();

        //loadContentData();


        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));


// set the adapter
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

/*
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

*/
        //end for recycler view


        //on touch adapter connecting recyclerTouchListener.java

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                chat_summary content_chat = contentList.get(position);
                //Toast.makeText(getApplicationContext(), content_chat.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                conversationidGlob=content_chat.getTitle() ;




                Intent intent = new Intent(MainActivity.this, ChatActivity.class);

// 2. put key/value data


                intent.putExtra("conversationidmain",  conversationidGlob);



                // 3. or you can add data to a bundle
                Bundle extras = new Bundle();
                extras.putString("status", "useridvariablereceived!");

                // 4. add bundle to intent
                intent.putExtras(extras);

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //end of On Touch
    }

    /**
     * method make volley network call and parses json
     */

    /*
    private void prepareData() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<chat_summary> items = new Gson().fromJson(response.toString(), new TypeToken<List<chat_summary>>() {
                        }.getType());

                        // adding items to cart list
                        contentList.clear();
                        contentList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

*/


    private void prepareContentData() {


        //contentList = getGlobalArrayList();

        //update from online sources
        // chatHistory =getInfo(useridGlob);


            chat_summary content = new chat_summary("Teddy Ogallo", "Hi i am testing out this chat today ", "20:53", 1234, "null", 2);
            contentList.add(content);


            content = new chat_summary("Teddy Angir", "I also tried craeting this groups", "20:35", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Yvonne's Taxi Business", "Our Incomes for the day are KES 10000", "20:30", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Brian Aboso", "I am traveling to Uganda", "20:07", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Bill Israel", "Please help me out here", "19:05", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Mum", "My Son Send me Money", "19:01", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Dad", "Hallo", "18:16", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Yvonne Olwang", "Please Help me with this items", "17:24", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Amari Ogalo", "Hey Dad, Please come pick me up", "16:14", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Irene", "Hi Your Documents are ready", "Yesterday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Collins Odhiambo", "Hey Bro, I finished the registration", "Monday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Shosho", "Please Call here", "Sunday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Mechanic", "Not Finished", "Saturday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("WayaWaya", "Transaction Completed", "Friday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Chamaah Help", "Please Respond", "Friday", 1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Chamaah", "Thank you for Joining this service", "30/05/2018", 1234, "null", 2);
            contentList.add(content);


            //saveGlobalArrayList(contentList);

           // mAdapter.notifyDataSetChanged();


        mAdapter.notifyDataSetChanged();


    }


    private void loadContentData() {

        //contentList = new ArrayList<chat_summary>();


        //update from online sources
        // chatHistory =getInfo(useridGlob);

        contentList = getGlobalArrayList();

        //update from online sources
        // chatHistory =getInfo(useridGlob);

        if (contentList != null && !contentList.isEmpty()) {


            chat_summary content = new chat_summary("Teddy Okello", "Hi i am testing out this chat today ", "20:53", 1234, "null", 2);
            contentList.add(content);

            chat_summary content2 = new chat_summary("Teddy Otieno", "How its saving data ", "20:53", 1234, "null", 2);

            contentList.add(content2);


            saveGlobalArrayList(contentList);

            mAdapter.notifyDataSetChanged();

        }else {

            contentList = new ArrayList<chat_summary>();

            chat_summary content = new chat_summary("Teddy Ogallo", "Hi i am testing out this chat today ", "20:53",1234, "null", 2);
            contentList.add(content);



            content = new chat_summary("Teddy Angir", "I also tried craeting this groups", "20:35",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Yvonne's Taxi Business", "Our Incomes for the day are KES 10000", "20:30",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Brian Aboso", "I am traveling to Uganda", "20:07",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Bill Israel", "Please help me out here", "19:05",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Mum", "My Son Send me Money", "19:01",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Dad", "Hallo", "18:16",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Yvonne Olwang", "Please Help me with this items", "17:24",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Amari Ogalo", "Hey Dad, Please come pick me up", "16:14",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Irene", "Hi Your Documents are ready", "Yesterday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Collins Odhiambo", "Hey Bro, I finished the registration", "Monday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Shosho", "Please Call here", "Sunday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Mechanic", "Not Finished", "Saturday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("WayaWaya", "Transaction Completed", "Friday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Chamaah Help", "Please Respond", "Friday",1234, "null", 2);
            contentList.add(content);

            content = new chat_summary("Chamaah", "Thank you for Joining this service", "30/05/2018",1234, "null", 2);
            contentList.add(content);



            // saveGlobalArrayList(contentList, useridGlob);

            mAdapter.notifyDataSetChanged();


        }



    }


    public void saveGlobalArrayList(List<chat_summary> list){

        String key="chmahh";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<chat_summary> getGlobalArrayList(){

        String key="chmahh";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
    }






    public String update_global(String usertitle, String content_this, int notification_no, String year_this, String profile_thumbnail){



        contentList = new ArrayList<chat_summary>();


        //update from online sources
        // chatHistory =getInfo(useridGlob);

        //chatGlobalHistory = getGlobalArrayList();

        //update from online sources
        // chatHistory =getInfo(useridGlob);

        if (contentList != null && !contentList.isEmpty()) {



            for (chat_summary row : contentList) {
                String userIdThis=row.getTitle();

                int index_this = contentList.indexOf(row);

                if (userIdThis.equals(usertitle) ) {
                    //read contents here
                    int id_this=row.getId();

                    String message_this=row.getContent();
                    String Date_this=row.getYear();
                    String profile_url_this=row.getThumbnail();
                    Long notifications_num=row.getnotificationsNum();

                    //create new values here

                    chat_summary content = new chat_summary(usertitle, content_this, year_this,id_this, profile_thumbnail, notification_no);
                    //contentList.add(content);

                    contentList.set( index_this, content );

                    saveGlobalArrayList(contentList);

                    contentList.clear();

                }
            }



        }else {
            //chat history is  null


            contentList = new ArrayList<chat_summary>();

            int id_random=123;


            chat_summary content = new chat_summary(usertitle, content_this, year_this,id_random, profile_thumbnail, notification_no);
            //contentList.add(content);

            contentList.add(  content );

            saveGlobalArrayList(contentList);



            contentList.clear();



        }




        return  null;


    }

    /*
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContentAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = contentList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final chat_summary deletedItem = contentList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //getMenuInflater().inflate(R.menu.messages_top_menu, menu);

        MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);

        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
     int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.groups_button) {
            // Handle the camera action


            Intent intent = new Intent(MainActivity.this, groupslist.class);



            startActivity(intent);

        } else if (id == R.id.savings_button) {


            Intent intent = new Intent(MainActivity.this, savings_loans.class);



            startActivity(intent);

        } else if (id == R.id.preferences_button) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.log_out_button) {



            //option to log out user accounts

            //remove all preference data

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString(Username, "");
            editor.putString(Usercurrency, "");
            editor.putString(Userlogintype, "");
            editor.putString(Userrate, "");



            editor.commit();

            //check if facebook is logged in

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


            if(isLoggedIn==true){


                LoginManager.getInstance().logOut();



            }


            //switch to login screen


            Intent intent = new Intent(MainActivity.this, loginscreen.class);



            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
