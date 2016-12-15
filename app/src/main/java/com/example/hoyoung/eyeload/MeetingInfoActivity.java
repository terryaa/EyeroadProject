package com.example.hoyoung.eyeload;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-11-5.
 */

public class MeetingInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private int key;
    private MeetingControl control;
    private TextView meeting_name_text;
    private TextView meeting_content_text;
    private TextView meeting_place_text;
    private TextView meeting_publisher_text;
    private AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this.getIntent());
        String meetingKey = intent.getStringExtra("meetingKey");
        key = Integer.valueOf(meetingKey);
        alert = new AlertDialog.Builder(MeetingInfoActivity.this);
        control = MeetingControl.getInstance(); // MeetingControl은 싱글톤
        setContentView(R.layout.activity_meeting_info);
        setTitle("모임 정보");
        findViewById(R.id.meeting_delete).setOnClickListener(this);
        findViewById(R.id.meeting_navi).setOnClickListener(this);
        meeting_name_text = (TextView)findViewById(R.id.meeting_name);
        meeting_content_text = (TextView)findViewById(R.id.meeting_content);
        meeting_place_text = (TextView)findViewById(R.id.meeting_place);
        meeting_publisher_text = (TextView)findViewById(R.id.meeting_publisher);
        SelectMeeting selectMeeting = new SelectMeeting();
        selectMeeting.execute(key);



    }

    public void onClick(View v) { // 메뉴의 버튼 선택 시 activity 이동
        switch (v.getId()) {
            case R.id.meeting_navi:
                Intent intent = new Intent(MeetingInfoActivity.this, SearchPlaceActivity.class);
                intent.putExtra("dest",control.getMeetingDTOSelected().getPlaceName() );
                Toast.makeText(getApplicationContext(), "출발지를 입력하시오.", Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
            case R.id.meeting_delete:
                deleteMeeting();
        }
    }

    public void navigateMeeting()
    {
        //showMapActivity를 호출하여 길을 안내하면 됨
    }

    public void deleteMeeting()
    {

        final String stringKey = String.valueOf(key);

        alert.setTitle("비밀번호를 입력하시오.");
        //alert.setMessage("");


        final EditText passwordAlert = new EditText(this);
        alert.setView(passwordAlert);

        alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String password = passwordAlert.getText().toString();

                DeleteMeeting deleteMeeting = new DeleteMeeting();
                deleteMeeting.execute(stringKey,password);
            }
        });

        alert.show();
    }


    //선택된 Meeting을 UI에 적용하기 위한 쓰레드
    class SelectMeeting extends AsyncTask<Integer, Void, Boolean> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MeetingInfoActivity.this);
            loading.setMessage("불러오는 중입니다.");
            //loading.setProgressStyle(loading.STYLE_SPINNER);
            loading.show();
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            loading.dismiss();

            if(flag == true) {
                meeting_name_text.setText(control.getMeetingDTOSelected().getTitle());
                meeting_content_text.setText(control.getMeetingDTOSelected().getMeetingInfo());
                meeting_place_text.setText(control.getMeetingDTOSelected().getPlaceName());
                meeting_publisher_text.setText(control.getMeetingDTOSelected().getPublisher());
            }
            else
                Toast.makeText(getApplicationContext(), "모임 검색 실패!", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            return control.getMeeting(params[0]);

        }
    }

    //선택된 Meeting 삭제하고 UI에 적용하기 위한 쓰레드
    class DeleteMeeting extends AsyncTask<String, Void, Boolean> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MeetingInfoActivity.this);
            loading.setMessage("삭제하는 중입니다.");
            //loading.setProgressStyle(loading.STYLE_SPINNER);
            loading.show();
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            loading.dismiss();

            if(flag == true) {
                Toast.makeText(getApplicationContext(), "모임 삭제 완료", Toast.LENGTH_LONG).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "모임 삭제 실패!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            return control.deleteInfo(params[0],params[1]);

        }
    }
}
