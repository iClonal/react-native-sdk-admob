#import "RNSGADInterstitial.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif
#import "RNSGADCommon.h"

static NSString *const kEventAdLoaded = @"interstitialAdLoaded";
static NSString *const kEventAdFailedToLoad = @"interstitialAdFailedToLoad";
static NSString *const kEventAdOpened = @"interstitialAdOpened";
static NSString *const kEventAdFailedToOpen = @"interstitialAdFailedToOpen";
static NSString *const kEventAdClosed = @"interstitialAdClosed";
static NSString *const kEventAdLeftApplication = @"interstitialAdLeftApplication";

@implementation RNSGADInterstitial
{
    GADInterstitial  *_interstitial;
    NSString *_adUnitID;
    NSArray *_testDevices;
    RCTPromiseResolveBlock _requestAdResolve;
    RCTPromiseRejectBlock _requestAdReject;
    BOOL hasListeners;
}

- (id)init
{
    if (self == [super init]) {
        NSString* unitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_ADMOB_INTERSTITIAL_ID];
        if (!!unitId) {
            _adUnitID = unitId;
        } else {
            _adUnitID = TEST_INTERSTITIAL_ADUNIT_ID;
        }
    }
    return self;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
    return @[
             kEventAdLoaded,
             kEventAdFailedToLoad,
             kEventAdOpened,
             kEventAdFailedToOpen,
             kEventAdClosed,
             kEventAdLeftApplication ];
}

#pragma mark exported methods

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
    _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices)
{
    _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd)
{
    if (![_interstitial isReady] || [_interstitial hasBeenUsed] || _interstitial == nil) {
        _interstitial = [[GADInterstitial alloc] initWithAdUnitID:_adUnitID];
        _interstitial.delegate = self;
        
        GADRequest *request = [GADRequest request];
        request.testDevices = _testDevices;
        [_interstitial loadRequest:request];
    }
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if ([_interstitial isReady]) {
        [_interstitial presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve(@[[NSNumber numberWithBool:[_interstitial isReady]]]);
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
             @"simulatorId": kGADSimulatorID
             };
}

- (void)startObserving
{
    hasListeners = YES;
}

- (void)stopObserving
{
    hasListeners = NO;
}

#pragma mark GADInterstitialDelegate

- (void)interstitialDidReceiveAd:(__unused GADInterstitial *)ad
{
    if (hasListeners) {
        [self sendEventWithName:kEventAdLoaded body:nil];
    }
}

- (void)interstitial:(__unused GADInterstitial *)interstitial didFailToReceiveAdWithError:(GADRequestError *)error
{
    if (hasListeners) {
        NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
        [self sendEventWithName:kEventAdFailedToLoad body:jsError];
    }
}

- (void)interstitialWillPresentScreen:(__unused GADInterstitial *)ad
{
    if (hasListeners){
        [self sendEventWithName:kEventAdOpened body:nil];
    }
}

- (void)interstitialDidFailToPresentScreen:(__unused GADInterstitial *)ad
{
    if (hasListeners){
        [self sendEventWithName:kEventAdFailedToOpen body:nil];
    }
}

- (void)interstitialDidDismissScreen:(__unused GADInterstitial *)ad
{
    if (hasListeners) {
        [self sendEventWithName:kEventAdClosed body:nil];
    }
}

- (void)interstitialWillLeaveApplication:(__unused GADInterstitial *)ad
{
    if (hasListeners) {
        [self sendEventWithName:kEventAdLeftApplication body:nil];
    }
}

@end

