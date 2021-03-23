Feature:SignUp page feature

  Background:
    Given user is on Sign-Up page

  Scenario:Sign-Up page title
    When user gets the title of the page
    Then page title should be "Play Online Casino Games Now | MoneyGaming.com"


  Scenario:Signing-up and validating
    And   Clicks on JOIN NOW! button
    Then  user selects "Mr" from dropdown for Title field
    And   user enters "firstname"  in FirstName field
    And   user enters "surname" in SurName field
    And   user checks the terms and conditions
    Then  user submits the form by clicking on JOIN NOW! button below
    And   message appears  "This field is required" under the date of  birth box field
