import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-kapt")     // <- ต้องมี
    id("com.google.dagger.hilt.android")     // <- ต้องมี


}


android {
    namespace = "com.nilezia.myweather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nilezia.myweather"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"96a8ea9a212673dfe1dd4b8610fc6e9f\"")
    }
    android.buildFeatures.buildConfig = true
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
        compose = true
        viewBinding = true
    }
    // แก้ไฟล์ซ้ำ
    packaging {
        resources {
            excludes.add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.material3:material3:1.2.0")
//
    //AndroidX
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Compose Accompanist Permissions
    implementation(libs.accompanist.permissions)
    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    kapt("com.google.dagger:hilt-compiler:2.57.1")

    implementation(libs.hilt.navigation.compose)

    //Google Play Service
    implementation(libs.play.services.location)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp3.logging)

    implementation(libs.coil.compose)
    implementation(libs.font.awesome)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}