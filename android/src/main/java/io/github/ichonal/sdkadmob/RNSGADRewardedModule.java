package io.github.ichonal.sdkadmob;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

public class RNSGADRewardedModule extends ReactContextBaseJavaModule implements RewardedVideoAdListener {
    public static final String REACT_CLASS = "RNSGADRewarded";

    public static final String EVENT_AD_LOADED = "rewardedVideoAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "rewardedVideoAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "rewardedVideoAdOpened";
    public static final String EVENT_AD_CLOSED = "rewardedVideoAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "rewardedVideoAdLeftApplication";
    public static final String EVENT_REWARDED = "rewardedVideoAdRewarded";
    public static final String EVENT_VIDEO_STARTED = "rewardedVideoAdVideoStarted";

    RewardedVideoAd mRewardedVideoAd;
    String adUnitID;
    String[] testDevices;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNSGADRewardedModule(ReactApplicationContext reactContext) {
        super(reactContext);

        try {
            PackageManager pm = reactContext.getPackageManager();
            String pkgName = reactContext.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitID = pInfo.metaData.getString(RNSGADCommon.META_KEY_REWARDED_ID);
            if (adUnitID == null) {
                adUnitID = RNSGADCommon.TEST_REWARDED_ADUNIT_ID;
            }
            this.adUnitID = adUnitID;
        } catch (PackageManager.NameNotFoundException e) {
            //TODO: WARNING
            this.adUnitID = RNSGADCommon.TEST_REWARDED_ADUNIT_ID;
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        WritableMap reward = Arguments.createMap();

        reward.putInt("amount", rewardItem.getAmount());
        reward.putString("type", rewardItem.getType());

        sendEvent(EVENT_REWARDED, reward);
    }

    @Override
    public void onRewardedVideoCompleted() {
        //TODO:
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        sendEvent(EVENT_AD_LOADED, null);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        sendEvent(EVENT_AD_OPENED, null);
    }

    @Override
    public void onRewardedVideoStarted() {
        sendEvent(EVENT_VIDEO_STARTED, null);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        sendEvent(EVENT_AD_CLOSED, null);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        sendEvent(EVENT_AD_LEFT_APPLICATION, null);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        String errorString = "ERROR_UNKNOWN";
        String errorMessage = "Unknown error";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorString = "ERROR_CODE_INTERNAL_ERROR";
                errorMessage = "Internal error, an invalid response was received from the ad server.";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorString = "ERROR_CODE_INVALID_REQUEST";
                errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorString = "ERROR_CODE_NETWORK_ERROR";
                errorMessage = "The ad request was unsuccessful due to network connectivity.";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorString = "ERROR_CODE_NO_FILL";
                errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                break;
        }
        WritableMap event = Arguments.createMap();
        WritableMap error = Arguments.createMap();
        event.putString("message", errorMessage);
        sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void setAdUnitID(String adUnitID) {
        this.adUnitID = adUnitID;
    }

    @ReactMethod
    public void setTestDevices(ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        this.testDevices = list.toArray(new String[list.size()]);
    }

    @ReactMethod
    public void requestAd() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Context ctx = RNSGADRewardedModule.this.getCurrentActivity();
                if (ctx == null) {
                    ctx = RNSGADRewardedModule.this.getReactApplicationContext();
                }
                RNSGADRewardedModule.this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(ctx);

                RNSGADRewardedModule.this.mRewardedVideoAd.setRewardedVideoAdListener(RNSGADRewardedModule.this);

                if (!mRewardedVideoAd.isLoaded()) {
                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            adRequestBuilder.addTestDevice(testDevices[i]);
                        }
                    }

                    AdRequest adRequest = adRequestBuilder.build();
                    mRewardedVideoAd.loadAd(adUnitID, adRequest);
                }
            }
        });
    }

    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if ((mRewardedVideoAd != null) && mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    promise.resolve(null);
                } else {
                    promise.reject("E_AD_NOT_READY", "Ad is not ready.");
                }
            }
        });
    }

    @ReactMethod
    public void isReady(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                promise.resolve((mRewardedVideoAd != null) && mRewardedVideoAd.isLoaded());
            }
        });
    }
}
