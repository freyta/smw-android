
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
Default Settings for gamepad:  <br>
L3 = Save State<br>
L3 = Load State <br>

Default Settings for touch screen:<br>
Start + R = Save State<br>
Start + L = Load State<br>
Known issues with touch screen: pressing B while holding Y will shortly release Y (You can't jump holding a shell).<br>

*Note: Credit to [@yeticarus](https://github.com/yeticarus) for their touch screen code.* 


<h3>Instructions for creating smw.dat on android:</h3>
1. Download PyDroid: https://play.google.com/store/apps/details?id=ru.iiec.pydroid3&hl=en_US. Choose to skip any options that ask for money, you can do all of the following steps without paying.<br>
2. Open the hamburger menu at the top left of the app and select Pip.<br>
3. Type in "Pillow" without the quotes and it will have you install the repository app from the app store.<br>
4. Once the repository app is installed, you can install "Pillow" and "pyyaml" <br>
5. Download the <b>source code</b> zip file for smw. The zip file with the exe file in it will not work. <br>
6. Extract the zip file. <br>
7. Place your rom file in the main smw directory that you extracted, the same one as extract_assets.bat, and rename it to smw.sfc <br>
8. Open PyDroid again, open the hamburger menu, and select Terminal.<br>
9. Navigate to where you placed the rom file. (If you are unfamiliar with terminal commands, "ls" lists the folders and files and "cd Foldername" changes the directory.  <br> 
10. Paste in this command <code>python3 assets/restool.py --extract-from-rom</code> <br>
11. It should pause for a while and when it finishes you should be able to see smw.dat in the same folder as your rom. You can go ahead and copy that to the Android/data/net.freyta.smw/files location. <br>
