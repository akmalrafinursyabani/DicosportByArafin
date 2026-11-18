plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 27
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Coroutines & Lifecycle
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Retrofit & Moshi
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.okhttp.logging)

    // Room & SQLCipher Encryption
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation("net.zetetic:android-database-sqlcipher:4.5.4")
    implementation("androidx.sqlite:sqlite-ktx:2.4.0")

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Glide
    implementation(libs.glide)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
