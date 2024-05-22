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

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.UserState;

public class SigninActivity extends AppCompatActivity {
    TextView txtUsrname, txtPassword, txtPasswordRepeat;
    Button btnConrfirm;
    UserState userState = UserState.INSTANCE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtUsrname = findViewById(R.id.txtUsernameInputField);
        txtPassword = findViewById(R.id.txtPasswordInputField);
        txtPasswordRepeat = findViewById(R.id.txtPasswordConfirmationField);
        btnConrfirm = findViewById(R.id.btnConfirm);
    }

    public void onBtnConfirm_Clicked(View Caller) {
        if (txtUsrname.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Username should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (txtUsrname.getText().toString().contains(".")) {
            Toast.makeText(getBaseContext(), "Username should not contain periods", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (txtPassword.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Both passwords need to be filled in", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!(txtPassword.getText().toString().equals(txtPasswordRepeat.getText().toString()))) {
            Toast.makeText(getBaseContext(), "Passwords should match", Toast.LENGTH_SHORT).show();
            return;
        }
        btnConrfirm.setEnabled(false);
        userState.findByUsername(txtUsrname.getText().toString(), new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(stringResponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (!jsonArray.isNull(0)){
                    Toast.makeText(getBaseContext(), "Username already in database", Toast.LENGTH_SHORT).show();
                    btnConrfirm.setEnabled(true);
                }
                else {
                    Toast.makeText(getBaseContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                    userState.addNewUser(txtUsrname.getText().toString(), txtPassword.getText().toString(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String stringResponse) {
                            btnConrfirm.setEnabled(true);
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", txtUsrname.getText().toString());
                            userState.findByUsername(txtUsrname.getText().toString(), new VolleyCallback() {
                                @Override
                                public void onSuccess(String stringResponse) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(stringResponse);
                                        int usrId = jsonArray.getJSONObject(0).getInt("iduser");
                                        editor.putInt("usrId", usrId);
                                        editor.apply();
                                        Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            return;
                        }
                    });
                }
            }
        });
        btnConrfirm.setEnabled(true);
    }
}