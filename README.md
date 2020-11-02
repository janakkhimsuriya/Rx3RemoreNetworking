# Rx3RemoreNetworking

[![](https://jitpack.io/v/janakkhimsuriya/Rx3RemoreNetworking.svg)](https://jitpack.io/#janakkhimsuriya/Rx3RemoreNetworking)

### About Rx3RemoreNetworking Library
Rx3RemoreNetworking Library is a powerful library for doing any type of networking in Android applications which is made on top of OkHttp Networking Layer.

Rx3RemoreNetworking Library takes care of each and everything. So you don't have to do anything, just make request and listen for the response.

### Why use Rx3RemoreNetworking ?
* Recent removal of HttpClient in Android Marshmallow(Android M) made other networking libraries obsolete.
* No other single library does each and everything like making request, downloading any type of file, uploading file, loading
  image from network in ImageView, etc. There are some libraries but they are outdated.
* No other library provides simple interface for doing all types of things in networking like setting priority, cancelling, etc.
* As it uses [Okio](https://github.com/square/okio) , No more GC overhead in android applications.
  [Okio](https://github.com/square/okio) is made to handle GC overhead while allocating memory.
  [Okio](https://github.com/square/okio) does some clever things to save CPU and memory.
* It uses [OkHttp](http://square.github.io/okhttp/) , more importantly it supports HTTP/2.  

### RxJava3 Support

## Requirements

Rx3RemoreNetworking Library can be included in any Android application. 

Rx3RemoreNetworking Library supports Android 2.3 (Gingerbread) and later. 


## Using Rx3RemoreNetworking Library in your application

Add this in your build.gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.janakkhimsuriya:Rx3RemoreNetworking:SNAPSHOT'
}
```
Do not forget to add internet permission in manifest if already not present
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Then initialize it in onCreate() Method of application class :
```java
AndroidNetworking.initialize(getApplicationContext());
```
Initializing it with some customization , as it uses [OkHttp](http://square.github.io/okhttp/) as networking layer, you can pass custom okHttpClient while initializing it.
```java
// Adding an Network Interceptor and Curl Interceptor for Debugging purpose :
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(new CurlLoggerInterceptor()).build();
                
AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
```

Using the Rx3RemoreNetworking with Jackson Parser
```java
// Set the JacksonParserFactory like below
AndroidNetworking.setParserFactory(new JacksonParserFactory());
```

### Contact - Let's become friend
- [Twitter](https://twitter.com/Jashita9293)
- [Github](https://github.com/janakkhimsuriya)
- [Facebook](https://www.facebook.com/khimsuriya.janak)
