import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OddsSharkScraper {
    ChromeDriver driver;

    OddsSharkScraper() {
        String projectLocation = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", projectLocation + "\\lib\\drivers\\chromedriver\\chromedriver.exe");

        System.out.println("Setting options");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("start-maximized");

        System.out.println("Setting driver");
        driver = new ChromeDriver(options);
    }

    private void goToSeason(int season) {
        String curSeason;

        System.out.println("Trying to reach Season: " + season);
        goToHome();

        do {
            // Clicks and opens calendar spin selector
            driver.findElement(By.id("scoreboard-date-navigation")).findElement(By.className("os-spin-selector")).click();

            // Sets current season to initial position of spin selector
            curSeason = driver.findElement(By.cssSelector(".spin-value.active")).getText();

            // Checks if homePage is current season. If not then goes down one year.
            if (!Integer.toString(season).equals(curSeason)) {
                driver.findElement(By.cssSelector(".spin-up")).click();
                String newSeason = driver.findElement(By.cssSelector(".spin-value.active")).getText();
                curSeason = newSeason;
                System.out.println("Going to season: " + curSeason);
                driver.findElement(By.cssSelector(".spin-selector-button.base-button.button-apply")).click();
            } else System.out.println("Already at Season: " + season);
        } while (!Integer.toString(season).equals(curSeason));
        waitUntilLoaded();
    }

    private void goToHome() {
        System.out.println("Navigating to Homepage");
        driver.get("http://oddsshark.com/nfl/scores");
        waitUntilLoaded();
    }

    private void goToItem(int position) {
        // Selects week/index slider as element
        WebElement slider = driver.findElement(By.id("gc-scoreboard")).findElement(By.cssSelector(".list.slidee"));
        WebElement item = slider.findElement(By.xpath("li[" + position + "]"));

        System.out.println("Navigating slider until item " + position + " is visible");
        int i = -45 + (position * 45);
        driver.executeScript("document.getElementsByClassName('slidee')[1].style.transform = \"translateX(-" + i + "px)\";");
        System.out.print("px" + i + " ");
        System.out.println("Found item " + position + "... attempting to load new scoreboard");

        item.click();
        waitUntilLoaded();
    }

    private void waitUntilLoaded() {
        System.out.print("Loading Scoreboard... ");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement oslive = driver.findElement(By.id("gc-scoreboard")).findElement(By.id("oslive-scoreboard"));
        wait.until(ExpectedConditions.attributeToBe(oslive, "class", "scoreboard"));

        System.out.println("Scoreboard loaded!");
    }

    public Scoreboard getScoreBoard(int season, int position) {
        goToSeason(season);
        goToItem(position);
        return new Scoreboard(driver.findElement(By.id("gc-scoreboard")).getAttribute("outerHTML"), season, position);
    }

    public void quit() {
        driver.quit();
    }

    public static void main(String[] args) throws Exception {
        OddsSharkScraper scraper = new OddsSharkScraper();
        scraper.getScoreBoard(2014, 10);
    }
}
