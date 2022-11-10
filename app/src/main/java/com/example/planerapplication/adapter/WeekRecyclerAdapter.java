package com.example.planerapplication.adapter;

import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planerapplication.R;
import com.example.planerapplication.viewmodel.DateViewModel;
import com.example.planerapplication.model.room.entity.Tasks;
import com.example.planerapplication.singleton.FindIDSingletonClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class WeekRecyclerAdapter extends RecyclerView.Adapter<WeekRecyclerAdapter.ViewHolder>{

    private static final int[] DAYS_OF_WEEK = new int[]{R.string.monday, R.string.tuesday,R.string.wednesday,R.string.thursday,R.string.friday,R.string.saturday,R.string.sunday };

    private final List<Tasks> mTasks;
    private final OnItemClickedListener mListener;
    private final Date[] mDatesInTheWeek;
    private final DateViewModel mDateViewModel;
    private final boolean[] mOpenClosed;

    int mLength;

    public WeekRecyclerAdapter(List<Tasks> mTasks, OnItemClickedListener listener, Date[] datesInTheWeek, DateViewModel dateViewModel, boolean[] openClosed) {
        this.mTasks = mTasks;
        this.mListener = listener;
        this.mDatesInTheWeek = datesInTheWeek;
        this.mDateViewModel = dateViewModel;
        this.mOpenClosed = openClosed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_week_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text;
        Tasks tasks;
        List<Tasks> pom = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        text = holder.itemView.getContext().getString(DAYS_OF_WEEK[position])+", "+dateFormat.format(mDatesInTheWeek[position]);
        holder.txtDayInWeek.setText(text);
        //holder.txtMore.setId(position);





        if(!mTasks.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(mTasks, Comparator.comparingInt(Tasks::getTypeID));
            } else {
                Collections.sort(mTasks, (obj1, obj2) -> obj1.getTypeID() - obj2.getTypeID());
            }


            for (int i = 0; i < mTasks.size(); i++) {
                if(position == 6){
                    if (mTasks.get(i).getDateTime() >= mDatesInTheWeek[position].getTime()) {
                        pom.add(mTasks.get(i));
                    }
                } else{
                    if (mTasks.get(i).getDateTime() >= mDatesInTheWeek[position].getTime() &&
                            mTasks.get(i).getDateTime() < mDatesInTheWeek[position+1].getTime()) {
                        pom.add(mTasks.get(i));
                    }
                }
            }

        }

        if(!pom.isEmpty()){
            if(pom.size()<4){
                holder.txtMore.setText(holder.itemView.getContext().getString(R.string.no_more));
                mLength = pom.size();
            }
            else {
                if(mOpenClosed[position]){
                    holder.txtMore.setText(holder.itemView.getContext().getString(R.string.less));
                    mLength = pom.size();
                }
                else{
                    holder.txtMore.setText(holder.itemView.getContext().getString(R.string.more));
                    mLength = 4;
                }
            }

            for (int i = 0; i < mLength; i++) {
                tasks = pom.get(i);

                holder.tableRow = new TableRow(holder.itemView.getContext());
                holder.tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                holder.tableRow.setPadding(0, (int) holder.itemView.getContext().getResources().getDimension(R.dimen.week_layout_padding_top), 0, 0);

                holder.txtTask = new TextView(holder.itemView.getContext());
                holder.txtTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                if(tasks.getTypeID() == 1){
                    holder.txtTask.append((i+1)+". "+tasks.getTaskEventName());
                }
                else{
                    holder.txtTask.append("* "+tasks.getTaskEventName());
                }

                holder.tableRow.addView(holder.txtTask);
                holder.tableLayout.addView(holder.tableRow);
            }
        } else {
            holder.txtMore.setText(holder.itemView.getContext().getString(R.string.no_tasks_string));

            holder.tableRow = new TableRow(holder.itemView.getContext());
            holder.tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            holder.tableRow.setPadding(0, (int) holder.itemView.getContext().getResources().getDimension(R.dimen.week_layout_padding_top), 0, 0);

            holder.txtTask = new TextView(holder.itemView.getContext());
            holder.txtTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.txtTask.append("");
            holder.txtTask.setVisibility(View.INVISIBLE);

            holder.tableRow.addView(holder.txtTask);
            holder.tableLayout.addView(holder.tableRow);
        }

    }

    @Override
    public int getItemCount() {
        return mDatesInTheWeek.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayInWeek, txtMore, txtTask;
        TableLayout tableLayout;
        TableRow tableRow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDayInWeek = itemView.findViewById(R.id.txtDayInWeek);
            tableLayout = itemView.findViewById(R.id.tableLayoutWeek);
            txtMore = itemView.findViewById(R.id.txtMore);
            txtMore.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                FindIDSingletonClass txtMoreID = FindIDSingletonClass.getInstance();
                txtMoreID.setData(position);
                mListener.OnMoreCLicked();
            });


            tableLayout.setOnClickListener(view -> {
                setDate();
                mListener.OnDayInWeekClicked();
            });

            txtDayInWeek.setOnClickListener(view -> {
                setDate();
                mListener.OnDayInWeekClicked();
            });
        }

        private void setDate(){
            int position = getAbsoluteAdapterPosition();
            Date pom = new Date();
            pom.setTime(mDatesInTheWeek[position].getTime());
            mDateViewModel.setSelectItem(pom);
        }
    }

    public interface OnItemClickedListener{
        void OnDayInWeekClicked();
        void OnMoreCLicked();
    }
}
