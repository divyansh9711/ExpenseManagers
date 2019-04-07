package com.example.android.expensemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    Context context;
    ArrayList<ExpenseItem> list;
    OnClickListener listener;

    public interface OnClickListener{
        public void OnClick(int position);
    }


    public ListItemAdapter(Context context, ArrayList<ExpenseItem> list,OnClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item, viewGroup, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int i) {
        ExpenseItem newitem = list.get(i);
        holder.date.setText(newitem.getDate());

        holder.time.setText(newitem.getTime());
        holder.amount.setText(newitem.getAmount());

        if(newitem.getCategory().equals("Amount Added")){
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.income));
        }
        else {
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.expense));
        }
        holder.category.setText(newitem.getCategory());
        holder.description.setText(newitem.getDescription());

    }

    @Override
    public int getItemCount() {
        if(list == null){
        return 0;}
        return list.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder{


        public TextView date,time,description,amount,category;
        public Button delete;

        public ListItemViewHolder(@NonNull View listItem) {
            super(listItem);
            date = listItem.findViewById(R.id.date);
            time = listItem.findViewById(R.id.time);
            delete = listItem.findViewById(R.id.delete);
            description = listItem.findViewById(R.id.list_description);
            amount = listItem.findViewById(R.id.list_amount);
            category = listItem.findViewById(R.id.list_category);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.OnClick(getAdapterPosition());
                }
            });
        }
    }

    public void addAll(ArrayList<ExpenseItem> itemList){

        list = itemList;
        notifyDataSetChanged();

    }

}
