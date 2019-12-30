import sys
import calendar
import re
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select

username = sys.argv[1]
password = sys.argv[2]

driver = webdriver.Firefox()
driver.implicitly_wait(10)
driver.get("https://nb.fidelity.com/public/nb/default/home")
assert "Log In to Fidelity NetBenefits" in driver.title
usernameField = driver.find_element_by_id("username")
usernameField.clear()
usernameField.send_keys(username)
passwordField = driver.find_element_by_id("password")
passwordField.clear()
passwordField.send_keys(password)
loginButton = driver.find_element_by_id("fs-login-button")
passwordField.send_keys(Keys.RETURN)
driver.find_elements_by_class_name("nb-account-header--value-description")
driver.get("https://workplaceservices.fidelity.com/mybenefits/savings2/navigation/dc/OnlineStatement")
customRadio = driver.find_element_by_id("custradio")
customRadio.click()
with open("data.txt","w") as file:
    for year in [2013,2014,2015,2016,2017,2018,2019]:
        for month in [1,2,3,4,5,6,7,8,9,10,11,12]:
            fromMonthField = driver.find_element_by_name("from_date_month")
            fromDayField = driver.find_element_by_name("from_date_day")
            fromYearField = driver.find_element_by_name("from_date_year")
            toMonthField = driver.find_element_by_name("to_date_month")
            toDayField = driver.find_element_by_name("to_date_day")
            toYearField = driver.find_element_by_name("to_date_year")
            lastDay = calendar.monthrange(year,month)[1]
            if year==2019 and month==12:
                lastDay = 27
            fromMonthField.send_keys("%02d"%month)
            fromDayField.send_keys("01")
            fromYearField.send_keys(str(year))
            toMonthField.send_keys("%02d"%month)
            toDayField.send_keys("%02d"%lastDay)
            toYearField.send_keys(str(year))
            continueButton = driver.find_elements_by_class_name("continueButton")[0]
            continueButton.click()
            dataSegment = driver.find_element_by_xpath("//div[@id='horz_region2']/div[@id='horz_content']")
            data = str(dataSegment.text)
            data = re.sub(r'[ \r\n\t]+',' ',data)
            data = re.sub(r'[^@]*Account Activity This Period ','',data)
            data = re.sub(r' Your Personal Rate of Return is calculated[^@]*','',data)
            file.write(data+"\n")
            statementsLink = driver.find_element_by_id("link_dc_statement")
            statementsLink.click()
driver.close()
