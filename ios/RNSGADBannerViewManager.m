#import "RNSGADBannerViewManager.h"
#import "RNSGADBannerView.h"
#import "RCTConvert+GADAdSize.h"

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>
#else
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTEventDispatcher.h"
#endif

@implementation RNSGADBannerViewManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
    return [RNSGADBannerView new];
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

RCT_EXPORT_METHOD(loadBanner:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNSGADBannerView *> *viewRegistry) {
        RNSGADBannerView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNSGADBannerView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNGADBannerView, got: %@", view);
        } else {
            [view loadBanner];
        }
    }];
}

RCT_EXPORT_VIEW_PROPERTY(testDevices, NSArray)
RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLoaded, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdFailedToLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdOpened, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClosed, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLeftApplication, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(adSize, GADAdSize, RNSGADBannerView) {
    view.adSize = json == nil ? kGADAdSizeBanner : [RCTConvert GADAdSize:json];
}

RCT_CUSTOM_VIEW_PROPERTY(adUnitId, NSString, RNSGADBannerView) {
    view.adUnitId = json == nil ? nil : [RCTConvert NSString:json];
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
        @"simulatorId": kGADSimulatorID
    };
}

@end

