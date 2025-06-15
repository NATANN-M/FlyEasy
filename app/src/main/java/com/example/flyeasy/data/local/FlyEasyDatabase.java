package com.example.flyeasy.data.local;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;

import com.example.flyeasy.data.local.dao.*;
import com.example.flyeasy.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                UserEntity.class,
                FlightEntity.class,
                AirlineEntity.class,
                BookingEntity.class,
                TicketEntity.class,
                NotificationEntity.class,
                SavedFlightEntity.class,
                SupportMessageEntity.class
        },
        version = 3,
        exportSchema = false
)
public abstract class FlyEasyDatabase extends RoomDatabase {

    private static volatile FlyEasyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // DAOs3
    public abstract FlightDao flightDao();
    public abstract AirlineDao airlineDao();
    public abstract BookingDao bookingDao();
    public abstract TicketDao ticketDao();
    public abstract NotificationDao notificationDao();
    public abstract SavedFlightDao savedFlightDao();
    public abstract UserDao userDao();
    public abstract SupportDao supportDao();
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE bookings ADD COLUMN flightOrigin TEXT");
            database.execSQL("ALTER TABLE bookings ADD COLUMN flightDestination TEXT");
            database.execSQL("ALTER TABLE bookings ADD COLUMN flightAirlineName TEXT");
            database.execSQL("ALTER TABLE bookings ADD COLUMN flightDepartureTime TEXT");
            database.execSQL("ALTER TABLE bookings ADD COLUMN flightPriceAtBooking REAL NOT NULL DEFAULT 0.0");
        }
    };

    public static FlyEasyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlyEasyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    FlyEasyDatabase.class,
                                    "flyeasy_database"
                            )
                            .addMigrations(MIGRATION_2_3)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        // Insert default admin
                                        UserEntity admin = new UserEntity();
                                        admin.fullName = "Admin User";
                                        admin.email = "admin@flyeasy.com";
                                        admin.password = "admin123";
                                        admin.phone = "0000000000";
                                        admin.role = "admin";
                                        admin.profileImageUri = "";

                                        getDatabase(context).userDao().insertUser(admin);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
