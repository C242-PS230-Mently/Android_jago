plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "id.co.mentalhealth"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.co.mentalhealth"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://mently-api-861370546933.asia-southeast2.run.app\"")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.core.animation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.android.material:material:1.8.0")

//    retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // data store
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Untuk ViewModel dan LiveData yang lebih terintegrasi
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

//    barchart
    implementation("com.diogobernardino:williamchart:3.10.1")
//    implementation ("com.github.SeptiawanAjiP:AndroidChartJS:1.0.5")

//    Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // Profile
    implementation(libs.circleimageview)
    implementation(libs.androidx.exifinterface)

}