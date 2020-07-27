package com.hcode.howienote.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据库的增删改查类
 *
 * @author howie
 * @time 2020-07-25 21:31:56
 */
public class CRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;
    // 数据库字段
    private static final String[] columns = {
            NoteDatabase.ID,
            NoteDatabase.TIME,
            NoteDatabase.CONTENT,
            NoteDatabase.TAG
    };

    public CRUD(Context context) {
        dbHandler = new NoteDatabase(context);
    }


    /**
     * 打开数据库写入功能
     */
    public void open() {
        db = dbHandler.getWritableDatabase();
    }


    /**
     * 关闭数据库
     */
    public void close() {
        dbHandler.close();
    }

    /**
     * 将笔记记录在数据库中，并返回id
     *
     * @param note 要增加的note对象
     * @return 获取id后的note对象
     */
    public Note addNote(Note note) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.TIME, note.getTime());
        contentValues.put(NoteDatabase.CONTENT, note.getContent());
        contentValues.put(NoteDatabase.TAG, note.getTag());
        long insertId = db.insert(NoteDatabase.TABLE_NAME, null, contentValues);
        note.setId(insertId);
        close();
        return note;
    }

    /**
     * 根据id获取笔记
     *
     * @param id 笔记的id
     * @return 查询到的笔记
     */
    public Note getNote(long id) {
        open();
        Cursor cursor = db.rawQuery("select * from " + NoteDatabase.TABLE_NAME + " where ?=?",
                new String[]{NoteDatabase.ID, String.valueOf(id)});
        Note note = new Note();
        if (cursor.moveToFirst()) {
            note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
            note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
            note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.TAG)));
        }
        close();
        return note;
    }


    /**
     * 查询并返回所有笔记
     *
     * @return Note列表
     */
    public List<Note> getAllNotes() {
        open();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + NoteDatabase.TABLE_NAME, null);

        // 如果有数据
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.TAG)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        close();
        return notes;
    }

    public void updateNote(Note newNote) {
        open();
        db.execSQL("update " + NoteDatabase.TABLE_NAME + " set " + NoteDatabase.CONTENT + " = ?,"
                        + NoteDatabase.TIME + " =?," + NoteDatabase.TAG
                        + " =? where " + NoteDatabase.ID + "=?",
                new String[]{newNote.getContent(), newNote.getTime()
                        , String.valueOf(newNote.getTag()), String.valueOf(newNote.getId())});
        close();
    }

    public void deleteNote(long id) {
        open();
        db.execSQL("delete from " + NoteDatabase.TABLE_NAME + " where " + NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)});
        close();
    }

    public void deleteAllNotes() {
        open();
        db.execSQL("delete from " + NoteDatabase.TABLE_NAME);

        // 把表的id字段置零
        db.execSQL("delete from sqlite_sequence where name = '" + NoteDatabase.TABLE_NAME + "'");
        close();
    }

}
