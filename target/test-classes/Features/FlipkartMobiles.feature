#Author: your.email@your.domain.com
Feature: Flipkart Mobiles

  @tag1
  Scenario: Validate Flipkart mobiles module
    Given user need to launch the browser
    And user need to handle the login option
    And user need to click the search option
    When user need to get the name of mobiles with price,write the result in excel
    And user need to print the lowest value and search the mobile
    When user click on the lowest mobile.
    Then user validate the output.
