buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}



plugins {
    id("com.android.application") version "8.2.1" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}


