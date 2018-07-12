package com.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class Main {
    public WebDriver driver ;
    String text;
    String[] text_array;
    int noOfads;
    int adCounter =1;

    @BeforeTest
    public void launchBrowser(){
        System.setProperty("webdriver.gecko.driver","E:\\java\\gecko\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get("https://ikman.lk/");
    }

    @Test(priority = 0)
    public void navigate(){
        //click property
        driver.findElement(By.linkText("Property")).click();

        //click on Houses
        driver.findElement(By.partialLinkText("Houses")).click();

        //click Colombo
        driver.findElement(By.partialLinkText("Colombo")).click();
    }

    @Test(priority = 1)
    public void setConditions(){
        //apply minimum and maximum price
        driver.findElement(By.partialLinkText("Price (Rs)")).click();
        driver.findElement(By.name("filters[0][minimum]")).sendKeys("5000000");
        driver.findElement(By.name("filters[0][maximum]")).sendKeys("7500000");

        //click filter
        driver.findElement(By.className("btn-apply")).click();

        //apply no of beds
        driver.findElement(By.partialLinkText("Beds")).click();
        driver.findElement(By.id("filters2values-3")).click();
    }

    @Test(priority = 2)
    public void getSales() {
        //get the no of ads shown
        text = driver.findElement(By.className("summary-count")).getText();
        text_array = text.split(" ");
        noOfads =  Integer.parseInt(text_array[3]);

        //Iterate through all pages
        for (int i = 0; i <= (noOfads / 25); i++) {

            //Get ads on one page to a list
            int count = 0;
            List<WebElement> ads = ((FirefoxDriver) driver).findElementsByClassName("ui-item");

            //Iterate through ads
            for (WebElement ad : ads) {
                //Remove first two ads (Sponsored)
                if (count < 2) {
                    count++;
                } else {
                    //Getting Price of the ad
                    String price = ad.findElement(By.className("item-info")).getText();

                    //Printing the ad as per the need
                    System.out.println("Ad Number " + (adCounter) + " Price is : " + price);

                    //Sanitizing the Price to integer
                    String[] priceArr = price.split("Rs| |,");
                    String priceMerge = priceArr[2] + priceArr[3] + priceArr[4];
                    Integer priceVal = Integer.parseInt(priceMerge);

                    //Getting no. of beds//a[@class='col-6 lg-3 pag-next']
                    String beds = ad.findElement(By.xpath("//p[@class='item-meta']//span[1]")).getText();
                    String[] bedsArr = beds.split(" ");
                    Integer bedsCount = Integer.parseInt(bedsArr[1]);

                    //Validating the price and no.of Beds
                    if (5000000 <= priceVal && priceVal <= 7500000 && bedsCount == 3) {
                        count++;
                        adCounter++;
                    } else {
                        System.out.println("Ad Number " + (adCounter) + "'s Validation failed");
                        count++;
                        adCounter++;
                    }

                }
            }
            try {
                String nextBtnLink = "//a[@class='col-6 lg-3 pag-next']";
                WebElement nextBtn = ((FirefoxDriver) driver).findElementByXPath(nextBtnLink);
                nextBtn.click();
            } catch (Exception e) {
                System.out.println("End of ads");
            }

        }

    }



    @AfterTest
    public void terminateBrowser(){
        driver.close();
    }


}
