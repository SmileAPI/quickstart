//
//  ShieldDemoUITests.swift
//  ShieldDemoUITests
//

import XCTest

final class ShieldDemoUITests: XCTestCase {

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    func testExample() throws {
        let app = XCUIApplication()
        app.launch()
    }
}
