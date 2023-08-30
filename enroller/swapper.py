import sys
import time
from twilio.rest import Client

from selenium.webdriver.chrome.options import Options

from webdriver_manager.chrome import ChromeDriverManager

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as ec
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import StaleElementReferenceException
from random import randint

import requests
import json

from datetime import datetime
"""
Copyright 2023 Jorge Robles

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the 
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the 
Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE. 

DISCLAIMER: This script is for educational purposes only. The developer is not responsible for any 
consequences that may occur from using this script. This software is released "as is" under the MIT license.

Requirements to run: Python 3, Selenium, Twilio, and the ChromeDriverManager

A discord webhook is also required for notifications.

This script will automatically enroll you in a class if it is available. It will also notify you via text message and 
discord webhook if a class is available. The script will also notify you if it is unable to enroll you in a class. 
There are many places where the script could break after being released due to constantly changing HTML elements on 
CUNYFirst. If you are having issues, please check the discord webhook and print statements for any error messages, 
and fix accordingly. The comments alongside the code should help the user figure out what is going on when errors 
occur, as the script is fairly linear in nature."""
# Insert your discord webhook here
WEBHOOK_URL = "DISCORD WEBHOOK URL HERE"


# For sending Discord message notifications.


def wait_for_page_load(driver, timeout=30):
    WebDriverWait(driver, timeout).until(
        lambda d: d.execute_script("return document.readyState") == "complete"
    )


def send_discord_message(webhook_url, content):
    current_time = datetime.now().strftime('%H:%M:%S')
    content_with_timestamp = f"[{current_time}] {content}"

    data = {
        "content": content_with_timestamp
    }
    headers = {
        "Content-Type": "application/json"
    }

    response = requests.post(
        webhook_url, data=json.dumps(data), headers=headers)

    if response.status_code != 204:
        pass  # Do nothing if the request fails.


def send_text_message(body):
    current_time = datetime.now().strftime('%H:%M:%S')
    message_with_timestamp = f"[{current_time}] {body}"

    # Your Twilio account SID and Auth Token
    account_sid = "ADD ACCOUNT SID HERE OR ERRORS. you can remove this whole segment if you want and modify script"
    auth_token = "ADD AUTH TOKEN OR DELETE ALL INSTANCES OF THIS FUNCTION"

    # Initialize the Twilio client
    client = Client(account_sid, auth_token)

    # Send the text message
    message = client.messages.create(
        body=message_with_timestamp,
        from_="+YOUR TWILIO NUMBER",
        to="+CELL NUMBER"
    )

    # Initializes the chrome driver.


# Here, the driver will be initialized. I recommend using an installed chromium chrome driver and putting the
# path in the commented out section.
def init_driver():
    chrome_options = Options()
    chrome_options.add_argument("--start-maximized")
    chrome_options.add_argument(
        f'user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36')
    # Use the installed chromium-chromedriver
    # driver = webdriver.Chrome(service=Service(
    #     executable_path="/usr/lib/chromium-browser/chromedriver"), options=chrome_options)
    driver = webdriver.Chrome(service=Service(
        ChromeDriverManager().install()), options=chrome_options)
    return driver

# SOME ELEMENTS MAY BREAK RANDOMLY. Use inspect element on chrome and figure out if an ID has changed.
# Historically, an ID has moved by about 1 number and that could break everything.
# Check discord if the script seems to constantly be resetting and see what's happening.
def main(driver):
    # Get user input.
    username, password, time_delay = get_user_input()
    print("Opening CUNYFirst...")
    driver.get("https://home.cunyfirst.cuny.edu/")
    wait_for_page_load(driver)
    # Enter username and password.
    send_discord_message(WEBHOOK_URL, "Starting script!")
    print("Logging in...")
    driver.find_element(By.ID, "CUNYfirstUsernameH").send_keys(username)
    driver.find_element(By.ID, "CUNYfirstPassword").send_keys(password)
    time.sleep(2)
    driver.find_element(By.ID, "submit").click()
    time.sleep(8)
    wait_for_page_load(driver)

    # Go to the Student Center.
    print("Navigating to the Student Center...")
    try:
        driver.find_element(
            By.ID, "win0groupletPTNUI_LAND_REC_GROUPLET$0").click()
    except NoSuchElementException:
        print("Could not find the Student Center element! Maybe you inputted the wrong credentials?")
        driver.quit()

    # Click on the Schedule Builder element.

    # Wait for the page to load.
    wait_for_page_load(driver)

    # IMPORTANT - THIS MAY BREAK SOMETIMES WHEN CUNYFIRST CHANGES. the grouplet$(NUMBERS) may be off by a number.
    # Please inspect element.
    print("Navigating to the Schedule Builder...")
    try:
        driver.find_element(
            By.ID, "win0groupletPTNUI_LAND_REC_GROUPLET$15").click()
    except NoSuchElementException:
        print("Could not find the Schedule Builder element! Restarting...")
        print("If this is constantly happening, check to make sure the groupletID$(##) hasn't changed. Modify script.")
        driver.quit()

    # Switch to the newly opened tab.
    driver.switch_to.window(driver.window_handles[-1])

    # Wait for the page to load.
    time.sleep(10)
    wait_for_page_load(driver)

    # Click on the element titled "2023 Fall".
    print("Selecting the 2023 Fall term...")
    try:
        driver.execute_script("UU.caseTermContinue(3202330);")

    except NoSuchElementException:
        print("Could not find the '2023 Fall' element! Please make sure the target element is present on the page.")
        print("If you are looking for a different semester, please modify the script.")
        driver.quit()

    # Wait for the schedule to show up, may take a while.
    time.sleep(8)
    wait_for_page_load(driver)
    print("Waiting for the schedule to load...")
    try:
        switch_container = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable(
                (By.CSS_SELECTOR, ".results-legend-switch-container"))
        )
        switch_container.click()
    except NoSuchElementException:
        print("Unable to find the 'results-legend-switch-container' element.")
        driver.quit()

    except TimeoutException:
        print("The 'results-legend-switch-container' element took too long to load or become clickable.")
        driver.quit()

    wait_for_page_load(driver)
    print("Starting schedule loop...")
    while True:
        get_schedule(driver, time_delay)

# PLEASE ADD YOUR INFO HERE
def get_user_input():
    username = "USERNAME"
    password = "PASSWORD"

    time_delay = 25

    print()

    return username, password, time_delay


def exit_routine():
    print("Exiting program...")
    sys.exit(0)


# Finds an element with a retry mechanism, used to scan class availability.


class ElementNotFound(Exception):
    pass


def find_element_with_retry(parent, by, value, retries=3):
    while retries > 0:
        try:
            return parent.find_element(by, value)
        except StaleElementReferenceException:
            retries -= 1
        except NoSuchElementException:
            return None


def get_schedule(driver, time_delay):
    wait_for_page_load(driver)
    print("Opening favorites...")
    # Step 1: Click on the "page_favorites_expander" button.
    print("Clicking on the 'page_favorites_expander' button...")
    try:
        favorites_expander = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable((By.ID, "page_favorites_expander"))
        )
        favorites_expander.click()
        wait_for_page_load(driver)
    except NoSuchElementException:
        print("Unable to find the 'page_favorites_expander' button.")
        driver.quit()
    except TimeoutException:
        print(
            "The 'page_favorites_expander' button took too long to load or become clickable. You may have been timed out. Restarting...")
        driver.quit()

    # Step 2: Click on the "thumbtest1_0" element.
    print("Clicking on favorite schedule #1...")
    try:
        thumbtest_element = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable((By.ID, "thumbtest1_0"))
        )
        thumbtest_element.click()
        wait_for_page_load(driver)
    except NoSuchElementException:
        print("Unable to find the 'thumbtest1_0' element.")
        driver.quit()
    except TimeoutException:
        print("The 'thumbtest1_0' element took too long to load or become clickable.")
        driver.quit()

    # Step 3: Click on the "Load" button.
    print("Clicking on the 'Load' button...")
    try:
        load_button = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable(
                (By.XPATH, "//button[contains(text(), '◄ Load')]"))
        )
        load_button.click()
    except NoSuchElementException:
        print("Unable to find the 'Load' button.")
        driver.quit()
    except TimeoutException:
        print("The 'Load' button took too long to load or become clickable.")
        driver.quit()
    time.sleep(2)
    wait_for_page_load(driver)

    print("Checking for available classes...")
    # Check for full classes in the shopping cart.
    full_classes_in_cart = False
    space_in_cart = False
    swappable_classes = False
    full_indic = False
    i = 1
    try:
        print("Checking for full classes in the shopping cart...")
        selection_tables = WebDriverWait(driver, 10).until(
            ec.presence_of_all_elements_located(
                (By.CSS_SELECTOR, ".selection_table"))
        )
        current_time = datetime.now().strftime("%H:%M:%S")
        print(f"Current time: {current_time}")
        print("Found selection tables.")
        for table in selection_tables:
            course_state = None
            print()
            print(
                f"Checking for full classes in class {i}...")
            try:
                full_indic = WebDriverWait(table, 1).until(
                    ec.presence_of_all_elements_located(
                        (By.CSS_SELECTOR, ".fullText"))
                )
            except TimeoutException:
                # If the element is not found, continue to the next table
                full_indic = False
                print(f"Found space in class {i}!")
                try:
                    course_state = find_element_with_retry(
                        table, By.CSS_SELECTOR, ".course_state")
                    if course_state:
                        if course_state.text == "In Shopping Cart":
                            space_in_cart = True
                            print(f"Class {i} is ready to be enrolled.")
                        else:
                            print(
                                f"Class {i} is already enrolled. Moving on...")
                    elif not course_state:
                        swappable_classes = True
                        print(f"Class {i} IS READY TO BE SWAPPED!!!!")

                except (TimeoutException, StaleElementReferenceException):
                    swappable_classes = True
                    print(f"Class {i} is ready to be swapped. EXCEPT")

            # # debugging print statements
            # print(f"\nSwappable classes is currently: {swappable_classes}")
            # test_state = None
            # if course_state:
            #     test_state = course_state.text
            # print(f"Course state is currently: {test_state}")
            # print(f"Space in cart is currently: {space_in_cart}")
            # test_full = False
            # if full_indic:
            #     test_full = True
            # print(f"full_indic is currently: {test_full}\n")
            # # end debug statements

            if full_indic:
                print(f"Class {i} is FULL.")
            i += 1

        # If the course is full, check if it's in the shopping cart or not enrolled.
        if full_indic:
            try:
                in_shopping_cart = None
                course_state = find_element_with_retry(
                    table, By.CSS_SELECTOR, ".course_state")
                if course_state:
                    in_shopping_cart = course_state.text == "In Shopping Cart"
            except (TimeoutException, StaleElementReferenceException, AttributeError):
                in_shopping_cart = None
            if in_shopping_cart:
                full_classes_in_cart = True
        else:
            swappable_classes = True

    except NoSuchElementException:
        print("Unable to find the 'selection_table' element.")
        driver.quit()
    except TimeoutException:
        print("The 'selection_table' element took too long to load or become clickable.")
        driver.quit()
    # If there are ONLY full classes in the shopping cart, refresh the page and wait some seconds before checking again.

    # # More testing debug statements
    # print("\nTesting enroll action...")
    # print(f"full_classes_in_cart is currently: {full_classes_in_cart}")
    # print(f"space_in_cart is currently: {space_in_cart}")
    # print(f"swappable_classes is currently: {swappable_classes}")
    # # End testing debug statements

    if not space_in_cart and not swappable_classes:
        longer_time_delay = 120
        current_time = datetime.now()
        current_time_tuple = (
            current_time.hour, current_time.minute, current_time.second)
        # If it's between 11:30pm and 6am, wait a random amount of time between 120 and 170 seconds.

        if (23, 30, 0) <= current_time_tuple or current_time_tuple < (6, 0, 0):
            delay = randint(longer_time_delay, longer_time_delay + 50)
        else:
            # If it's not between 11:30pm and 6am, wait a random amount of time between 35 and 75 seconds.
            delay = randint(time_delay + 10, time_delay + 50)

        print(
            f"No classes available. Refreshing page and sleeping for {delay} seconds...\n")
        driver.refresh()
        time.sleep(delay)
        return
    if swappable_classes or space_in_cart:
        # Locate and click on the "Get this Schedule" button.
        try:
            get_schedule_button = WebDriverWait(driver, 4).until(
                ec.element_to_be_clickable(
                    (By.CSS_SELECTOR, ".button_get_schedule"))
            )
            get_schedule_button.click()
        except NoSuchElementException:
            print("Unable to find the 'Get this Schedule' button.")
            driver.quit()
        except TimeoutException:
            print(
                "The 'Get this Schedule' button took too long to load or become clickable.")
            driver.quit()

        # If there are enrollable classes, notify the user immediately.

        wait_for_page_load(driver)
        success_element = None
        failed_element = None

        # Locate and click on the "Do Actions" button.
        try:
            do_actions_button = WebDriverWait(driver, 10).until(
                ec.element_to_be_clickable(
                    (By.CSS_SELECTOR, ".big_button.button_do_actions"))
            )
            do_actions_button.click()
            print("\nAttempting to get schedule...")
            send_discord_message(WEBHOOK_URL, "Found a class to enroll in!")
            send_text_message("Found a class to enroll in!")
            wait_for_page_load(driver)
        except NoSuchElementException:
            print("Unable to find the 'Do Actions' button.")
            driver.quit()

        except TimeoutException:
            print("The 'Do Actions' button took too long to load or become clickable.")
            driver.quit()

        success_element = None
        added_element = None
        swapped_element = None
        failed_element = None

        try:
            # Check if the "Success" message is visible.
            success_element = WebDriverWait(driver, 9).until(
                ec.presence_of_element_located(
                    (By.XPATH, "//*[contains(text(), 'Success')]"))
            )
            # If the "Success" message is visible, refresh the page and start over.
            if success_element.is_displayed():
                print("Success detected!")
                send_text_message("Partial success detected! Check CUNYFirst.")
                send_discord_message(WEBHOOK_URL,
                                     "Partial success detected! Check CUNYFirst.")
                print("Checking for failure message...")
        except TimeoutException:
            print(
                "The 'Success' element did not appear within 9 seconds. Checking for added...")

        # Check if Added message is visible.
        try:
            added_element = WebDriverWait(driver, 2).until(
                ec.presence_of_element_located(
                    (By.XPATH, "//*[contains(text(), 'Added')]"))
            )
            if added_element.is_displayed():
                print("Added detected!")
                send_text_message(
                    "Partial success (Added) detected! Check CUNYFirst.")
                send_discord_message(WEBHOOK_URL,
                                     "Partial success (Added) detected! Check CUNYFirst.")
                print("Checking for other messages...")
        except TimeoutException:
            print(
                "The 'Added' element did not appear within 2 seconds. Checking for swapped...")

        # Check if Swapped message is visible.
        try:
            swapped_element = WebDriverWait(driver, 2).until(
                ec.presence_of_element_located(
                    (By.XPATH, "//*[contains(text(), 'Swapped')]"))
            )
            if swapped_element.is_displayed():
                print("Swapped detected!")
                send_text_message(
                    "Partial success (Swapped) detected! Check CUNYFirst.")
                send_discord_message(WEBHOOK_URL,
                                     "Partial success (Swapped) detected! Check CUNYFirst.")
                print("Checking for failure message...")
        except TimeoutException:
            print(
                "The 'Swapped' element did not appear within 2 seconds. Checking for failure...")

        try:
            # Check if the "Failed" message is visible.
            failed_element = WebDriverWait(driver, 2).until(
                ec.presence_of_element_located(
                    (By.XPATH, "//*[contains(text(), 'Failed')]"))
            )
            # If the "Failed" message is visible, refresh the page and start over.
            if failed_element.is_displayed():
                print("Failed!")
                send_discord_message(
                    WEBHOOK_URL, "Mission failed, we'll get em next time.")

        except TimeoutException:
            print(
                "The 'Failed' element did not appear within 2 seconds. Complete success!")

        # Looking for complete success - if so, exit the program.

        if (success_element and success_element.is_displayed()) or (added_element and added_element.is_displayed()) or (
                swapped_element and swapped_element.is_displayed()) and not (
                failed_element and failed_element.is_displayed()):
            print("Done! Complete success - no failures detected. Exiting program...")
            send_discord_message(
                WEBHOOK_URL, "Complete success - no failures detected. Check CUNYFirst.")
            send_text_message(
                "Complete success - no failures detected. Check CUNYFirst.")
            exit_routine()

        print("Refreshing page...")
        driver.refresh()
        # Random delay based off user input.
        delay = randint(time_delay, time_delay + 10)
        print(f"Sleeping until next check in {delay} seconds...")
        time.sleep(delay)
        return


if __name__ == "__main__":
    restarting = True
    while restarting:
        try:
            driver = init_driver()
            main(driver)
            restarting = False
        except Exception as e:
            print(f"An error occurred: {e}")
            print("Restarting the script...")
            send_discord_message(
                WEBHOOK_URL, "An error occurred, restarting script.")

            driver.quit()

        current_time = datetime.now()
        current_time_tuple = (
            current_time.hour, current_time.minute, current_time.second)

        # The script needs to sleep too!
        if (0, 30, 0) <= current_time_tuple < (6, 30, 0):
            sleepytime = randint(300, 600)
            sleepy_minutes = round(sleepytime / 60, 2)
            print(f"Sleeping for {sleepy_minutes} minutes.")
            send_discord_message(
                WEBHOOK_URL, f"It's late - sleeping for {sleepy_minutes} minutes.")
            time.sleep(sleepytime)  # Sleep for longer amount of minutes between loops after midnight.

        time.sleep(5)
