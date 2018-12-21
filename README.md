# react-native-sdk-admob

## Getting started

`$ npm install git+https://github.com/iChonal/react-native-sdk-admob.git --save`

or

`$ yarn add https://github.com/iChonal/react-native-sdk-admob.git`

### iOS

#### Installation

- Automatic (using cocoapods)

`$ react-native link react-native-sdk-admob`

or

add `pod 'react-native-sdk-admob', :path => '../node_modules/react-native-sdk-admob'` to your **Podfile**

then run `$ pod install`

- Manual

  1. install AdMob Sdk for iOS, see [Google AdMob](https://developers.google.com/admob/ios/quick-start)
  2. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
  3. Go to `node_modules` ➜ `react-native-sdk-admob` and add `RNSdkAdmob.xcodeproj`
  4. In XCode, in the project navigator, select your project. Add `libRNSdkAdmob.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`

#### Configuration

add **AdUnitId** to **Info.plist** file

- banner

```xml
    <key>GADBannerAdUnitId</key>
    <string>"YOUR_ADMOB_BANNER_ADUNIT_ID"</string>
```

- Interstitial

```xml
    <key>GADInterstitialAdUnitId</key>
    <string>"YOUR_ADMOB_INTERSTITIAL_ADUNIT_ID"</string>
```

- Rewarded Video

```xml
    <key>GADRewardedAdUnitId</key>
    <string>"YOUR_ADMOB_REWARDED_ADUNIT_ID"</string>
```

#### Initialization

Before loading ads, apps should initialize the Mobile Ads SDK by calling the `configureWithApplicationID:` class method in `GADMobileAds`, and passing it their AdMob App ID. This only needs to be done once, ideally at app launch. You can find the App ID for your app in the AdMob UI.

Here's an example of how to call the `configureWithApplicationID:` method in your **AppDelegate**:

```obj-c
- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

  // Initialize Google Mobile Ads SDK
  // Sample AdMob app ID: ca-app-pub-3940256099942544~1458002511
  [GADMobileAds configureWithApplicationID:@"YOUR_ADMOB_APP_ID"];
  return YES;
}
```

### Android

#### Installation

- Automatic

`$ react-native link react-native-sdk-admob`

- Manual

1. Append the following lines to `android/settings.gradle`:

  	```gradle
  	include ':react-native-sdk-admob'
  	project(':react-native-sdk-admob').projectDir = new File(rootProject.projectDir,  '../node_modules/react-native-sdk-admob/android')
  	```

2. Insert the following lines inside the dependencies block in `android/app/build.gradle`:

  	```gradle
      implementation project(':react-native-sdk-admob')
  	```


3. Open up `android/app/src/main/java/[...]/MainApplication.java`

   - Add `import io.github.ichonal.sdkadmob.RNSdkAdmobPackage;` to the imports at the top of the file
   - Add `new RNSdkAdmobPackage()` to the list returned by the `getPackages()` method

#### Configuration

- AppId

Add your AdMob App ID to your app's AndroidManifest.xml file by adding the `<meta-data>` tag shown below. You can find your App ID in the AdMob UI. For android:value insert your own AdMob App ID in quotes, as shown below.

```xml
<manifest>
    <application>
        <!-- Sample AdMob App ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="[ADMOB_APP_ID]"/>
    </application>
</manifest>
```

> **Important**: This step is required as of Google Mobile Ads SDK version 17.0.0. Failure to add this `<meta-data>` tag results in a crash with the message: "The Google Mobile Ads SDK was initialized incorrectly."

- AdUnitId: Banner

```xml
<meta-data
      android:name="io.github.ichonal.sdkadmob.GADBANNER_ID"
      android:value="[ADMOB_BANNER_ADUNIT_ID]"/>
```

- AdUnitId: Interstitial 

```xml
<meta-data
      android:name="io.github.ichonal.sdkadmob.GADINTERSTITIAL_ID"
      android:value="[ADMOB_INTERSTITIAL_ADUNIT_ID]"/>
```

- AdUnitId: Rewarded Video 

```xml
<meta-data
      android:name="io.github.ichonal.sdkadmob.GADREWARDED_ID"
      android:value="[ADMOB_REWARDED_ADUNIT_ID]"/>
```

#### Initialization

Before loading ads, have your app initialize the Mobile Ads SDK by calling `MobileAds.initialize()` with your AdMob App ID. This needs to be done only once, ideally at app launch.

Here's an example of how to call the `initialize()` method in an **Activity**:

```java
package ...
import ...
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    ...
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "YOUR_ADMOB_APP_ID");
    }
    ...
}
```

## Usage

```javascript
import {
    GADBanner,
    GADInterstitial,
    GADRewarded,
} from 'react-native-sdk-admob';
```

## DOC

### GADBanner


### GADInterstitial


### GADRewarded


## ACKNOWLEDGEMENT

* [react-native-admob](https://github.com/sbugert/react-native-admob) by @sbugert