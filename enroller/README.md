# CUNYFirst Class Enroller
**NOTE: THIS SCRIPT IS NOW OUTDATED AND LIKELY NO LONGER WORKS AS OF CUNYFIRST'S 9.2 UPDATE. THE USER INTERFACE HAS COMPLETELY CHANGED. I HAVE APPLIED FOR GRADUATION, AND AM THEREFORE UNABLE TO PROVIDE AN UPDATED VERSION.** 

A Python script that automatically enrolls you into a class on CUNYFirst. 

This script will check the status of a class (Open, Closed, or Wait Listed). If it is open or wait listed, the script will attempt to enroll you in the class. If the class is closed, the script will check every few seconds (you specify the exact amount of time between each check) to see if the status has changed. Once the script terminates (either having enrolled successfully or having encountered some sort of error) a sound effect of a bell ringing will play, letting you know that the program has taken some sort of action.

I was only able to test this using Queens College's courses and using a Queens College CUNYFirst account, so I am not sure if it will work with other CUNY colleges. Seeing as the CUNYFirst system is used at all CUNY schools, however, there should be no issues using this script for other CUNY schools. 

# Dependencies 
For this program to function correctly you must have Python installed, along with the selenium, webdriver-manager, playsound, and keyboard libaries for Python. Additionally, you must install Google Chrome.

- Download Python: https://www.python.org/downloads/
- Install selenium: `pip install selenium`
- Install webdriver-manager: `pip install webdriver-manager`
- Install playsound: `pip install playsound`
- Install keyboard: `pip install keyboard`

- Download Google Chrome: https://www.google.com/chrome/

# How to Use 
- Download and install Python, along with the other relevant dependencies as outlined above. 
- On CUNYFirst, add the class you wish to monitor to your Enrollment Shopping Cart. 
- Download the zip file of the cunyfirst-class-enroller Github archive, and extract it to a folder. 
- Run a terminal window in the folder that you extracted the zip file to, and enter `python enroller.py`.
- Follow the terminal prompts for user input. 

# Troubleshooting
If the script fails to work properly, here are a few things to try: 

- Make sure that the CUNYFirst system isn't down for maintenance.
- Make sure that the class you wish to monitor is in your Enrollment Shopping Cart.
- Make sure that you are eligible to enroll in classes (ex: no holds on your account, you are past your enrollment date and time, etc.).  
- Make sure that you aren't trying to enroll in a class with the same class number as a class that you are already enrolled in (ex: You can't enroll in multiple sections of ANTH 103). 
- Make sure that there isn't a time conflict between a class already in your schedule and the class you wish to enroll in. 

# Credits
The bell ringing sound effect was obtained from https://freesound.org, and was licensed under the Creative Commons 0 License (https://creativecommons.org/publicdomain/zero/1.0/). The specific sound effect that I used can be found at the following link: https://freesound.org/people/danarobinsondesignsgmailcom/sounds/377639/.  



