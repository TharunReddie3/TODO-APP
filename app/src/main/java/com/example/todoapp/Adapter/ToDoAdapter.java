package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB , MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getTask());
        holder.Category.setText(item.getCategory());
        holder.priority.setText(item.getPriority());
        holder.duedate.setText(item.getDueDate());
        holder.status.setText(item.getStatus());
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deletTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("title" , item.getTitle());
        bundle.putString("desciption" , item.getTask());
        bundle.putString("status" , item.getStatus());
        bundle.putString("category" , item.getCategory());
        bundle.putString("priority" , item.getPriority());
        bundle.putString("duedate" , item.getDueDate());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
        

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
       TextView title,description,duedate,Category,priority,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title1);
            description=itemView.findViewById(R.id.description1);
            duedate=itemView.findViewById(R.id.duedate1);
            Category=itemView.findViewById(R.id.category1);
            priority=itemView.findViewById(R.id.priority1);
            status=itemView.findViewById(R.id.status1);
        }
    }
}
