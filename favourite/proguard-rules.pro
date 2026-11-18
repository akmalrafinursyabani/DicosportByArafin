# ProGuard / R8 Rules for Favourite Dynamic Feature Module

-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*, ElementValueAttribute

-keep public class com.example.favourite.FavouriteActivity {
    public <init>(...);
}
-keep class com.example.favourite.** { *; }
-keepclassmembers class com.example.favourite.** { *; }
-keep class com.example.favourite.di.** { *; }
-keepclassmembers class com.example.favourite.di.** { *; }
-keep class com.example.favourite.databinding.** { *; }
-keepclassmembers class com.example.favourite.databinding.** { *; }
