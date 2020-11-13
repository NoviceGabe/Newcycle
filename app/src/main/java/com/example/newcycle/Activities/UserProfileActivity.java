package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Fragments.BottomSheet;
import com.example.newcycle.Model.Data;
import com.example.newcycle.R;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.newcycle.Utils.Utility.URL;

public class UserProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Data userData;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CircleImageView image = findViewById(R.id.profile);
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView contact = findViewById(R.id.contact_no);
        TextView address = findViewById(R.id.address);
        Button update = findViewById(R.id.update);
        Button logout = findViewById(R.id.logout);

        user = User.getInstance();
        user.init(UserProfileActivity.this);

        if(!user.isSessionInit()){
            Intent intent = new Intent(UserProfileActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            userData = user.loadDataFromSession();
            name.setText(userData.getFirstName()+" "+userData.getLastName());
            email.setText(userData.getEmail());

            if(userData.getAddress() != null && !userData.getAddress().equals("null")){
                address.setText(userData.getAddress());
            }else{
                address.setText("");
            }

            if(userData.getPhoneNo() != null && !userData.getPhoneNo().equals("null")){
                contact.setText(userData.getPhoneNo());
            }else{
                contact.setText("");
            }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheet bottomSheet = new BottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "TAG");
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.logOut(UserProfileActivity.this);
                    finish();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        UI ui = new UI(UserProfileActivity.this);
        View view = findViewById(R.id.checkout);
        ui.loadUITheme(UserProfileActivity.this, toolbar, null, (RelativeLayout) view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}