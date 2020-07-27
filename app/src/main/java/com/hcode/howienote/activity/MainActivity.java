package com.hcode.howienote.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.hcode.howienote.R;
import com.hcode.howienote.adapter.NoteAdapter;
import com.hcode.howienote.model.CRUD;
import com.hcode.howienote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener
        , AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "Howie"; // 调试TAG
    private final Context mContext = this;
    private FloatingActionButton mBtnAddNote;
    private ListView mListView;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNoteList = new ArrayList<>();
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        bindListener();
    }

    /**
     * 绑定View的Id
     */
    public void bindViews() {
        mDrawerLayout = findViewById(R.id.layout_main_draw_view);
        mNavView = findViewById(R.id.nav_main_nav_view);
        mBtnAddNote = findViewById(R.id.btn_main_add_note);
        mListView = findViewById(R.id.lv_main_note_list);
        mNoteAdapter = new NoteAdapter(mContext, mNoteList);
        mListView.setAdapter(mNoteAdapter);
        refreshListView();

        myToolbar = findViewById(R.id.tb_mytoolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
    }

    public void bindListener() {
        mBtnAddNote.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mNavView.setNavigationItemSelectedListener(this);
    }

    /**
     * 打开模式 openMode
     * 0 创建模式：新建笔记
     * 1 编辑模式：编辑已有笔记
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_add_note:
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra("openMode", 0); // 新建模式
                startActivityForResult(intent, 0);
                break;
            case 100:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.lv_main_note_list:
                Note note = (Note) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("time", note.getTime());
                intent.putExtra("content", note.getContent());
                intent.putExtra("tag", note.getTag());
                intent.putExtra("openMode", 1); // 编辑模式
                startActivityForResult(intent, 1);
            case 3:
                break;
        }
    }


    /**
     * 返回模式 returnMode
     * 0 什么都不做
     * 1 创建模式：创建新笔记
     * 2 修改模式：修改已有笔记
     * 3 删除模式：删除已有笔记
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int returnMode = data.getIntExtra("returnMode", 0);
        String content, time;
        CRUD op = new CRUD(mContext);
        long id;
        switch (returnMode) {
            case 0:
                break;
            case 1:
                content = data.getStringExtra("content");
                time = data.getStringExtra("time");
                Note note = new Note(content, time, 1);
                op.addNote(note);
                break;
            case 2:
                id = data.getLongExtra("id", 0);
                content = data.getStringExtra("content");
                time = data.getStringExtra("time");
                int tag = data.getIntExtra("tag", 1);
                Note newNote = new Note(content, time, tag);
                newNote.setId(id);
                op.updateNote(newNote);
                break;
            case 3:
                id = data.getLongExtra("id", 0);
                op.deleteNote(id);
                break;
        }
        refreshListView();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshListView() {
        if (mNoteList.size() > 0) mNoteList.clear();
        CRUD op = new CRUD(mContext);
        mNoteList.addAll(op.getAllNotes());
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // 绑定搜索按钮
        MenuItem mSearch = menu.findItem(R.id.menu_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNoteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击主页菜单栏
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_clear:
                new AlertDialog.Builder(mContext).setMessage("Delete All Notes?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CRUD op = new CRUD(mContext);
                                op.deleteAllNotes();
                                refreshListView();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.side_menu_settings:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.side_menu_logcat:
                break;
            case R.id.side_menu_feedback:
                break;
        }
        return true;
    }
}
