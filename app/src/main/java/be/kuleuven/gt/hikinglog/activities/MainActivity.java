package be.kuleuven.gt.hikinglog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.helpers.SQLControl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SQLControl.INSTANCE.setUp(getBaseContext());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onBtnLogin_Clicked(View Caller) {
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }

    public void onBtnSignin_Clicked(View Caller) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}