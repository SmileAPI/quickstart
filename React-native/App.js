/* eslint-disable react-native/no-inline-styles */
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {Component} from 'react';
import {WebView} from 'react-native-webview';
import {Platform, Image} from 'react-native';
import {SafeAreaView, StyleSheet, useColorScheme} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

class App extends Component {
  constructor(props) {
    super(props);
    this.webView = null;
  }

  onMessage = e => {
    console.log('WebView onMessage:', e.nativeEvent.data);
    let params = e.nativeEvent.data;
    params = JSON.parse(params);
    console.log('WebView onMessage:', params);
  };

  onLoadEnd = async e => {
    // console.log('WebView onLoadEnd e：', e.nativeEvent);
    let data = null;
    data = await fetch('http://10.0.35.71:8000/api/create_link_token').then(
      response => response.json(),
    );
    this.webView && this.webView.postMessage(JSON.stringify(data));
  };

  render() {
    let source =
      Platform.OS === 'ios'
        ? require('./index.html')
        : {
            uri: 'file:///android_asset/index.html',
          };
    return (
      <SafeAreaView style={{flex: 1}}>
        <WebView
          originWhitelist={['*']}
          source={source}
          allowFileAccess={true}
          allowingReadAccessToURL="*"
          domStorageEnabled={true}
          javaScriptEnabled={true}
          allowUniversalAccessFromFileURLs={true}
          decelerationRate="normal"
          scrollEnabled={true}
          useWebKit={true}
          mediaPlaybackRequiresUserAction={true}
          mixedContentMode="compatibility"
          androidHardwareAccelerationDisabled={false}
          androidLayerType={'hardware'}
          style={{
            marginTop: Platform.OS !== 'ios' ? null : 20,
            flex: 1,
            backgroundColor: '#fff',
          }}
          ref={ref => (this.webView = ref)}
          onLoadEnd={async () =>
            Platform.OS !== 'ios' ? await this.onLoadEnd() : null
          }
          onMessage={e => (Platform.OS !== 'ios' ? this.onMessage(e) : null)}
        />
      </SafeAreaView>
    );
  }
}

export default App;
