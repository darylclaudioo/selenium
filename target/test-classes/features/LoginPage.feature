Feature: Login to SmartEdu Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of SmartEdu app "http://localhost:8080/"

@ValidCredentials
Scenario Outline: Login with valid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be able to see "Login success!" notification message

    Examples:
        | username | password |
        | admin    | 1234     |

@InvalidCredentials
Scenario Outline: Login with invalid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be able to see "Login failed!" notification message

    Examples:
        | username | password |
        | daryl    | dary     |