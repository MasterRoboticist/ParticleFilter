# ParticleFilter

<h3> App download as a .jar file </h3>
You will need to download the resources folder and put it in the same folder as the jar in order for the jar to work. To download the app, download ParticleFilterJava14.jar or ParticleFilterJava8.jar. The Java 14 jar may be more up to date than the Java 8 jar, though I will try to keep them more or less the same. If the Java 14 jar doesn't work, it may be because you don't have the most up to date version of Java. Simply try the Java 8 jar. <br>
<strong> Your computer will probably warn you you are openning a dangerous file. </strong> .jar files, by their nature, are a way for programmers to quickly package their code into a runnable file. That warning is just telling you there is no regulation over or gaurantee that the code in a jar file is not a virus.  It does not mean that the file actually contains anything dangerous. <br><br>
<strong> Mac users: </strong> You will probably need to go to System Preferences, Security and Privacy, and click "Open Anyway" in the general tab. Apple does not let you open apps from "unidentified developers" (people who don't pay Apple money) by default. <br>
<strong> Windows users: </strong> If windows tells you to choose an app to open the jar with, you may need to install java. You can download the JDK (Java Development Kit) from here: https://www.oracle.com/java/technologies/javase-downloads.html <br>
If you decide not to download java from that link, be very careful to download a JDK from the Oracle website only. There are many fake downloads out there.
<br><br>

<h3> Cloning the repo </h3>
Feel free to clone this repository and change the code.  We'll try to keep things commented, but I'm not making any promises on that front... <br>
You can also run the code this way even if you never want to change the code. In fact, it's the only way you can be gauranteed to have the latest version. You will need to download a Java IDE (i.e. Eclipse) if you don't already have one and figure out how to clone a git repo (i.e. this). Though I encourage new coders to try this, I don't recommend it because it requires some work on your part to figure everything out. Google is your best friend.
<br><br>

<h3> Bugs? </h3>
If you have any problems opening the jars, or find any bugs in the app, please let us know! We will try our best to fix them if we can.  If you have any ideas for improvements, also feel free to let us know. We may decide to implement it!
<br><br>

<h3> Known Issues </h3>
When robots hit walls very close to corners, they sometimes teleport to the next closest wall. I know why this happens, I just haven't fixed it yet.
The graphics look a little funny on Windows. This is an issue with Swing that we can only tweak and not fix.
When you change one of the text box values, it steals focus and you can't drive anymore. We haven't tried very hard to fix this one but we should be able to. In the meantime, check and uncheck one of the checkboxes to take back control.
