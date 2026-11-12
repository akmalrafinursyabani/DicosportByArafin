# ProGuard / R8 Rules for Dicosport by Arafin

# Keep Domain Models, DTOs, Entities, and Presentation Models
-keep class com.example.core.domain.model.** { *; }
-keep class com.example.core.data.source.remote.response.** { *; }
-keep class com.example.core.data.source.local.entity.** { *; }
-keep class com.example.dicosportbyarafin.presentation.model.** { *; }

# Retrofit & Moshi
-keepclassmembers class * {
    @com.squareup.moshi.* <fields>;
    @com.squareup.moshi.* <methods>;
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# SQLCipher & Room
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-keep class androidx.room.** { *; }
-dontwarn net.sqlcipher.**

# Koin
-keep class io.insertkoin.** { *; }
-dontwarn io.insertkoin.**

# Glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.** { *; }

# Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
