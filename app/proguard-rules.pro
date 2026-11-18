# ProGuard / R8 Rules for Dicosport App

##---------------Begin: proguard configuration for Attributes & Models ----------
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*, ElementValueAttribute

# Presentation Models
-keep class com.example.dicosportbyarafin.presentation.model.** { *; }

##---------------Begin: proguard configuration for Favourite Module ----------
-keep class com.example.favourite.** { *; }
-keepclassmembers class com.example.favourite.** { *; }

##---------------Begin: proguard configuration for ViewBinding ----------
-keep class **.databinding.** { *; }

##---------------Begin: proguard configuration for Retrofit & Moshi ----------
-dontwarn retrofit2.**
-dontwarn com.squareup.moshi.**

# Keep Retrofit API interfaces intact for suspend function reflection
-keep interface * {
    @retrofit2.http.* <methods>;
}

##---------------Begin: proguard configuration for Koin ----------
-dontwarn org.koin.**
-keep class org.koin.** { *; }

##---------------Begin: proguard configuration for Glide ----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
    *** rewind();
}

##---------------Begin: proguard configuration for Lottie & Parcelable ----------
-keep class com.airbnb.lottie.** { *; }

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
