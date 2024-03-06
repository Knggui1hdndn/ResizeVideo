import java.io.FileInputStream
import java.util.Properties
import com.android.build.OutputFile

plugins {
    id("kotlin-kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//     id("androidx.navigation.safeargs")
    id("androidx.navigation.safeargs.kotlin")
//    id ("com.google.gms.google-services")

}
val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
android {
    namespace = "com.tearas.resizevideo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tearas.resizevideo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GG_APP_OPEN", apikeyProperties["GG_APP_OPEN"] as String)
        buildConfigField("String", "GG_BANNER", apikeyProperties["GG_BANNER"] as String)
        buildConfigField("String", "GG_NATIVE", apikeyProperties["GG_NATIVE"] as String)
        buildConfigField("String", "GG_FULL", apikeyProperties["GG_FULL"] as String)
        buildConfigField("String", "GG_REWARDED", apikeyProperties["GG_REWARDED"] as String)

//        ndk {
//            abiFilters.add("armeabi-v7a")
//        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
    buildFeatures {
        viewBinding = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }


}




dependencies {

    implementation(files("libs/adshelper.aar"))
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.android.billingclient:billing-ktx:6.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
//    implementation("com.google.gms:google-services:4.4.0")
    implementation("com.github.Jay-Goo:RangeSeekBar:v3.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.arthenica:ffmpeg-kit-full-gpl:6.0-2")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    implementation("com.google.android.gms:play-services-measurement-api:21.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    val lifecycle_version = "2.3.1"
     implementation ("androidx.lifecycle:lifecycle-runtime:$lifecycle_version")
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
}