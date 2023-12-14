package com.example.scholarisv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.scholarisv2.databinding.NewsActivityBinding;

public class NewsActivity extends AppCompatActivity {

    NewsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new NewsFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.news) {
                replaceFragment(new NewsFragment());
            } else if (item.getItemId() == R.id.people) {
                replaceFragment(new PeopleFragment());
            }
            // Add more else-if blocks for additional cases if needed
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}