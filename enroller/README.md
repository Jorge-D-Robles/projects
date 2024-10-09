# Web Automation Project

## Note: This script is partially outdated. Minor edits (documented in code) heading into the right semester for the schedule are outdated as this was written for Fall 2023 only. If you managed to modify the script to account for the correct date, please submit a pull request and I'll update it.

The `enroller.py` Python script automates the process of checking for available classes on Schedule Builder and swapping them with the current schedule. It uses Selenium WebDriver for browser automation, and Twilio for sending text message notifications. The script utilizes a human-like approach to traverse through the site and enroll for classes accordingly.

## How it works

1. The script initializes the Selenium WebDriver with a custom user agent.
2. It prompts the user for their QC login credentials and the delay between schedule checks (in seconds).
3. The script navigates to the QC login page and logs in using the provided credentials.
4. It then navigates to the Student Center and clicks on the Schedule Builder link.
5. The script selects the 2023 Fall term and waits for the schedule to load.
6. It switches the view to display full classes.
7. The script enters a loop where it checks the first favorite schedule for available classes and attempts to swap them with the current schedule.

## Edge cases and error handling

The script handles cases where it cannot find elements on the page (e.g., Student Center link, Schedule Builder link, etc.) by quitting the WebDriver and printing an error message. If a TimeoutException is raised, the script will print an error message and quit the WebDriver. Regardless of exception or error, the script will quit the WebDriver, wait for 5 seconds, and automatically restart without user input, allowing for constant uptime.

## Assumptions and limitations

This script assumes that the user has a QC account and knows their login credentials. The script uses hardcoded CSS selectors and XPaths to locate elements on the page, so if the website's structure changes, the script may break. The script currently selects the 2023 Fall term by executing a JavaScript function. If the term or function changes, the script may need to be updated. Delays (using time.sleep()) have been added throughout the script to account for loading times. However, these delays may not be sufficient for slower internet connections, and the script may need to be adjusted accordingly.

## Usage

1. Add your desired schedule to the first favorite slot on Schedule Builder before running the script. The script will attempt to match your current schedule with this favorite schedule and enroll or swap the classes that don't match as they open up.
2. Install the required dependencies: `pip install selenium webdriver-manager twilio requests`
3. Run the script: `python3 enroller.py`
4. Enter your CUNYFirst login credentials and the delay between schedule checks when prompted.
5. The script will navigate through the CUNYFirst website, check the first favorite schedule for available classes, and attempt to swap them with the current schedule.

Note: This script requires the Selenium WebDriver, the WebDriver Manager, Twilio, and Requests Python packages. Make sure to install them before running the script.

## Caution on Frequency of Requests

While this script is designed to automate the process of checking for available classes and swapping them, it's important to use it responsibly to avoid causing issues with the CUNYFirst server. Excessive requests to the server in a short period of time can lead to performance issues, and in some cases, may even result in your IP address being temporarily or permanently blocked.

When you edit the script and input your own credentials, you will be asked to enter a delay (in seconds) between schedule checks. This delay is essentially the frequency at which the script sends requests to the server with a random interval of +- 20 seconds.  Please ensure that you set a reasonable delay to prevent flooding the server with requests, although this program does already have protections against that. A delay of 300+ seconds is generally considered reasonable, but this may vary depending on the time of day and the server's current load. 

## Contact

If you have any questions or issues, feel free to reach out at: [me@jorgerobles.com](mailto:me@jorgerobles.com)
