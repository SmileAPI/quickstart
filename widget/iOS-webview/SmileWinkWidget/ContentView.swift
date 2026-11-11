//
//  ContentView.swift
//  SmileWinkWidget
//
//  Created by Smile Wade on 2023/1/31.
//

import SwiftUI
import WebKit

struct ContentView: View {
    var body: some View {
        NavigationView{
            let path = Bundle.main.path(forResource: "index", ofType: ".html", inDirectory: "HTML_files")
            let url = URL(fileURLWithPath: path!)
            
            SwiftUIWebview(url: url)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
