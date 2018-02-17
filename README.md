# Money Tracker
## CS4720: Mobile Application Development
## final-project-final-sudowoodo

Team: Sudowoodo

Members: Joe Chey (jc7yu) and Katherine Qian (kq3zm)

Github: https://github.com/UVA-CS4720-F17/final-project-final-sudowoodo

## Project Description
Money Tracker is an application that will serve consumers who wish to keep better track of their monthly expenditures. Budget management is an important aspect in life, and we believe that it would be useful to provide an efficient way to store and organize expenditure information. Through Money Tracker, users will be able to record and organize a variety of data regarding their expenditure, including name of expenditure, category, date/time, and description of the item, and photos.

Milestone Features 
- Have an activity screen for 'Login', 'Overview', 'List View', 'Add New Item'
- Login activity screen

  Implemented using SQLite (optional feature). The system checks for valid user ID and password, and will not navigate to the Main Activity page until the user information is verified. 
  A new user can make a new account using the bottom half of the page, which does not allow the new user to create an account with an existing ID.
  When all fields on the page are filled out (i.e., those for both returning and new users), the system checks for the returning user and carries out the verification process accordingly.
  Other checks include length restriction (i.e., the length of the ID and password must be greater than 0).

- Add New Item

  Accessed through clicking on the dots in the navigation bar in the top right --> 'Add Item'
  While this 'new item' will not save, clicking the "Save" button will navigate back to the main page, as well as display a toast after processing the user input.
  The Add Page contains the camera functionality (optional feature) and most of the necessary fields. 
  
- 'Overview' and 'List View' do not have much content yet but have working side navigation drawers
