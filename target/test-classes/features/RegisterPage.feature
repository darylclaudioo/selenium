Feature: Register to SmartEdu Application

Background:
    Given User has opened the browser
    And User has navigated to the register page of SmartEdu app "http://localhost:8080/register"

@ValidCredentials
Scenario Outline: Register with valid credentials
    When User registers with username "<username>" and password "<password>"
    And User clicks on the register button
    Then User should see notification message "Registrasi berhasil untuk <username>"

    Examples:
        | username | password |
        | daryl    | daryl    |

@InvalidCredentials
Scenario Outline: Register with invalid credentials
    When User enters existing username "<username>" and password "<password>"
    And User clicks on the register button
    Then User should see notification message "Username sudah terdaftar"

    Examples:
        | username | password |
        | admin    | 1234     |