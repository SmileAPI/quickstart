//
//  SwiftUIView.swift
//  SmileWinkWidget
//
//  Created by Smile Wade on 2023/2/1.
//

import SwiftUI
import WebKit

 struct SwiftUIWebview : UIViewRepresentable {

     var url: URL?
     
     let contentController = ContentController()

     func makeUIView(context: Context) ->  WKWebView {
         return WKWebView()
     }

     func updateUIView(_ uiView: WKWebView, context: Context) {
         guard let url = url else{
             return
         }
         uiView.configuration.userContentController.add(contentController,name: "onAccountCreated")
         uiView.configuration.userContentController.add(contentController,name: "onAccountConnected")
         uiView.configuration.userContentController.add(contentController,name: "onAccountRemoved")
         uiView.configuration.userContentController.add(contentController,name: "onAccountError")
         uiView.configuration.userContentController.add(contentController,name: "onTokenExpired")
         uiView.configuration.userContentController.add(contentController,name: "onClose")
         uiView.configuration.userContentController.add(contentController,name: "onUploadsCreated")
         uiView.configuration.userContentController.add(contentController,name: "onUIEvent")
         uiView.load(URLRequest(url: url))
     }
     class ContentController: NSObject, WKScriptMessageHandler {
             func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
                print(message.body)
             }
         }

 }



