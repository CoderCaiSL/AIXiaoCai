# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\csl\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#如果引用了v4或者v7包
-dontwarn android.support.**

-keepattributes EnclosingMethod
 #如果有其它包有warning，在报出warning的包加入下面类似的-dontwarn 报名
-dontwarn com.fengmap.*.**
## 注解支持
-keepclassmembers class *{
   void *(android.view.View);
}
#保护注解
-keepattributes *Annotation*
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆时是否记录日志
-verbose
#预校验
-dontpreverify
# 是否使用大小写混合
-dontusemixedcaseclassnames
#以下是retrofit+rxjava的混淆
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class com.example.csl.aixiaocai.enity.**{*;}##这是你定义的实体类
#百度识别的混淆
-keep class com.baidu.speech.**{*;}
-keep class com.baidu.tts.**{*;}
-keep class com.baidu.speechsynthesizer.**{*;}

