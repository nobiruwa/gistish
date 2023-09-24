@http
Feature: sample

Scenario: authenticate and hello

Given url 'http://localhost:3000/authenticate'
And request { username: 'john' }
When method post
Then status 200

Given url 'http://localhost:3000/protect/hello'
When method get
Then status 200