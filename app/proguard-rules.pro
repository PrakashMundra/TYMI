# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\prakash.mundra\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes Signature

-keep class kotlin.** { *; }
-dontwarn kotlin.**

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.google.gson.examples.android.model.**

-keep class com.firebase.** { *; }
-dontwarn com.firebase.**

-keep class com.tymi.entity.** { *; }
-keep enum com.tymi.enums.** { *; }
