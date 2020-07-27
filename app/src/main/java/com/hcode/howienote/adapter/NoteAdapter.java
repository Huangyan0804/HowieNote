package com.hcode.howienote.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.hcode.howienote.R;
import com.hcode.howienote.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {
    private final Context mContext;
    private List<Note> backList;  // 备份的笔记列表
    private List<Note> noteList;  // 笔记列表，会改变，以防万一先备份
    private MyFilter mFilter;

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.note_list_layout,
                    viewGroup, false);
            holder = new ViewHolder();
            holder.content = view.findViewById(R.id.tv_note_list_content);
            holder.time = view.findViewById(R.id.tv_notelist_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        Log.d("Howie", "getView: " + noteList.get(i).toString());
        holder.time.setText(noteList.get(i).getTime());
        holder.content.setText(noteList.get(i).getContent().trim());
        return view;
    }

    static class ViewHolder {
        TextView content;
        TextView time;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(charSequence)) {
                list = backList;
            } else {
                list = new ArrayList<>();
                for (Note note : backList) {
                    if (note.getContent().contains(charSequence)) {
                        list.add(note);
                    }
                }
            }
            results.values = list;
            results.count = list.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList = (List<Note>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
