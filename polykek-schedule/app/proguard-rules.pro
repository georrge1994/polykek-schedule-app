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

##********************************* Retrofit ******************************************************
# Base
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Keep XmlResourceParser.
-dontwarn org.xmlpull.v1.**
-dontwarn org.kxml2.io.**
-dontwarn android.content.res.**
-dontwarn org.slf4j.impl.StaticLoggerBinder
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }
-keep class org.simpleframework.xml.** { *; }

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#**************************** Polykek app *********************************************************
-keep public class com.android.feature.main.screen.menu.fragments.BottomSheetFragment
-keep public class com.android.feature.main.screen.saved.fragments.SavedItemsFragment
-keep public class com.android.feature.map.fragments.MapToolbarFragment
-keep public class argument.twins.com.polykekschedule.background.PolytechFirebaseMessagingService

# Data classes and enums
-keep class com.**.**Response { *; }
-keep class com.**.Rss** { *; }
-keepclassmembers enum * { *; }

# Google view.
-keep class com.google.android.play.core.common.PlayCoreDialogWrapperActivity
-keep class com.google.android.play.core.review.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }
-keep class androidx.appcompat.widget.SearchView { *; }

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