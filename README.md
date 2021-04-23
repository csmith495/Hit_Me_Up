# Hit_Me_Up
Hit Me Up is a messaging application where each text message is coupled with a Korean translation. I created this application as a platform for those who are learning Korean to practice what they have learned so far. This application also comes with an on-device translator and on-device dictionary.

Tools Used:
* Google ML Kit On-Device Translation API
* Google Firebase
* Android Room Library

## Google ML Kit On-Device Translation API
This API allows developers to download translation models onto the application for free. Although the device has to be connected to the Internet to download the models, it is not needed to use them. In the screenshot below, you can see an example of the translator feature. You input a word or phrase into the text box. The button sends the input to the translator object, and the translator object returns a String that contains the translation of the input. The text box below the button shows the translation.

