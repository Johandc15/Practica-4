plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.gms.google-services")
    id("kotlin-kapt") // <- NECESARIO para el compiler de Glide
}

android {
    namespace = "com.ucentral.proyectopractica3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ucentral.proyectopractica3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        defaultConfig { vectorDrawables.useSupportLibrary = true }

    }

    buildFeatures { viewBinding = true }

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
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    // --- AndroidX / Material ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- Firebase BoM (debe ir ANTES de las libs de Firebase) ---
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // --- Firebase (usar artefactos sin versión; BoM fija versiones compatibles) ---
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-database")      // si lo usas
    implementation("com.google.firebase:firebase-storage-ktx")   // <- PARA AVATAR

    // --- UI avatar e imágenes ---
    implementation("de.hdodenhof:circleimageview:3.1.0")        // avatar circular
    implementation("com.github.bumptech.glide:glide:4.16.0")     // carga de imagen
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

