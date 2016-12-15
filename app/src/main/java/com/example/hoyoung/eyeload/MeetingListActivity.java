package com.example.hoyoung.eyeload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-10-8.
 */

public class MeetingListActivity extends AppCompatActivity {

    private MeetingControl control;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = MeetingControl.getInstance();

        setTitle("모임 목록");
        setContentView(R.layout.activity_meeting_list);


        setupListView(); // List내용을 xml에 추가하는 부분

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Activity 안의 버튼이 눌린 경우
                Intent intent = new Intent(MeetingListActivity.this,MakingMeetingActivity.class);

                startActivity(intent);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setupListView();

    }

    //List내용을 xml에 추가하는 부분
    public void setupListView()
    {

        SelectAllMeeting selectAllMeeting = new SelectAllMeeting();
        selectAllMeeting.execute();

    }

    // Meeting list 선택 작동하는 부분
    public void meetingClicked() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MeetingListActivity.this,MeetingInfoActivity.class);
                intent.putExtra("meetingKey",String.valueOf(control.getMeetingList().get(position).getKey()));
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(listener);
    }

    //모든 Meeting을 UI에 적용하기 위한 쓰레드
    class SelectAllMeeting extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MeetingListActivity.this);
            loading.setMessage("불러오는 중입니다.");

            listView = (ListView) findViewById(R.id.meetingListview1);

            control = MeetingControl.getInstance();

            loading.show();
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            loading.dismiss();

            if(flag == true) {
                listView.setAdapter(control);
                meetingClicked();
            }
            else
                Toast.makeText(getApplicationContext(), "모임 검색 실패!", Toast.LENGTH_LONG).show();

        }


        @Override
        protected Boolean doInBackground(Void ... params) {

            return control.getAllMeeting();// DB에서 Meeting에 관한 모든 정보를 가져옴

        }
    }
}
