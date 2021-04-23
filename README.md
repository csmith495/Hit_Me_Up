# Hit_Me_Up
Hit Me Up is a messaging application where each text message is coupled with a Korean translation. I created this application as a platform for those who are learning Korean to practice what they have learned so far. This application also comes with an on-device translator and on-device dictionary.

Tools Used:
* Google ML Kit On-Device Translation API
* Google Firebase
* Android Room Library

## Google ML Kit On-Device Translation API
This API allows developers to download translation models onto the application for free. Although the device has to be connected to the Internet to download the models, it is not needed to use them. In the screenshot below, you can see an example of the translator feature. You input a word or phrase into the text box. The button sends the input to the translator object, and the translator object returns a String that contains the translation of the input. The text box below the button shows the translation.

![Screenshot_20210423-034338 1](https://user-images.githubusercontent.com/54324155/115939270-3f8f3700-a463-11eb-9388-c68359c6bbdb.png)


## Dictionary (On-Device Database)
The next two screenshots show the on-device database. The first screenshot shows where the user can create categories and place words and phrases that best fit that category. The second screenshot shows a word that is coupled with a translation. This can help the user keep track of any word or phrase they want to learn without using the Internet to look up a translation. When the user does not need to learn the word or phrase anymore, they can either replace it with another word or phrase, or the user can delete it. The same goes with the category instance. I structured the dicitonary feature this way to give the user more organization.

![Screenshot_20210416-010344~2 1](https://user-images.githubusercontent.com/54324155/115939307-56ce2480-a463-11eb-8a8e-42fca7bdf704.png)
![Screenshot_20210416-010338~2 1](https://user-images.githubusercontent.com/54324155/115939367-8d0ba400-a463-11eb-82bf-525cf04ca3ff.png)


## Messenger
The last screenshot shows the messenger feature of the application. This feature uses the Translation API and Firebase Realtime Database. When a user texts another, the message they send is coupled with a translation thanks to the Translation API. The message is then stored on the Firebase Realtime Database. Each conversation is stored on its own branch. The name of the branch is composed...

![Screenshot_20210419-232333 1](https://user-images.githubusercontent.com/54324155/115939379-9ac12980-a463-11eb-9142-e493f7cc7ff9.png)
