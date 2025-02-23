@e2e
Feature: authenticate and hello

Background:
* configure driver = { type: 'geckodriver', executable: './geckodriver' }

Scenario: authenticate and hello
* def username = 'Bob'

Given driver 'http://localhost:3000/'
And driver.input('input[id=username]', username)
When driver.click('#submit')
Then match driver.text('body') == 'Hello!!!'

* def bytes = driver.screenshot()
* eval karate.embed(bytes, 'image/png')