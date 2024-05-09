package be.kuleuven.gt.hikinglog.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.databinding.ActivityBaseBinding;
import be.kuleuven.gt.hikinglog.fragments.ChatsFragment;
import be.kuleuven.gt.hikinglog.fragments.HomeFragment;
import be.kuleuven.gt.hikinglog.fragments.ProfileFragment;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.UserState;

public class BaseActivity extends AppCompatActivity {
    MapState mapState = MapState.INSTANCE;
    ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapState.setUpMapState(getBaseContext());
//        mapState = new MapState(this);

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home)
                replaceFragment(new HomeFragment());
            else if (item.getItemId() == R.id.profile)
                replaceFragment(new ProfileFragment());
            else if (item.getItemId() == R.id.chat)
                replaceFragment(new ChatsFragment());
            return true;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commit();
    }

    public MapState returnMapState() {
        return mapState;
    }

    public int getUsrId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usrId", 1);
    }
}