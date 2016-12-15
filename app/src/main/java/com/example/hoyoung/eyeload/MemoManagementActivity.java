package com.example.hoyoung.eyeload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-10-8.
 */

public class MemoManagementActivity extends AppCompatActivity {

    private ListView listView;
    private MemoControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_management);
        setupListView();
        setTitle("메모 목록");

    }

    @Override
    public void onResume() {
        super.onResume();

        setupListView();

    }

    //List내용을 xml에 추가하는 부분
    public void setupListView()
    {

        SelectAllPersonalMemo selectAllPersonalMemo = new SelectAllPersonalMemo();
        selectAllPersonalMemo.execute();

    }

    public void memoClicked() {

        // Memo list 선택 작동하는 부분
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MemoManagementActivity.this,MemoInfoActivity.class);
                intent.putExtra("memoKey",control.getMemoList().get(position).getKey());
                intent.putExtra("flag", 0);
                startActivity(intent);

            }
        };
        listView.setOnItemClickListener(listener);
    }


    class SelectAllPersonalMemo extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MemoManagementActivity.this);
            loading.setMessage("불러오는 중입니다.");

            listView = (ListView) findViewById(R.id.memoManagementListview1);

            control = MemoControl.getInstance();

            loading.show();
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            loading.dismiss();

            if(flag == true) {
                listView.setAdapter(control);
                memoClicked();
            }
            else
                Toast.makeText(getApplicationContext(), "메모 검색 실패!", Toast.LENGTH_LONG).show();

        }


        @Override
        protected Boolean doInBackground(Void... params) {

            return control.getAllPersonalMemo();

        }
    }
}
