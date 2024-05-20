package be.kuleuven.gt.hikinglog.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.UserState;

public class MainActivity extends AppCompatActivity {
    TextView txtUsrname, txtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SQLControl.INSTANCE.setUp(getApplicationContext());
        UserState.INSTANCE.setUp(getApplicationContext());
        txtUsrname = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onBtnLogin_Clicked(View Caller) {
        if (txtUsrname.getText().toString().isEmpty())
        {
            Toast.makeText(getBaseContext(), "Input username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (txtPassword.getText().toString().isEmpty())
        {
            Toast.makeText(getBaseContext(), "Input password", Toast.LENGTH_SHORT).show();
            return;
        }
        btnLogin.setEnabled(false);
        UserState.INSTANCE.findPasswordByUsername(txtUsrname.getText().toString(), new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject.getString("password").equals(txtPassword.getText().toString())){
                            btnLogin.setEnabled(true);
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            UserState.INSTANCE.findByUsername(txtUsrname.getText().toString(), new VolleyCallback() {
                                @Override
                                public void onSuccess(String stringResponse) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(stringResponse);
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                        int idusr = jsonObject1.getInt("iduser");
                                        editor.putInt("usrId", idusr);
                                        editor.putString("username", txtUsrname.getText().toString());
                                        editor.apply();
                                        Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        Toast.makeText(getBaseContext(),"Index out of range", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException exception){
                        Toast.makeText(getBaseContext(), "Usermame not in database", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "No array", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnLogin.setEnabled(true);
    }

    public void onBtnSignin_Clicked(View Caller) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}