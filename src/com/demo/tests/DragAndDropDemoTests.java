package com.demo.tests;

import com.demo.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;


public class DragAndDropDemoTests {
    private WebDriver driver;
    private Utils utils;

    @Before
    public void beforeTest() {
        this.utils = Utils.getInstance();
    }

    @After
    public void afterTest() {
        utils.stopChrome();
    }

    @Test
    public void dragAndDropDemoUsingJavaScript() throws IOException {
        driver = utils.launchBrowser();

        driver.navigate().to("https://www.seleniumeasy.com/test/drag-and-drop-demo.html");

        for (int i = 0; i < 4; i++) {
            ((JavascriptExecutor) driver).executeScript(DragNDropUtil.dragNDrop("src/com/demo/utils/dragNDrop.js")
                    .replace("#sourceElem", "#todrag span")
                    .replace("#targetElem", "#mydropzone"));
            utils.waitFor(1);
        }

    }


}
