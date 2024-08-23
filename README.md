
# smw-android
A port of SMW to Android. <br>

Original Repository: https://github.com/snesrev/smw <br>

Use the instructions on the original repository to extract the **smw.dat** file from your rom and put it in **Android/data/net.freyta.smw/files** <br>  
Running the app once will create the directory. <br>

How to Change Settings:  <br>  
***Android/data/net.freyta.smw/files** contains **smw.ini**. Use a text editor to change options.<br>

###### Updated upstream
Default Settings:<br>
L3 Save State <br>
R3 Load State <br>

------

This is a cobbled together version from [Waterdish](https://github.com/Waterdish/zelda3-android). Full credit goes to them, I just ported the SMW code over.
=======
Default Settings for gamepad:  
L3 = Save State
L3 = Load State <br>

Default Settings for touch screen:
Start + R = Save State
Start + L = Load State
Known issues with touch screen: pressing B while holding Y will shortly release Y (You can't jump holding a shell).

*Note: Credit to [@yeticarus](https://github.com/yeticarus) for their touch screen code.* 

