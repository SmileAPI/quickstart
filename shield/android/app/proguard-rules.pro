# Keep the JS bridge methods reachable from WebView.
-keepclassmembers class com.smile.shielddemo.ShieldJsBridge {
    public *;
}
