package com.example.flyeasy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.flyeasy.databinding.ActivityMainBinding;
import com.example.flyeasy.util.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    NavController navController;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }


        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Drawer toggle (hamburger menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                0,
                0
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load profile info in drawer header
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.image_profile);
        TextView userName = headerView.findViewById(R.id.text_user_name);

        //session manager to get name role and id
   SessionManager sessionManager = new SessionManager(this);
        String currentUserName = sessionManager.getUserName();
        String imageUri = sessionManager.getProfileImageUri();

        userName.setText(currentUserName);

        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .placeholder(R.drawable.lc_profile)
                    .into(profileImage);
        } else {
            Glide.with(this)
                    .load(R.drawable.lc_profile)
                    .into(profileImage);
        }

        String role = sessionManager.getUserRole(); // "admin" or "user"

        // Navigation controller from nav host fragment
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            if ("admin".equalsIgnoreCase(role)) {
                // Admin user
                binding.bottomNavigation.setVisibility(View.GONE);
                findViewById(R.id.bottom_navigation_admin).setVisibility(View.VISIBLE);


                NavigationUI.setupWithNavController(
                        (com.google.android.material.bottomnavigation.BottomNavigationView)
                                findViewById(R.id.bottom_navigation_admin),
                        navController
                );
            } else {
                // Normal user
                binding.bottomNavigation.setVisibility(View.VISIBLE);
                findViewById(R.id.bottom_navigation_admin).setVisibility(View.GONE);

                // Setup user bottom nav
                NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
            }

            // Sync title with nav controller
            NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        }

        // Handle drawer menu clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                int id = item.getItemId();

                if (id == R.id.nav_saved_flights) {
                    navController.navigate(R.id.savedFlightsFragment);
                } else if (id == R.id.nav_my_tickets) {
                    navController.navigate(R.id.SavedTicketsFragment);
                } else if (id == R.id.nav_support && "user".equalsIgnoreCase(role)) {
                    navController.navigate(R.id.supportFragment);
                }else if (id == R.id.nav_support && "admin".equalsIgnoreCase(role)) {
                    navController.navigate(R.id.adminSupportFragment);
                }
                else if (id == R.id.nav_notifications) {
                    navController.navigate(R.id.notificationFragment);
                }

                else if (id == R.id.nav_logout) {

                    AlertDialog.Builder Builder=new AlertDialog.Builder(MainActivity.this);
                    Builder.setTitle("Logout");
                    Builder.setMessage("Are you sure you want to logout?");
                    Builder.setPositiveButton("Yes", (dialog,which)->{

                        sessionManager.logout();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    });
                   Builder.setNegativeButton("No",null);
                   Builder.create().show();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
