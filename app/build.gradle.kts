plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safe.args)
}


android {
    namespace = "com.example.flyeasy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flyeasy"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildFeatures{

            viewBinding=true
dataBinding=true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)

    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Room
    // Room
    implementation(libs.room.runtime)
    implementation(libs.protolite.well.known.types)
    implementation(libs.media3.common)
    annotationProcessor(libs.room.compiler)

// Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

// Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

// Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

// OkHttp Logging
    implementation(libs.okhttp.logging)


    // Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // QR Code (ZXing)
    implementation(libs.zxing.android.embedded)

// Material Design
    implementation("com.google.android.material:material:1.11.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation("androidx.cardview:cardview:1.0.0")

   

}


