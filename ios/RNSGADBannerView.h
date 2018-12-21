#if __has_include(<React/RCTView.h>)
#import <React/RCTView.h>
#else
#import "RCTView.h"
#endif

#import <GoogleMobileAds/GoogleMobileAds.h>

@class RCTEventDispatcher;

@interface RNSGADBannerView : RCTView <GADBannerViewDelegate, GADAdSizeDelegate>

@property (nonatomic, assign) GADAdSize adSize;
@property (nonatomic, copy) NSString *adUnitId;
@property (nonatomic, copy) NSArray *testDevices;

@property (nonatomic, copy) RCTBubblingEventBlock onSizeChange;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLoaded;
@property (nonatomic, copy) RCTBubblingEventBlock onAdFailedToLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onAdOpened;
@property (nonatomic, copy) RCTBubblingEventBlock onAdClosed;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLeftApplication;

- (void)loadBanner;

@end
