buildscript {

    dependencies {
        classpath(libs.google.services)
        classpath(libs.gradle)
        classpath(libs.perf.plugin)
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}

