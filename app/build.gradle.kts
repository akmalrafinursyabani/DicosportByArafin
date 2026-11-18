plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.dicosportbyarafin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dicosportbyarafin"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true
        viewBinding = true
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
        checkDependencies = false
        ignoreTestSources = true
    }
}

tasks.configureEach {
    if (name.contains("lintVital", ignoreCase = true)) {
        enabled = false
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":favourite"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(libs.androidx.activity.ktx)

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // LeakCanary Memory Leak Detection (Debug only)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    // Coroutines & Lifecycle
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Glide & Lottie & Play Feature Delivery
    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.play.feature.delivery)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
