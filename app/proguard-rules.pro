-keepattributes Signature
-keep class * extends java.lang.annotation.Annotation { *; }
-keepattributes *Annotation*

-keepattributes *JavascriptInterface*

-keepclassmembers class com.weiyin.wysdk.activity.WYWebViewActivity$AndroidClient {
  public *;
}

-keep class com.weiyin.wysdk.model.** {*;}
-dontwarn com.weiyin.wysdk.model.**
-keepclassmembers class com.weiyin.wysdk.model {
    <fields>;
    <methods>;
}