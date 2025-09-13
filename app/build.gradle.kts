plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Agrega el complemento de Google Services
    id("com.google.gms.google-services")  // Aplica el complemento aquí
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
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Importa el BoM (Bill of Materials) de Firebase para mantener las versiones compatibles
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // Firebase Analytics (Ejemplo)
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-auth-ktx")


}