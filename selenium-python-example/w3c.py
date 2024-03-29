from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

driver = webdriver.Firefox()

try:
    driver.get("https://www.w3.org/")

    assert "W3C" in driver.title

    elem = driver.find_element(By.ID, "main")

    driver.get_full_page_screenshot_as_file('/tmp/screenshot.png')

except Exception as e:
    print(e)
finally:
    driver.quit()
