package com.hcode.howienote.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.hcode.howienote.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends BaseActivity {
    private Context mContext = this;
    private EditText mContent;
    private TextView mTime, mLenth;
    private Toolbar myToolbar;
    private long id;
    private int openMode;
    private String oldTime;
    private String oldContent;
    private int oldTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        bindViews();
        bindListener();
        switchOpenMode();
    }

    public void bindViews() {
        mContent = findViewById(R.id.et_etnode_content);
        mTime = findViewById(R.id.tv_editnote_time);
        mLenth = findViewById(R.id.tv_editnote_length);
        myToolbar = findViewById(R.id.tb_mytoolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void bindListener() {
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLenth.setText(charSequence.toString().trim().length() + "字");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * 打开模式 openMode
     * 0 创建模式：新建笔记
     * 1 编辑模式：编辑已有笔记
     */
    public void switchOpenMode() {
        Intent intent = getIntent();
        openMode = intent.getIntExtra("openMode", 0);
        switch (openMode) {
            case 0:
                mTime.setText(dateToStr());
                mLenth.setText("0字");
                break;
            case 1:
                id = intent.getLongExtra("id", 0);
                oldTime = intent.getStringExtra("time");
                oldContent = intent.getStringExtra("content");
                oldTag = intent.getIntExtra("tag", 1);
                mContent.setText(oldContent);
                mTime.setText(oldTime);
                mLenth.setText(oldContent.trim().length() + "字");
        }
    }

    /**
     * 返回模式 returnMode        打开模式 openMode
     * 0 什么都不做                0 创建模式：新建笔记
     * 1 创建模式：创建新笔记        1 编辑模式：编辑已有笔记
     * 2 修改模式：修改已有笔记
     * 3 删除模式：删除已有笔记
     */
    public void switchReturnMode() {
        Intent intent = new Intent();
        String content = mContent.getText().toString();

        switch (openMode) {
            case 0:
                if (content.trim().isEmpty()) {
                    // 如果文字为空，什么都不做
                    intent.putExtra("returnMode", 0);
                } else {
                    // 新建笔记
                    intent.putExtra("returnMode", 1);
                    intent.putExtra("content", content);
                    intent.putExtra("time", dateToStr());
                }
                break;
            case 1:
                if (content.trim().isEmpty()) {
                    // 如果修改后为空，删除
                    intent.putExtra("returnMode", 3);
                    intent.putExtra("id", id);
                } else if (content.equals(oldContent)) {
                    // 如果没修改，什么都不做
                    intent.putExtra("returnMode", 0);
                } else {
                    // 修改笔记
                    intent.putExtra("returnMode", 2);
                    intent.putExtra("id", id);
                    intent.putExtra("tag", oldTag);
                    intent.putExtra("content", content);
                    intent.putExtra("time", dateToStr());
                }
                break;
        }
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        switchReturnMode();
        finish();
        super.onBackPressed();
    }

    public String dateToStr() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


    // 绑定导航栏按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 导航栏按钮设置事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switchReturnMode();
                finish();
                break;
            case R.id.delete:
                new AlertDialog.Builder(mContext).setMessage("Delete or not?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                if (openMode == 0) {
                                    // 如果是新建模式，则直接关闭
                                    intent.putExtra("returnMode", 0);
                                } else {
                                    // 如果是已有的笔记，则删除
                                    intent.putExtra("returnMode", 3);
                                    intent.putExtra("id", id);
                                }
                                setResult(RESULT_OK, intent);
                                finish();
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
}
