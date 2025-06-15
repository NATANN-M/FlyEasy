package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData; // For observing user data
import androidx.lifecycle.Observer;  // For observing user data
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.flyeasy.R;
import com.example.flyeasy.data.local.FlyEasyDatabase; // For DB instance
import com.example.flyeasy.data.local.dao.UserDao;    // For User DAO
import com.example.flyeasy.databinding.FragmentBookingBinding;
import com.example.flyeasy.model.BookingEntity;
import com.example.flyeasy.model.FlightEntity;
import com.example.flyeasy.model.UserEntity; // Import UserEntity
import com.example.flyeasy.util.SessionManager;   // Import SessionManager
import com.example.flyeasy.viewmodel.BookingViewModel;
// import com.google.type.Date; // This import is unused

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService; // For background tasks
import java.util.concurrent.Executors;       // For background tasks
import java.util.Date; // Use java.util.Date

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private FlightEntity selectedFlight;
    private BookingViewModel bookingViewModel;
    private NavController navController;

    // For user data
    private SessionManager sessionManager;
    private UserDao userDao;
    private ExecutorService executorService; // To run DB operations off the main thread

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        sessionManager = new SessionManager(requireContext());
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(requireContext()); // Get DB instance
        userDao = db.userDao(); // Get UserDao
        executorService = Executors.newSingleThreadExecutor(); // Initialize executor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return root from binding
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        // Load flight details
        if (getArguments() != null) {
            selectedFlight = (FlightEntity) getArguments().getSerializable("flight");
            if (selectedFlight != null) {
                binding.textFlightOrigin.setText("From: " + selectedFlight.origin);
                binding.textFlightDestination.setText("To: " + selectedFlight.destination);

                if (binding.textAirline != null) { // Check if the old ID exists
                    binding.textAirline.setText("Airline: " + selectedFlight.airline);
                }
                if (binding.textDeparture != null) { // Check if the old ID exists
                    binding.textDeparture.setText("Departure: " + selectedFlight.departureTime);
                }
                binding.textPrice.setText("Price: $" + selectedFlight.price);
            }
        }

        //  autofill user data if logged in
        attemptAutofillUserData();

        binding.btnConfirmBooking.setOnClickListener(v -> {
            String name = binding.editName.getText().toString().trim();
            String email = binding.editEmail.getText().toString().trim();
            String phone = binding.editPhone.getText().toString().trim();
            String passportNumber = binding.editpassportNumber.getText().toString().trim();


            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(passportNumber)) {
                Toast.makeText(getContext(), "Please fill all fields, including passport number.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedFlight == null) {
                Toast.makeText(getContext(), "No flight selected. Please go back and select a flight.", Toast.LENGTH_LONG).show();
                return;
            }

            BookingEntity booking = new BookingEntity();
            booking.setUserId(sessionManager.getUserId());
            booking.setFlightId(selectedFlight.id);
            booking.setFullName(name);
            booking.setEmail(email);
            booking.setPhone(phone);
            booking.setPassportNumber(passportNumber);
            booking.setBookingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            booking.setSeatsBooked(1);

            booking.setFlightOrigin(selectedFlight.origin);
            booking.setFlightDestination(selectedFlight.destination);
            booking.setFlightAirlineName(selectedFlight.airline);
            booking.setFlightDepartureTime(selectedFlight.departureTime);
            booking.setFlightPriceAtBooking(selectedFlight.price);

            bookingViewModel.insertBooking(booking);

            Toast.makeText(getContext(), "Booking confirmed!", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
        });
    }

    private void attemptAutofillUserData() {
        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();
            if (userId != -1) {

                LiveData<UserEntity> userLiveData = userDao.getUserById(userId);
                userLiveData.observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
                    @Override
                    public void onChanged(@Nullable UserEntity user) {
                        if (user != null) {
                            binding.editName.setText(user.fullName);
                            binding.editEmail.setText(user.email);
                            binding.editPhone.setText(user.phone);

                            userLiveData.removeObserver(this);
                        } else {
                            Toast.makeText(getContext(), "Could not load user data to autofill.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            binding.editName.setText("");
            binding.editEmail.setText("");
            binding.editPhone.setText("");
            binding.editpassportNumber.setText("");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown(); // Shutdown executor
        }
    }
}