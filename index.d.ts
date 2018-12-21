import React from 'react';
import {
    ViewProps,
    EventSubscription,
} from 'react-native';

export interface GADBannerProps extends ViewProps {
    //adUnitId?: string;
    adSize?: "banner" | "largeBanner" | "mediumRectangle" | "fullBanner" | "leaderboard" | "smartBannerPortrait" | "smartBannerLandscape";
    testDevices?: string[];
    onSizeChange?: (layout: {width: number, height: number}) => void;
    onAdFailedToLoad?: () => void;
    onAdOpened?: () => void;
    onAdClosed?: () => void;
    onAdLeftApplication?: () => void;
}

export class GADBanner extends React.Component<GADBannerProps> {

}

type GADInterstitialEvent = "adLoaded" | "adFailedToLoad" | "adOpened" | "adClosed" | "adLeftApplication"
type GADInterstitialEventHandler = () => void

declare module GADInterstitial {
    //export function setAdUnitID(id: string) : void;
    export function setTestDevices(list: string[]): void;
    export function requestAd(): void;
    export function showAd(): Promise<void>;
    export function isReady(): Promise<boolean>;
    export function addEventListener(evt: GADInterstitialEvent, handler: GADInterstitialEventHandler): EventSubscription;
    export function removeEventListener(type: any, handler: GADInterstitialEventHandler): void;
    export function removeAllListeners(): void;
}


type GADRewardedEvent = "adLoaded" | "adFailedToLoad" | "adOpened" | "adClosed" | "adLeftApplication" | "rewarded" | "videoStarted"
type GADRewardedEventHandler = () => void

declare module GADRewarded {
    //export function setAdUnitID(id: string): void;
    export function setTestDevices(list: string[]): void;
    export function requestAd(): void;
    export function showAd(): Promise<void>;
    export function isReady(): Promise<boolean>;
    export function addEventListener(evt: GADRewardedEvent, handler: GADRewardedEventHandler): EventSubscription;
    export function removeEventListener(type: any, handler: GADRewardedEventHandler): void;
    export function removeAllListeners(): void;
}