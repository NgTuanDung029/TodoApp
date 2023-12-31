package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.db = this.db;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        db = new DatabaseHandler(getContext());
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.toDoStart.setText("Giờ bắt đầu:" + item.getStartTime());
        holder.toDoEnd.setText("Giờ kết thúc:"+item.getEndTime());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);

                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public void setTasks(List<ToDoModel> todoList){
        this.todoList=todoList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return  activity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("startTime", item.getStartTime());
        bundle.putString("endTime", item.getEndTime());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        TextView toDoStart, toDoEnd;
        public ViewHolder(View view){
            super(view);
            toDoStart=view.findViewById(R.id.toDoStart);
            toDoEnd=view.findViewById(R.id.toDoEnd);
            task=view.findViewById(R.id.todoCheckBox);
        }
    }
}
