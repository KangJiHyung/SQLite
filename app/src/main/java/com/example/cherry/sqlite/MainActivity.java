package com.example.cherry.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Database ���� ��ü��
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;


    // layout object
    EditText mEtName;
    Button mBtInsert;
    Button mBtRead;

    EditText mEtId;
    Button mBtDelete;
    Button mBtUpdate;

    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Database ���� �� ����
        db = openOrCreateDatabase(dbName,dbMode,null);
        // ���̺� ����
        createTable();

        mEtName = (EditText) findViewById(R.id.editText);
        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);

        mEtId = (EditText) findViewById(R.id.editText2);
        mBtDelete = (Button) findViewById(R.id.bt_delete);
        mBtUpdate = (Button) findViewById(R.id.bt_update);

        mList = (ListView) findViewById(R.id.listView);

        // Insert ��ư
        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                insertData(name);
            }
        });

        // Read ��ư
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        // Delete ��ư
        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String index = mEtId.getText().toString();  // �ؽ�Ʈ�� �޾ƿ���
                int num = Integer.parseInt(index);  // string �� int
                removeData(num);

                // read ���
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        // Update ��ư
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString(); // �ٲ� �̸��� �޾ƿ�
                String index = mEtId.getText().toString();  // �� ��°���� ���ڸ� �޾ƿ�
                int num = Integer.parseInt(index);  // string �� int
                updateData(num, name);

                // read ���
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

    }

    // Table ����
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table ����
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data �߰�
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data ������Ʈ
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data ����
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data �б�(��������)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor ��ü)�� ��� ������ false ����
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }

    // ��� Data �б�
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.close();
    }

}

