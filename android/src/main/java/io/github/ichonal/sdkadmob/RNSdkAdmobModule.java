
package io.github.ichonal.sdkadmob;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNSdkAdmobModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNSdkAdmobModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNSdkAdmob";
    }
}