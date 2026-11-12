# Consumer ProGuard Rules for Core Library

-keep class com.example.core.domain.model.** { *; }
-keep class com.example.core.data.source.remote.response.** { *; }
-keep class com.example.core.data.source.local.entity.** { *; }

-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-dontwarn net.sqlcipher.**
