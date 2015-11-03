# Jitter

Jitter is a sample Android app that works with Twitter APIs. It fetches timeline for userName entered
 in MainActivity, then it will try to fetch timeline and favorites for other users found in 
 userName's stream. 

## Application structure & flow

Application consists of three activities:

* In `MainActivity`, there is an EditText to enter Twitter used name, and search button
* Upon clicking search button with valid user name, `UsersActivity` will be opened, where user's
 timeline will be shown in a list. All retweeted messages will appear in the list under its original
 author's names, and it will be indicated by small retweet icon on top right corner.
* Clicking retweeted messages will open `FollowersActivity`, which consists of two tabs. First tab, 
 'Timeline' will show retweeted message author's timeline, while 'Favorites' tab will list favorite 
 messages of the same author.
* `TweetsFragment` is used as a reusable component, and it is used in `UsersActivity`, and 
 `FollowersActivity`s both tabs.

## Used external libraries
* [Retrofit](http://square.github.io/retrofit/) HTTP client for Android
* [Picasso](http://square.github.io/picasso/) Image  downloading and caching library
* [RxJava/RxAndroid](https://github.com/ReactiveX/RxJava) Library for composing asynchronous and 
event-based programs by using observable sequences.
* [Realm.io](https://realm.io/) Mobile database for Android & iOS
* [Dagger 2](http://google.github.io/dagger/) Dependency injection for Android
* [ButterKnife](http://jakewharton.github.io/butterknife/) Field and method binding for Android views
* [JUnit](http://junit.org/) Unit Testing library
* [Robotium](https://github.com/RobotiumTech/robotium) Android UI Testing

## Building/running the app
### With Gradle/adb

If you want to build the app from source code, first set `ANDROID_HOME` environment variable to your
current Android SDK location, for example:

    export ANDROID_HOME=/Users/qwerty/Library/Android/sdk

Next, execute the followings:

    git clone git@github.com:atabekm/Jitter.git
    cd Jitter/
    ./gradlew build

If your build is successful, you can install newly generated apk file to your device by running
the followings (your `adb` file should be located inside platform-tools folder of `ANDROID_HOME`):

    adb install -r app/build/outputs/apk/app-debug.apk

### With Android Studio
1. Check out project from Version Control -> GitHub -> Login with your GitHub account
2. Paste URI from clipboard to `Git Repository URL`, check `Parent Directory` and `Directory Name`,
click **Clone**
3. Just to build, select `Build -> Make Project` from menu
4. Or to run, select `Run -> Run 'app'` from menu

## Testing the app

Basic set of Unit and UI test have been implemented. 

### Unit testing

#### With Gradle

If you want to run unit tests of the app from source code, first set `ANDROID_HOME` environment
variable to your current Android SDK location, for example (you can skip all the commands till the
last one, if you have already the source code):

    export ANDROID_HOME=/Users/qwerty/Library/Android/sdk

Next, execute the followings:

    git clone git@github.com:atabekm/Jitter.git
    cd Jitter/
    ./gradlew test

#### With Android Studio

To run unit tests from Android Studio, execute following steps (skip first two steps, if your project
has been already opened in Android Studio):

1. Check out project from Version Control -> GitHub -> Login with your GitHub account
2. Paste URI from clipboard to `Git Repository URL`, check `Parent Directory` and `Directory Name`,
click **Clone**
3. Open `Build Variants` on left side toolbar, or View -> Tool Windows -> Build Variants
4. Select `Unit Tests` for Test Artifact
5. On Project window on the top left, find the directory `com.example.jitter (test)`
6. Tap with two fingers on the directory (right click on PC)
5. From opened context menu, select `Run 'Tests in 'com.example.jitter''`

### UI testing

#### With Gradle

If you want to run UI tests of the app from source code, first set `ANDROID_HOME` environment
variable to your current Android SDK location, for example (you can skip all the commands till the
last one, if you have already the source code):

    export ANDROID_HOME=/Users/qwerty/Library/Android/sdk

Next, prepare your device, and execute the followings:

    git clone git@github.com:atabekm/Jitter.git
    cd Jitter/
    ./gradlew connectedAndroidTest --info

#### With Android Studio

To run UI tests from Android Studio, execute following steps (skip first two steps, if your project
has been already opened in Android Studio):

1. Check out project from Version Control -> GitHub -> Login with your GitHub account
2. Paste URI from clipboard to `Git Repository URL`, check `Parent Directory` and `Directory Name`,
click **Clone**
3. Prepare your device
4. Open `Build Variants` on left side toolbar, or View -> Tool Windows -> Build Variants
5. Select `Android Instrumentation Tests` for Test Artifact
6. Go to Run -> Edit Configurations
7. Click + button on top left corner, and from popup select `Android Tests`
8. Default settings are usually fine, thus you can proceed with clicking OK button

## Screenshots

![device-2015-11-03-231401](https://cloud.githubusercontent.com/assets/8268042/10908024/a9d18900-8282-11e5-9f09-cd402d9f7f18.png) 

![device-2015-11-03-231440](https://cloud.githubusercontent.com/assets/8268042/10908025/a9d387be-8282-11e5-895e-d331c40f8123.png) 

![device-2015-11-03-231540](https://cloud.githubusercontent.com/assets/8268042/10908026/a9dc53da-8282-11e5-8be5-62ae4e1a23dd.png) 

![device-2015-11-03-231613](https://cloud.githubusercontent.com/assets/8268042/10908027/a9df9702-8282-11e5-9aa4-bea7821f4351.png) 

![device-2015-11-03-232242](https://cloud.githubusercontent.com/assets/8268042/10908028/a9e361ca-8282-11e5-84dc-696e6fc9ff50.png) 
