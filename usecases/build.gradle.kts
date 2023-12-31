plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "io.viesure.usecases"
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
    implementation(project(":entities"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.dagger:dagger:${project.properties["daggerVersion"]}")
    kapt("com.google.dagger:dagger-compiler:${project.properties["daggerVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${project.properties["coroutinesVersion"]}")
    implementation("com.jakewharton.timber:timber:${project.properties["timberVersion"]}")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:${project.properties["mockkVersion"]}")
    kaptTest("com.google.dagger:dagger-compiler:${project.properties["daggerVersion"]}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${project.properties["coroutinesVersion"]}")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
