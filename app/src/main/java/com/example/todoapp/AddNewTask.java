package com.example.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    //widgets
    private EditText mEditText,mDesc,mCategory;

    private TextView mDueDate,mStatus,mPriority;
    private Button mSaveButton;

    private DataBaseHelper myDb;

    private Button btn;
    private Spinner spinner,sp;
    private DatePickerDialog dialog;

    public AddNewTask() {
    }

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner=view.findViewById(R.id.spinner1);
        sp=view.findViewById(R.id.spinner2);
        mEditText = view.findViewById(R.id.edittext);
        mCategory=view.findViewById(R.id.category);
        mDesc=view.findViewById(R.id.desc);
        mDueDate=view.findViewById(R.id.dueDate);
        mPriority=view.findViewById(R.id.priority);
        mStatus=view.findViewById(R.id.status);
        mSaveButton = view.findViewById(R.id.button_save);
       // mts=view.findViewById(R.id.ts);
        btn=(Button) view.findViewById(R.id.duebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  openDialog();
            }
        });
        String temp[]={"Low","Medium","High"};
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,temp);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPriority.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String arr[]={"New","InProgress","Completed"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String val=adapterView.getItemAtPosition(i).toString();
                mStatus.setText(val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        myDb = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String title = bundle.getString("title");
            mEditText.setText(title);
            String desc = bundle.getString("desc");
            mDesc.setText(desc);
            String status = bundle.getString("mStatus");
            mStatus.setText(status);
            String category = bundle.getString("category");
            mCategory.setText(category);
            String priority = bundle.getString("priority");
            mPriority.setText(priority);
            String date=bundle.getString("mdueDate");
            mDueDate.setText(date);

            if (title.length() > 0 ){
                mSaveButton.setEnabled(false);
            }

        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().equals("")){
                   mSaveButton.setEnabled(false);
                   mSaveButton.setBackgroundColor(Color.GRAY);
               }else{
                   mSaveButton.setEnabled(true);
                   mSaveButton.setBackgroundColor(getResources().getColor(R.color.LightBlue));
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String title = mEditText.getText().toString();
               String desc = mDesc.getText().toString();
               String category = mCategory.getText().toString();
               String priority = mPriority.getText().toString();
               String duedate = mDueDate.getText().toString();
               String status = mStatus.getText().toString();


                if (finalIsUpdate){
                   myDb.updateTitle(bundle.getInt("id"),title);
               }else{
                   ToDoModel item = new ToDoModel();
                   item.setTitle(title);
                   item.setTask(desc);
                   item.setStatus(status);
                   item.setPriority(priority);
                   item.setCategory(category);
                   item.setDueDate(duedate);
                   myDb.insertTask(item);
               }
               dismiss();

            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }

    private void openDialog(){
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;

                // Format the date with separators
                String formattedDate = String.format("%04d-%02d-%02d", year, month, day);

                // Set the formatted date to your TextView (mDueDate)
                mDueDate.setText(formattedDate);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener, // Set the listener
                2024, // Initial year
                0, // Initial month (zero-based)
                1 // Initial day
        );

        datePickerDialog.show();
    }

}
