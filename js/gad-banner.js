'use strict';

import React from 'react';
import {
    requireNativeComponent,
    UIManager,
    findNodeHandle,
} from 'react-native';

import {createErrorFromErrorData} from './utils';

class GADBanner extends React.Component {
    constructor(props) {
        super(props);
        this.loadBanner = this._loadBanner.bind(this);
        this.onSizeChange = this._onSizeChange.bind(this);
        this.adFailedToLoad = this._adFailedToLoad.bind(this);
        this.state = {
            style: {},
        };
        this._bannerViewRef = null;
    }

    componentDidMount() {
        this._loadBanner();
    }

    render() {
        return (
            <RNSGADBannerView
                {...this.props}
                style={[this.props.style, this.state.style]}
                onSizeChange={this.onSizeChange}
                onAdFailedToLoad={this.adFailedToLoad}
                ref={ref => this._bannerViewRef = ref}
            />
        );
    }

    _loadBanner() {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this._bannerViewRef),
            UIManager.RNSGADBannerView.Commands.loadBanner,
            null,
        );
    }

    _onSizeChange(evt) {
        const {
            nativeEvent: {
                width,
                height,
            }
        } = evt;
        const {onSizeChange} = this.props;
        this.setState((prevState)=>{
            return { 
                style: {width, height} 
            };
        });
        !!onSizeChange && onSizeChange({width, height});
    }

    _adFailedToLoad(evt) {
        const {onAdFailedToLoad} = this.props;
        const {
            nativeEvent: {
                error,
            }
        } = evt;
        !!onAdFailedToLoad && onAdFailedToLoad(createErrorFromErrorData(error));
    }
};

const RNSGADBannerView = requireNativeComponent("RNSGADBannerView", GADBanner);

export default GADBanner;