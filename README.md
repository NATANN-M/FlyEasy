# ✈️ FlyEasy – Airline Ticket Booking App

**FlyEasy** is a modern airline ticketing app built in **Java** using **MVVM architecture**, **Room** for local data persistence, **Retrofit** for REST API integration, and **Jetpack Navigation** for smooth user flow. The app supports both **user and admin roles**, dynamic UI, QR code ticketing, and image uploads.

---

## 📱 Features

### 👤 User Features
- ✈️ Search and view real-time flight data via API
- 🔖 Save favorite flights locally
- 📅 Book flights and save booking details
- 🎫 View booked tickets with QR code
- 🧾 View and edit profile (including profile image)
- 🔔 Receive notifications and support messages
- 💬 In-app support system

### 🛠️ Admin Features
- 🧑‍💼 Admin login with role detection
- 👥 Manage users (edit, delete, view bookings)
- ✈️ Manage flights (import API flights, manually add, edit, delete)
- 📝 View and manage all bookings
- 🔔 Send notifications to users
- 💬 Reply to support messages

---

## 🧱 Architecture

This app follows **MVVM (Model-View-ViewModel)** architecture:

- **Model**: `Room` entities (e.g., `UserEntity`, `BookingEntity`), DAOs, and Retrofit API models.
- **ViewModel**: Lifecycle-aware components to expose LiveData to UI.
- **View**: Fragments and Activities observing ViewModel data.

### 🗂️ Folder Structure
com.flyeasy.airlineticketing
├── data
│ ├── local # Room DB & DAO
│ ├── remote # Retrofit API
│ └── repository # Data repository (API + DB)
├── model # Entity & API models
├── ui
│ ├── activities # Login, MainActivity, Splash
│ └── fragments # Booking, Profile, Admin dashboard
├── viewmodel # All ViewModel classes
├── util # SessionManager, constants, helpers


---

## 🔧 Tech Stack

- **Java** – Primary language
- **Room** – Local database
- **Retrofit2** – REST API client
- **MVVM** – Architectural pattern
- **LiveData & ViewModel** – Jetpack Architecture components
- **Jetpack Navigation** – For in-app navigation
- **Glide** – For image loading
- **QR Code Generator** – For ticketing
- **BottomNavigationView + Drawer** – Dual navigation UI
- **SharedPreferences** – Session persistence

---

## 🧪 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/flyeasy.git
Open in Android Studio.

Configure:

Retrofit base URL in ApiClient.java

API Key ( required for flight API) on FlightRepository change the API key

Permissions in AndroidManifest.xml

Build & Run on Android Emulator or physical device.


📸 Screenshots


Dashbord User

![image](https://github.com/user-attachments/assets/4fba8408-4284-4503-895d-2eb6ff3dbfa0)


Booking And SerchFlight 

![image](https://github.com/user-attachments/assets/f91a8895-c0a6-4414-8f43-f133227e8a01)

![image](https://github.com/user-attachments/assets/b2cb96d1-d2d8-4758-98f6-5d031abbc54e)


Mybooked flight 


![image](https://github.com/user-attachments/assets/138dc329-943d-4958-b06c-6d0e237d39f9)


Ticket And QR

![image](https://github.com/user-attachments/assets/1969c4f7-d57b-48cc-844a-e8af082b1608)

Admin Dashbord 


![image](https://github.com/user-attachments/assets/86cff9c1-944e-46a3-8f57-a9e1eb3c07b2)



Admin Flight management  

![image](https://github.com/user-attachments/assets/f872497b-4396-4914-999e-c1b4abe32308)

Admin Manage Users

![image](https://github.com/user-attachments/assets/afe586ad-735b-46fb-a58a-5b157c9f42a8)



Support Message to Admin


![image](https://github.com/user-attachments/assets/ad361be6-0a01-4a8d-a0b2-bc232d84b985)


My Tickets 

![image](https://github.com/user-attachments/assets/b649f97e-53a6-4678-b100-32c5f6201785)





