plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\keystore.jks")
            keyAlias = "delicias"
            keyPassword = "Password@8462"
            storePassword = "Password@8462"
        }
        create("realease") {
            storeFile = file("D:\\keystore.jks")
            storePassword = "Password@8462"
            keyAlias = "delicias"
            keyPassword = "Password@8462"
        }
    }
    namespace = "com.equipo1.DeliciasUrbanas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.equipo1.DeliciasUrbanas"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.places)
    implementation(libs.play.services.fitness)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.paypal.sdk:paypal-android-sdk:2.16.0")

    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("androidx.fragment:fragment:1.3.4")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")

    implementation ("com.google.firebase:firebase-auth:21.0.6")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")

    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("com.google.android.libraries.places:places:2.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}