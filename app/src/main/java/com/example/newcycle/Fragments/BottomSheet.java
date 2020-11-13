package com.example.newcycle.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newcycle.Activities.AccountActivity;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.DataHandlerString;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Data;
import com.example.newcycle.R;
import com.example.newcycle.Utils.SessionManager;
import com.example.newcycle.Utils.User;
import com.example.newcycle.Utils.Validator;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.newcycle.Utils.Utility.URL;

public class BottomSheet extends BottomSheetDialogFragment {
    private DbHelper db;
    private User user;
    private Data data;
    private SessionManager session;

    public BottomSheet(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHelper(getActivity());
        user = User.getInstance();
        user.init(getActivity());
        session = new SessionManager(getActivity());

         if(!user.isSessionInit()){
             session.setSession("lastActivity","UserProfile");
             Intent intent = new Intent(getActivity(), AccountActivity.class);
             startActivity(intent);
         }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        Button btnUpdate = view.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final EditText inputPhone = view.findViewById(R.id.phone);
               final EditText inputAddress = view.findViewById(R.id.address);

                String phone = inputPhone.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();

                if(phone.matches("")){
                    Toast.makeText(getActivity(), "Mobile number empty!", Toast.LENGTH_SHORT).show();
                    inputPhone.requestFocus();
                }else if(!Validator.isPhoneNumberValid(phone)){
                    Toast.makeText(getActivity(), "Invalid Mobile number!", Toast.LENGTH_SHORT).show();
                }else if(address.matches("")){
                    Toast.makeText(getActivity(), "Address is empty!", Toast.LENGTH_SHORT).show();
                    inputPhone.requestFocus();
                }else{
                     data = user.loadDataFromSession();

                    if(data.getId() > 0){
                        db.setURL(URL,"update_userinfo.php");
                        db.table("user");
                        db.param("id", String.valueOf(data.getId()));
                        db.param("phone_no", phone);
                        db.param("address", address);
                        db.write(new OnWriteClient() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "You have successfully updated your account", Toast.LENGTH_SHORT).show();
                                sync();
                            }

                            @Override
                            public void onError(String message) {
                                switch (message){
                                    case "connect_error":
                                        Toast.makeText(getActivity(), "Database connection error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "no_table":
                                        Toast.makeText(getActivity(), "No table", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "query_error":
                                        Toast.makeText(getActivity(), "Query error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "update_error":
                                        Toast.makeText(getActivity(), "Unable to update account", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "request_timeout":
                                        Toast.makeText(getActivity(), "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "no_internet_connection":
                                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "internal_server_error":
                                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(getActivity(), "Unable to update account", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return view;
    }

    public void sync(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                db.setURL(URL,"user.php");
                db.param("param", "id, email, first_name, last_name, phone_no, address, balance");
                db.param("id", String.valueOf(data.getId()));
                db.request(new DataHandlerString() {
                    @Override
                    public void getResult(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.has("message")) {
                                switch (obj.getString("message")) {
                                    case "connect_error":
                                        Toast.makeText(getActivity(), "Database connection error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "no_table":
                                        Toast.makeText(getActivity(), "No table", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "query_error":
                                        Toast.makeText(getActivity(), "Query error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "update_error":
                                        Toast.makeText(getActivity(), "Unable to update account", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "request_timeout":
                                        Toast.makeText(getActivity(), "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "no_internet_connection":
                                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "internal_server_error":
                                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Data data = new Data();
                                data.setId(obj.getInt("id"));
                                data.setEmail(obj.getString("email"));
                                data.setFirstName(obj.getString("first_name"));
                                data.setLastName(obj.getString("last_name"));
                                data.setBalance(obj.getInt("balance"));
                                data.setAddress(obj.getString("address"));
                                data.setPhoneNo(obj.getString("phone_no"));
                                user.initUserSession(data);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }});

            }
        }, 1000);
    }
}
