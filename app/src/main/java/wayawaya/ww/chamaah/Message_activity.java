package wayawaya.ww.chamaah;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by teddyogallo on 08/06/2018.
 */

public class Message_activity extends AppCompatActivity {

    private List<messagechat_summary> contentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private messageContentAdapter mAdapter;

    EditText message_box;

    Button sendBtn;

    Button extra_menu_button;

    RelativeLayout notificationCount1;

    String useridGlob="";

    String usernameGlob="";

    String chatidGlob="NewChatWW";



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        extra_menu_button=(Button) findViewById(R.id.side_menu);;
        notificationCount1 = (RelativeLayout) findViewById(R.id.notification_layout);
        sendBtn = (Button) findViewById(R.id.button_chatbox_send);

        message_box=(EditText) findViewById(R.id.edittext_chatbox);


        Intent intent = getIntent();


                // 2. get message value from intent
        String usernamegot = intent.getStringExtra("username");
        String useridgot = intent.getStringExtra("useridmain");



        usernameGlob=usernamegot;
        useridGlob=useridgot;




        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        if(!TextUtils.isEmpty(usernameGlob)) {

            chatidGlob=usernameGlob;
        }

        getSupportActionBar().setTitle(chatidGlob); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7ac4a3")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        contentList=new ArrayList<>();
        mAdapter = new messageContentAdapter(contentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        prepareContentData();




// set the adapter
        ///recyclerView.setAdapter(mAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = message_box.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                messagechat_summary chatMessage = new messagechat_summary();
                chatMessage.setId(122);//dummy
                chatMessage.setContent(messageText);
                chatMessage.setYear(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setTitle("self");

                message_box.setText("");

               // displayMessage(chatMessage);
            }
        });



        findViewById(R.id.side_menu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(Message_activity.this, extra_menu_button);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.message_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(Message_activity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });

                        popup.show();//showing popup menu


                    }
                });

//end of pop-up



        //on touch adapter connecting recyclerTouchListener.java

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                messagechat_summary content_chat = contentList.get(position);
                Toast.makeText(getApplicationContext(), content_chat.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




    }

    /*
    public void displayMessage(messagechat_summary message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }
*/

    //for pop up

    private void prepareContentData() {
        messagechat_summary content = new messagechat_summary("Teddy Ogallo", "Hi i am testing out this chat today ", "20:53");
        contentList.add(content);

        content = new messagechat_summary("Teddy Angir", "I also tried craeting this groups", "20:35");
        contentList.add(content);

        content = new messagechat_summary("Yvonne's Taxi Business", "Our Incomes for the day are KES 10000", "20:30");
        contentList.add(content);

        content = new messagechat_summary("Brian Aboso", "I am traveling to Uganda", "20:07");
        contentList.add(content);



        mAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages_top_menu, menu);

        MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        return super.onCreateOptionsMenu(menu);
    }


}