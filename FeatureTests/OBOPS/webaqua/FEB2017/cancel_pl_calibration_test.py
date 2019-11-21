from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import time
import unittest
import os
import xmlrunner

class QA2Test(unittest.TestCase):

    def setUp(self):
        
        #self.driver = webdriver.Firefox()
        self.driver = webdriver.Remote(
            command_executor='http://testfarm.sco.alma.cl:4444/wd/hub',
            desired_capabilities=DesiredCapabilities.CHROME
        )
        
        self.wait = WebDriverWait(self.driver, 10)

        self.driver.implicitly_wait(200)
        self.driver.get("https://%s.asa-test.alma.cl/webaqua/" % os.environ['AQUA_RELEASE'])

        username = self.driver.find_element_by_name("username")
        password = self.driver.find_element_by_name("password")
        submit = self.driver.find_element_by_name("submit")
        username.send_keys("aaguirre")
        password.send_keys("prodnew2me")
        submit.click()

    def uncheck_option(self, element_id):

        this_element = self.driver.find_element_by_id(element_id)
        if this_element.is_selected():
            this_element.click()

    def check_option(self, element_id):

        this_element = self.driver.find_element_by_id(element_id)
        if not this_element.is_selected():
            this_element.click()

    def test_cancel_pl_cal_review(self):

        qa2 = self.driver.find_element_by_id("zk_c_37")
        qa2.click()

        self.uncheck_option('zk_c_208-real')
        self.uncheck_option('zk_c_209-real')
        self.check_option('zk_c_210-real')
        self.uncheck_option('zk_c_211-real')

        search = self.driver.find_element_by_id("zk_c_195")
        search.click()
        
        first_project = self.driver.find_element_by_xpath('//*[@id="zk_c_167-rows"]/tr')
        first_project.click()

        #zk_c_16

        #pl_calibration = driver.find_element_by_id("zk_c_333-cnt")
        #pl_calibration.click()

        pl_calibration = self.driver.find_element_by_xpath('/html/body/div/div/div/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr/td/div/div/ul/li[2]')
        pl_calibration.click()

        #start_pl_cal_review = driver.find_element_by_id("zk_c_16812")
        #start_pl_cal_review.click()

        start_pl_cal_review = self.driver.find_element_by_xpath("/html/body/div/div/div/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr/td/div/div[2]/div[2]/div/div/div/div/div[3]/table/tbody/tr[6]/td/div/table/tbody/tr/td/table/tbody/tr/td/button")
        start_pl_cal_review.click()

        ok_button = self.driver.find_element_by_xpath('/html/body/div[4]/div[2]/table[2]/tbody/tr/td/table/tbody/tr/td/button')
        ok_button.click()

        time.sleep(1)
        
        cancel_pl_cal_review = self.driver.find_element_by_xpath("/html/body/div/div/div/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr/td/div/div[2]/div[2]/div/div/div/div/div[3]/table/tbody/tr[6]/td/div/table/tbody/tr/td/table/tbody/tr/td[9]/button")
        #self.wait.until(EC.visibility_of(cancel_pl_cal_review))
        try:
            cancel_pl_cal_review.click()
        except ElementNotInteractableException, e:
            # Check if user is data reducer
            raise e

        #import ipdb;ipdb.set_trace()
        
        ok_button = self.driver.find_element_by_xpath("/html/body/div[4]/div/table[2]/tbody/tr/td/table/tbody/tr/td/button")
        ok_button.click()

        ok_button = self.driver.find_element_by_xpath('/html/body/div[4]/div[2]/table[2]/tbody/tr/td/table/tbody/tr/td/button')
        ok_button.click()

    def tearDown(self):

        self.driver.quit()

if __name__ == "__main__":
    unittest.main(
        testRunner=xmlrunner.XMLTestRunner(output='test-reports'),
    )
