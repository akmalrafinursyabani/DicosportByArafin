plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.favourite"
    compileSdk = 34

    defaultConfig {
        minSdk = 27
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Glide
    implementation(libs.glide)
}
