# CUNYFirst Schedule Builder Automation

This script automates the process of checking for available classes on CUNYFirst's Schedule Builder and registering for them.

## How it works

1. The script initializes the Selenium WebDriver with a custom user agent.
2. It prompts the user for their CUNYFirst login credentials and the delay between schedule checks (in seconds).
3. The script navigates to the CUNYFirst login page and logs in using the provided credentials.
4. It then navigates to the Student Center and clicks on the Schedule Builder link.
5. The script selects the 2023 Fall term and waits for the schedule to load.
6. It switches the view to display full classes.
7. The script enters a loop where it checks for available classes and attempts to register for them.

## Edge cases and error handling

- The script handles cases where it cannot find elements on the page (e.g., Student Center link, Schedule Builder link, etc.) by quitting the WebDriver and printing an error message.
- If a TimeoutException is raised, the script will print an error message and quit the WebDriver.
- If any other exception occurs during the main loop, the script will quit the WebDriver, wait for 5 seconds, and restart.

## Assumptions and limitations

- This script assumes that the user has a CUNYFirst account and knows their login credentials.
- The script uses hardcoded CSS selectors and XPaths to locate elements on the page, so if the website's structure changes, the script may break.
- The script currently selects the 2023 Fall term by executing a JavaScript function. If the term or function changes, the script may need to be updated.
- Delays (using `time.sleep()`) have been added throughout the script to account for loading times. However, these delays may not be sufficient for slower internet connections, and the script may need to be adjusted accordingly.

## Usage

1. Install the required dependencies:
```pip install selenium webdriver-manager```
2. Run the script:
```python cunyfirst_schedule_builder_automation.py```

3. Enter your CUNYFirst login credentials and the delay between schedule checks when prompted.

4. The script will navigate through the CUNYFirst website, check for available classes, and attempt to register for them.

Note: This script requires the Selenium WebDriver and the WebDriver Manager Python packages. Make sure to install them before running the script.
