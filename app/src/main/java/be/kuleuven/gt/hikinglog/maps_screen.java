package be.kuleuven.gt.hikinglog;
import android.app.Fragment;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import be.kuleuven.gt.hikinglog.databinding.ActivityMapsScreenBinding;

public class maps_screen extends AppCompatActivity {

    ActivityMapsScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMapsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
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

    private void replaceFragment(androidx.fragment.app.Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.fragContainer, fragment);
        transaction.commit();
    }

}