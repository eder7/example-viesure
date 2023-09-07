plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "io.viesure.adapters"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":usecases"))
    implementation(project(":entities"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.room:room-runtime:${project.properties["roomVersion"]}")
    implementation("androidx.room:room-ktx:${project.properties["roomVersion"]}")
    kapt("androidx.room:room-compiler:${project.properties["roomVersion"]}")
    implementation("com.google.dagger:dagger:${project.properties["daggerVersion"]}")
    kapt("com.google.dagger:dagger-compiler:${project.properties["daggerVersion"]}")
    implementation("com.squareup.retrofit2:retrofit:${project.properties["retrofitVersion"]}")
    implementation("com.squareup.retrofit2:converter-gson:${project.properties["retrofitVersion"]}")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.jakewharton.timber:timber:${project.properties["timberVersion"]}")

    testImplementation("junit:junit:4.13.2")
}