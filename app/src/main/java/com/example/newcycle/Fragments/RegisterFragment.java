 package com.example.newcycle.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Activities.AccountActivity;
import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.R;
import com.example.newcycle.Utils.Validator;

import static com.example.newcycle.Utils.Utility.URL;
import static com.example.newcycle.Utils.Utility.showSnackBar;


 public class RegisterFragment extends Fragment {
    private View mView;
     private DbHelper db;

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         db = new DbHelper(getActivity());
     }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        Button btn = mView.findViewById(R.id.btn_signup);
        TextView tlink = mView.findViewById(R.id.et_register_link);

        tlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity)getActivity()).setCurrentItem(0, true);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputEmail = mView.findViewById(R.id.et_email);
                final EditText inputPassword = mView.findViewById(R.id.et_password);
                final EditText inputFName = mView.findViewById(R.id.et_fname);
                final EditText inputLName = mView.findViewById(R.id.et_lname);

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String fName = inputFName.getText().toString().trim();
                String lName = inputLName.getText().toString().trim();

                if(fName.matches("")){
                    showSnackBar(mView, "First Name is Required!", getResources().getColor(R.color.error));
                }else if(lName.matches("")){
                    showSnackBar(mView, "Last Name is Required!", getResources().getColor(R.color.error));
                }else if(!Validator.isNameValid(fName) || !Validator.isNameValid(lName)){
                    showSnackBar(mView, "Invalid First Name or Last Name!", getResources().getColor(R.color.error));
                }else if(email.matches("")){
                    showSnackBar(mView, "Email Required!", getResources().getColor(R.color.error));
                    inputEmail.requestFocus();
                }else if(!Validator.isEmailValid(email)){
                    showSnackBar(mView, "Invalid Email!", getResources().getColor(R.color.error));
                }else if(password.matches("")){
                    showSnackBar(mView, "Password Required!", getResources().getColor(R.color.error));
                    inputPassword.requestFocus();
                }else if(password.length() < 8){
                    showSnackBar(mView, "Password must be at least 8 characters", getResources().getColor(R.color.error));
                }else if(!Validator.isPasswordValid(password)){
                    showSnackBar(mView, "Password must start with a capital letter and contains at least one number", getResources().getColor(R.color.error));
                }else if(password.length() > 25){
                    showSnackBar(mView, "Password must not exceed 25 characters", getResources().getColor(R.color.error));
                }else{
                    db.setURL(URL,"register.php");
                    db.table("user");
                    db.param("email", email);
                    db.param("password", password);
                    db.param("first_name", fName);
                    db.param("last_name", lName);
                    db.setDialogTitle("Signing up..");
                    db.write(new OnWriteClient() {
                        @Override
                        public void onSuccess() {
                            showSnackBar(mView, "You have successfully created an account", getResources().getColor(R.color.success));
                        }

                        @Override
                        public void onError(String message) {
                            switch (message){
                                case "connect_error":
                                    showSnackBar(mView, "Database connection error", getResources().getColor(R.color.error));
                                    break;
                                case "no_table":
                                    showSnackBar(mView, "No table", getResources().getColor(R.color.error));
                                    break;
                                case "non_unique_data_error":
                                    showSnackBar(mView, "An account with the same email address already exists", getResources().getColor(R.color.error));
                                    break;
                                case "insert_error":
                                    showSnackBar(mView, "Unable to register account", getResources().getColor(R.color.error));
                                    break;
                                default:
                                    showSnackBar(mView, message, getResources().getColor(R.color.error));
                            }
                        }
                    });
                }
            }
        });

        return mView;
    }

    public static RegisterFragment newInstance(){
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

}
