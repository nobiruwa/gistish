#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import inspect

import selenium.webdriver.remote.webelement

print('selenium.webdriver.remote.webelement.WebElement')
for m in inspect.getmembers(selenium.webdriver.remote.webelement.WebElement):
    print(m[0])
