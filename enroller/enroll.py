import sys
import time

from selenium.webdriver.chrome.options import Options

from webdriver_manager.chrome import ChromeDriverManager

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as ec
from selenium.common.exceptions import TimeoutException


def init_driver():
    chrome_options = Options()
    chrome_options.add_argument(
        f'user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36')

    driver = webdriver.Chrome(service=Service(
        ChromeDriverManager().install()), options=chrome_options)
    return driver


def main(driver):
    # Get user input.
    username, password, time_delay = get_user_input()
    chrome_options = Options()
    chrome_options.add_argument(
        f'user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36')

    # Go to CUNYFirst.
    driver = webdriver.Chrome(service=Service(
        ChromeDriverManager().install()), options=chrome_options)
    driver.get("https://home.cunyfirst.cuny.edu/")

    # Enter username and password.
    print("Logging in...")
    driver.find_element(By.ID, "CUNYfirstUsernameH").send_keys(username)
    driver.find_element(By.ID, "CUNYfirstPassword").send_keys(password)
    driver.find_element(By.ID, "submit").click()
    time.sleep(9)

# Go to the Student Center.
    print("Navigating to the Student Center...")
    try:
        driver.find_element(
            By.ID, "win0groupletPTNUI_LAND_REC_GROUPLET$0").click()
    except NoSuchElementException:
        print("Could not find the Student Center element! Please make sure the target element is present on the page.")
        driver.quit()

    # Click on the Schedule Builder element.

    # Wait for the page to load.
    time.sleep(6)
    print("Navigating to the Schedule Builder...")
    try:
        driver.find_element(
            By.ID, "win0groupletPTNUI_LAND_REC_GROUPLET$16").click()
    except NoSuchElementException:
        print("Could not find the Schedule Builder element! Please make sure the target element is present on the page.")
        driver.quit()

    # Switch to the newly opened tab.
    driver.switch_to.window(driver.window_handles[-1])

    # Wait for the page to load.
    time.sleep(8)

    # Click on the element titled "2023 Fall".
    print("Selecting the 2023 Fall term...")
    try:
        driver.execute_script("UU.caseTermContinue(3202330);")

    except NoSuchElementException:
        print("Could not find the '2023 Fall' element! Please make sure the target element is present on the page.")
        driver.quit()

    # Wait for the schedule to show up, may take a while.
    time.sleep(10)
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

    time.sleep(5)
    print("Starting schedule loop...")
    while True:
        get_schedule(driver, time_delay)


def get_user_input():
    username = "jorge.robles73"
    password = "Redfire101"

    time_delay = 69

    print()

    return username, password, time_delay


def exit_routine():
    print("Exiting program...")
    sys.exit(0)


def get_schedule(driver, time_delay):
    print("Checking for available classes...")
    # Check for full classes in the shopping cart.
    full_classes_in_cart = False
    space_in_cart = False
    i = 1
    try:
        print("Checking for full classes in the shopping cart...")
        selection_tables = WebDriverWait(driver, 10).until(
            ec.presence_of_all_elements_located(
                (By.CSS_SELECTOR, ".selection_table"))
        )
        print("Found selection tables.")
        for table in selection_tables:
            print(f"Checking class {i}")
            i += 1
            class_table = WebDriverWait(table, 10).until(
                ec.presence_of_all_elements_located(
                    (By.CSS_SELECTOR, ".course_title"))
            )
            name_of_class = class_table.text
            print(
                f"Checking for full classes in class {name_of_class}")
            try:
                full_indic = WebDriverWait(table, 10).until(
                    ec.presence_of_all_elements_located(
                        (By.CSS_SELECTOR, ".bg_red.full_indic"))
                )
            except TimeoutException:
                # If the element is not found, continue to the next table
                print(f"Found space in class {name_of_class}")
                course_state = WebDriverWait(table, 10).until(
                    ec.presence_of_element_located(
                        (By.CSS_SELECTOR, ".course_state"))
                )
                if course_state.text == "In Shopping Cart":
                    space_in_cart = True
                continue
            print(f"full_indic is {full_indic}")

            if full_indic:
                print(f"Found full class in {name_of_class}")
                course_state = WebDriverWait(table, 10).until(
                    ec.presence_of_element_located(
                        (By.CSS_SELECTOR, ".course_state"))
                )
                if course_state.text == "In Shopping Cart":
                    full_classes_in_cart = True
                    continue
    except NoSuchElementException:
        print("Unable to find the 'selection_table' element.")
        driver.quit()
    except TimeoutException:
        print("The 'selection_table' element took too long to load or become clickable.")
        driver.quit()
    # If there are full classes in the shopping cart, refresh the page and wait 60 seconds before checking again.
    if full_classes_in_cart and not space_in_cart:
        print("Full class found in the shopping cart. Refreshing page...")
        driver.refresh()
        time.sleep(60)
        return

    # Locate and click on the "Get this Schedule" button.
    try:
        get_schedule_button = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable(
                (By.CSS_SELECTOR, ".button_get_schedule"))
        )
        get_schedule_button.click()
    except NoSuchElementException:
        print("Unable to find the 'Get this Schedule' button.")
        driver.quit()
    except TimeoutException:
        print("The 'Get this Schedule' button took too long to load or become clickable.")
        driver.quit()

    time.sleep(5)

    # Locate and click on the "Do Actions" button.
    try:
        do_actions_button = WebDriverWait(driver, 10).until(
            ec.element_to_be_clickable(
                (By.CSS_SELECTOR, ".big_button.button_do_actions"))
        )
        do_actions_button.click()
        print("Attempting to get schedule...")
        time.sleep(5)
    except NoSuchElementException:
        print("Unable to find the 'Do Actions' button.")
        driver.quit()

    except TimeoutException:
        print("The 'Do Actions' button took too long to load or become clickable.")
        driver.quit()

    # Check if the "Failed" message is visible.
    failed_element = WebDriverWait(driver, 10).until(
        ec.presence_of_element_located(
            (By.XPATH, "//strong[contains(text(), 'Failed')]"))
    )
    # If the "Failed" message is visible, refresh the page and start over.
    if failed_element.is_displayed():
        print("Failed! Refreshing page...")
        driver.refresh()
    print(f"Sleeping until next check... {time_delay} seconds.")
    time.sleep(time_delay)


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
            driver.quit()
            time.sleep(5)
