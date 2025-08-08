Feature: Login to SmartEdu Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of SmartEdu app "http://192.168.0.22/"

@ValidCredentialsAndCompleteIQTest
Scenario Outline: Login with valid credentials and complete IQ Test
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be directed to the dashboard page
    When User clicks on the Tests button
    Then User should be directed to the Tests page
    When User clicks on "IQ test" from the test list
    Then User should be directed to the test overview page
    When User clicks on "Start Test" button
    Then User should be directed to the test execution page
    When User answers all questions in the test
    Then User should be directed to the test result page

    Examples:
        | username | password   |
        | s1       | Student.01 |
