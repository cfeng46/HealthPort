# LifeDoc

Release 0.5 Beta

New Features:

Documents
- Upload multi-page documents via camera or phone storage
- Edit and remove documents
- View and search uploaded document list
- Preview and download documents
- Share documents via weblink

Contacts
- Add, edit, and remove contacts
- View Contacts

Account
- Two-factor authentication for registration
- Edit account information
- Password recovery

Bug Fixes
- As this is the first public release, there are no known bug fixes.

Known Bugs and Defects
- Faxing: Sending and receiving medical documents by fax is not currently implemented. Current Java fax API’s for Android are still in development

- Profiles: LifeDoc does not currently allow multiple profiles on one account 

- Automatic Keyboard Activation: When users land on the main Documents and Contacts pages, the search bar is automatically in focus, and the keyboard is called up.

- App Navigation after editing information: Whenever users add, edit, or delete contacts or documents, the app automatically navigates back to the home page before returning to the main Documents or Contacts page.


# Install Guide
  To begin using LifeDoc, simply go to the Play Store, search for LifeDoc, and download the application. Once LifeDoc is downloaded, you may select the LifeDoc icon in your application to launch the application. 
  During your first use, you must click “Are you new?” and register to create an account. You will receive a verification email. After you have verified your email, you may login and use all of LifeDoc’s features.


![bitmoji](https://render.bitstrips.com/v2/cpanel/9900e206-8e57-4079-846e-23178d0164c3-cdbe2b7c-88da-48cd-8e59-bc0e45fdb391-v1.png?transparent=1&palette=1&width=246)

Our Commit/Review Process:
- Create your branch
- Write code on your branch
- Push finished code to your branch
- Create a pull request with your code, describing what you did
- Comment @ someone in issue to review your code
- Only after a LGTM (looks good to me) can code be pushed to master

Our Done Criteria:
- Has been reviewed by at least one other team member
- The review must include running through the process of the individual user story that was completed
- The review must also include attempting breakage
- Only after this can it be moved to Done

Our Closed Criteria:
- Code must be pushed to master
- Only then can it be closed
