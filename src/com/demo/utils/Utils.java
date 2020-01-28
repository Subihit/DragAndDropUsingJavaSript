package com.demo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Utils {
    private Utils Utils;
    private String os;
    private ChromeDriverService service;
    private WebDriver driver;
    private String wsURL;
    private static ThreadLocal<Utils> instance = new ThreadLocal<Utils>();

    public static Utils getInstance() {
        if (instance.get() == null) {
            instance.set(new Utils());
        }
        return instance.get();
    }

    public WebDriver launchBrowser() throws IOException {
        return launchBrowser(false);
    }

    public WebDriver launchBrowser(boolean isHeadless) throws IOException {
        os = System.getProperty("os.name").toLowerCase();
        Map<String, Object> prefs = new HashMap<String, Object>();
        //1-Allow, 2-Block, 0-default
        prefs.put("profile.default_content_setting_values.notifications", 1);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments(Arrays.asList("--start-maximized"));
        if (isHeadless) {
            options.addArguments(Arrays.asList("--headless", "--disable-gpu"));
        }

        options.setExperimentalOption("prefs", prefs);

        DesiredCapabilities crcapabilities = DesiredCapabilities.chrome();
        crcapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        crcapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        crcapabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, System.getProperty("user.dir") + "/target/chromedriver.log");
        if (os.indexOf("mac") >= 0) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, System.getProperty("user.dir") + "/driver/chromedriver");
        } else
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, System.getProperty("user.dir") + "/driver/chromedriver");
        service = new ChromeDriverService.Builder()
                .usingAnyFreePort()
                .withVerbose(true)
                .build();
        service.start();

        try {
            driver = new RemoteWebDriver(service.getUrl(), crcapabilities);
        } catch (Exception e) {
            throw e;
        }

        return driver;
    }

    public void stopChrome() {
        driver.close();
        driver.quit();
        service.stop();
    }

    public void waitFor(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
