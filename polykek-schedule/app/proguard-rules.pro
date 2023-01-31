# Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

#noinspection ShrinkerUnresolvedReference
-keep public class android.app.csdk.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keep public class com.android.feature.main.screen.menu.fragments.BottomSheetFragment
-keep public class com.android.feature.main.screen.saved.fragments.SavedItemsFragment
-keep public class com.android.feature.map.fragments.MapToolbarFragment
-keep public class argument.twins.com.polykekschedule.background.PolytechFirebaseMessagingService

# Data classes and enums
-keep class com.**.api.** { *; }
-keepclassmembers enum * { *; }

# TODO: I don't have time to search problem with proguard in new multi module version.
# Will return to it later. Until all secondary modules will be without obfuscation.
-keep class com.android.** { *; }

# Google review.
-keep class com.google.android.play.core.common.PlayCoreDialogWrapperActivity
-keep class com.google.android.play.core.review.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }

# Google gms ()
-keepnames class com.google.android.gms.** {*;}
-dontwarn com.google.android.gms.common.api.Api$zza
-dontwarn com.google.android.gms.common.api.Api$zzf
-dontwarn com.google.android.gms.common.api.internal.zzcl
-dontwarn com.google.android.gms.common.api.internal.zzcn
-dontwarn com.google.android.gms.common.api.internal.zzcp
-dontwarn com.google.android.gms.common.api.internal.zzct
-dontwarn com.google.android.gms.common.api.internal.zzdg
-dontwarn com.google.android.gms.common.api.internal.zzdp
-dontwarn com.google.android.gms.common.api.internal.zzg
-dontwarn com.google.android.gms.common.internal.zzbq
-dontwarn com.google.android.gms.internal.zzbej
-dontwarn com.google.android.gms.internal.zzbek
-dontwarn com.google.android.gms.internal.zzbem