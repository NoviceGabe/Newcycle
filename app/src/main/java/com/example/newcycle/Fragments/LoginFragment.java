package com.example.newcycle.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newcycle.Activities.AccountActivity;
import com.example.newcycle.Activities.CartActivity;
import com.example.newcycle.Activities.CheckoutActivity;
import com.example.newcycle.Activities.FavoriteActivity;
import com.example.newcycle.Activities.MainActivity;
import com.example.newcycle.Activities.OrderActivity;
import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.Activities.UserProfileActivity;
import com.example.newcycle.Model.Data;
import com.example.newcycle.Interface.DataHandlerString;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.R;
import com.example.newcycle.Utils.SessionManager;
import com.example.newcycle.Utils.User;
import com.example.newcycle.Utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.newcycle.Utils.Utility.URL;
import static com.example.newcycle.Utils.Utility.showSnackBar;


public class LoginFragment extends Fragment {
    private View mView;
    private DbHelper db;
    private User user;
    private SessionManager session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         db = new DbHelper(getActivity());
         user = User.getInstance();
         user.init(getActivity());
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        Button btn = mView.findViewById(R.id.btn_login);
        TextView tlink = mView.findViewById(R.id.et_signup_link);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final EditText inputEmail = mView.findViewById(R.id.et_email);
                final EditText inputPassword = mView.findViewById(R.id.et_password);

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(email.matches("")){
                    showSnackBar(mView, "Email Required!", getResources().getColor(R.color.error));
                    inputEmail.requestFocus();
                }else if(!Validator.isEmailValid(email)){
                    showSnackBar(mView, "Invalid Email!", getResources().getColor(R.color.error));
                }else if(password.matches("")){
                    showSnackBar(mView, "Password Required!", getResources().getColor(R.color.error));
                    inputPassword.requestFocus();
                }else{
                    db.setURL(URL,"login.php");
                    db.table("user");
                    db.param("email", email);
                    db.param("password", password);
                    db.setDialogTitle("Signing in..");
                    db.request(new DataHandlerString() {
                       @Override
                       public void getResult(String response) {
                           try {
                               JSONObject obj = new JSONObject(response);

                               if(obj.has("message")){
                                   switch (obj.getString("message")){
                                       case "connect_error":
                                           showSnackBar(mView, "Database connection error", getResources().getColor(R.color.error));
                                           break;
                                       case "no_table":
                                           showSnackBar(mView, "Internal Server Error", getResources().getColor(R.color.error));
                                           break;
                                       case "query_error":
                                           showSnackBar(mView, "Unable to login", getResources().getColor(R.color.error));
                                           break;
                                       case "auth_error":
                                           showSnackBar(mView, "Invalid email or password", getResources().getColor(R.color.error));
                                           break;
                                       default:
                                           showSnackBar(mView, obj.getString("message"), getResources().getColor(R.color.error));
                                   }
                               }else{

                                     Data data = new Data();
                                     data.setId(obj.getInt("id"));
                                     data.setEmail(obj.getString("email"));
                                     data.setFirstName(obj.getString("first_name"));
                                     data.setLastName(obj.getString("last_name"));
                                     data.setBalance(obj.getInt("balance"));
                                     data.setAddress(obj.getString("address"));
                                     data.setPhoneNo(obj.getString("phone_no"));
                                     user.initUserSession(data);

                                     if(user.isSessionInit()){
                                         Intent intent;
                                         String lastActivity = session.getSessionString("lastActivity");

                                         switch (lastActivity){
                                             case "Cart":
                                                 intent = new Intent(getActivity(), CartActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             case "ProductView":
                                                 intent = new Intent(getActivity(), ProductViewActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             case "Checkout":
                                                 intent = new Intent(getActivity(), CheckoutActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             case "Order":
                                                 intent = new Intent(getActivity(), OrderActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             case "Favorite":
                                                 intent = new Intent(getActivity(), FavoriteActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             case "UserProfile":
                                                 intent = new Intent(getActivity(), UserProfileActivity.class);
                                                 startActivity(intent);
                                                 break;
                                             default:
                                                 intent = new Intent(getActivity(), MainActivity.class);
                                                 startActivity(intent);
                                         }
                                         getActivity().finish();
                                     }

                                 }

                           } catch (JSONException e) {
                              showSnackBar(mView, e.getMessage(), getResources().getColor(R.color.error));
                           }
                       }
                   });
                }
            }
        });


        tlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity)getActivity()).setCurrentItem(1, true);
            }
        });

        return mView;
    }

    public static LoginFragment newInstance(){
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }




}
