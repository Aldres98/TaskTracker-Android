package com.example.aldres.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Aldres on 19.07.2018.
 */

public class MyAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private List<Project> projects;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public MyAdapter(Context context, List<Project> projects) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.projects = projects;

        for (int i = 0; i < projects.size(); i++) {
            addSectionHeaderItem(projects.get(i).getTitle());
            for(int j = 0; j < projects.get(i).getTodos().size(); j++ ){
                addItem(projects.get(i).getTodos().get(j));
            }
        }
    }

    public void addItem(Todo todo) {
        todo.setPosition(mData.size());
        mData.add(todo.getText());
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.todo, null);
                    holder.checkBox = convertView.findViewById(R.id.checkbox);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.project, null);
                    holder.textView = convertView.findViewById(R.id.project_separator_item);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(holder.checkBox != null){
            Todo curTodo = FindTodoByIndex(position);

            holder.checkBox.setTag(curTodo);
            holder.checkBox.setOnCheckedChangeListener(todoChange);

            assert curTodo != null;
            holder.checkBox.setChecked(curTodo.isCompleted());
            if(curTodo.isCompleted()){
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            holder.checkBox.setText(mData.get(position));
        }else{
            holder.textView.setText(mData.get(position));
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }

    private Todo FindTodoByIndex(int index){
        for(int i=0;i<projects.size();i++){
            List<Todo> curTodos = projects.get(i).getTodos();
            for(int j = 0; j < curTodos.size(); j++){
                if(curTodos.get(j).getPosition() == index)
                    return curTodos.get(j);
            }
        }
        return null;
    }

    private CompoundButton.OnCheckedChangeListener todoChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Todo todoInFocus = (Todo) compoundButton.getTag();
            if (todoInFocus.isCompleted() == isChecked) return;
            if( !compoundButton.isShown() ) return;

            todoInFocus.setCompleted(isChecked);

            Ion.with(mInflater.getContext())
                    .load("PUT", "https://radiant-island-23944.herokuapp.com/todo/" +  String.valueOf( todoInFocus.getId()) ).asString();

            if(todoInFocus.isCompleted()){
                compoundButton.setPaintFlags(compoundButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                compoundButton.setPaintFlags(compoundButton.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    };
}
