package wayawaya.ww.chamaah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class load_contacts extends AppCompatActivity {

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

    String conversationnameGlob="";

    String conversationimageGlob="";

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
        setContentView(R.layout.load_contacts);



        //get saved values in preference file

        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Retrieve the saved values.

        String usernamepref = mySharedPreferences.getString("usernamepref", "");
        String usercurrencypref = mySharedPreferences.getString("usercurrencypref", "");

        String userlogintypepref = mySharedPreferences.getString("userlogintypepref", "");
        String usersocialidpref = mySharedPreferences.getString("usersocialidpref", "");
        String usersocialpref = mySharedPreferences.getString("usersocialimagepref", "");


        usernameGlob = usernamepref;
        useridGlob = usernamepref;
        currencyGlob = usercurrencypref;




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



        getSupportActionBar().setTitle("New Message"); // for set actionbar title

        //nav_namelabel.setText(usernameGlob);


        contentList=new ArrayList<>();
        mAdapter = new ContentAdapter(contentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        //loadContentData();


        prepareContentData();


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

                int userid=content_chat.getId() ;

                conversationidGlob=""+userid ;

                conversationnameGlob=content_chat.getTitle() ;

                conversationimageGlob=content_chat.getThumbnail() ;







                Intent intent = new Intent(load_contacts.this, ChatActivity.class);

// 2. put key/value data


                intent.putExtra("conversationidmain",  conversationidGlob);

                intent.putExtra("conversationnamemain",  conversationnameGlob);

                intent.putExtra("profileurlmain",   conversationimageGlob);





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






        ///end of oncreate
    }



    @Override
    public void onBackPressed() {
        // close search view on back button pressed


        Intent intent = new Intent(load_contacts.this, MainActivitySearch.class);

        // 3. or you can add data to a bundle
        Bundle extras = new Bundle();
        extras.putString("status", "useridvariablereceived!");

        // 4. add bundle to intent
        intent.putExtras(extras);

        startActivity(intent);



    }

    private void prepareContentData() {


        //contentList = getGlobalArrayList();

        //update from online sources
        // chatHistory =getInfo(useridGlob);




            chat_summary content = new chat_summary("chamaah", "Get Support ", "@chamaah", 1000003, "null", 2);
            contentList.add(content);


            //saveGlobalArrayList(contentList, save_id);

            // mAdapter.notifyDataSetChanged();




        mAdapter.notifyDataSetChanged();


    }



    private void loadContentData() {

        //contentList = new ArrayList<chat_summary>();


        //update from online sources
        // chatHistory =getInfo(useridGlob);

        String save_id = "contacts" + useridGlob;

        contentList = getGlobalArrayList(save_id);

        //update from online sources
        // chatHistory =getInfo(useridGlob);

        if (contentList != null && !contentList.isEmpty()) {


            chat_summary content = new chat_summary("Chamaah", "Send support messages ", "@chamaah", 1234, "null", 2);
            contentList.add(content);



            //saveGlobalArrayList(contentList,save_id);

            //mAdapter.notifyDataSetChanged();

        }else {

            contentList = new ArrayList<chat_summary>();

            chat_summary content = new chat_summary("Chamaah", "New support messages ", "@chamaah",1234, "null", 2);
            contentList.add(content);





            // saveGlobalArrayList(contentList, useridGlob);

            //mAdapter.notifyDataSetChanged();


        }

        mAdapter.notifyDataSetChanged();

    }


    public void saveGlobalArrayList(List<chat_summary> list,String key){



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<chat_summary> getGlobalArrayList(String key){



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<chat_summary>>() {}.getType();
        return gson.fromJson(json, type);
    }



}
