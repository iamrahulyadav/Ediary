package crafty.schooldiary.diary.e.student.craftystudio.ediary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import utils.DatabaseHandler;
import utils.NoticeDetail;

public class DailyDiaryActivity extends AppCompatActivity {

    ListView noticeListView;
    ArrayAdapter<String> noticeListAdapter;
    public ArrayList<String> noticeStringList =new ArrayList<>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openInputDialogue();
            }
        });



        noticeListAdapter = new ArrayAdapter<String>(DailyDiaryActivity.this,android.R.layout.simple_list_item_1 ,noticeStringList  );

        noticeListView = (ListView)findViewById(R.id.dailydiaryListView);
        noticeListView.setAdapter(noticeListAdapter);

        DatabaseHandler databasehandler =new DatabaseHandler();
        databasehandler.getDailyComplainList(10);
        databasehandler.addNoticeListListner(new DatabaseHandler.DataBaseHandlerNoticeListner() {
            @Override
            public void onNoticeList(ArrayList<NoticeDetail> noticeDetailArrayList) {
                Toast.makeText(DailyDiaryActivity.this, ""+noticeDetailArrayList.size(), Toast.LENGTH_SHORT).show();

                for (int i = noticeDetailArrayList.size()-1 ;i>=0 ; i-- )
                {
                    noticeStringList.add(noticeDetailArrayList.get(i).getNoticeMessage());
                }


                noticeListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNoticePost(boolean isSuccessful) {

            }
        });


    }



    private void openInputDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.notice_add_dialoguebox_layout, (ViewGroup) findViewById(R.id.content_notice), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                postNoticeToFireBase(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void postNoticeToFireBase(String s) {
        NoticeDetail noticeDetail = new NoticeDetail();
        noticeDetail.setDate("");
        noticeDetail.setNoticeMessage(s);
        DatabaseHandler databasehandler = new DatabaseHandler();
        databasehandler.postDailyComplain(noticeDetail);
        databasehandler.addNoticeListListner(new DatabaseHandler.DataBaseHandlerNoticeListner() {
            @Override
            public void onNoticeList(ArrayList<NoticeDetail> noticeDetailArrayList) {

            }

            @Override
            public void onNoticePost(boolean isSuccessful) {

                Toast.makeText(DailyDiaryActivity.this, "Complain posted "+isSuccessful, Toast.LENGTH_SHORT).show();

            }
        });
    }


}
