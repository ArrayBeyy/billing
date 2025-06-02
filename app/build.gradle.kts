plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.billing"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.billing"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.json:json:20231013")

    //json converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))

    //chucker
    debugImplementation ("com.github.chuckerteam.chucker:library:4.1.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:4.1.0")

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}