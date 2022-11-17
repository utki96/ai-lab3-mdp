## System requirements to build and run the project:

1. Apache Maven - (3.6.0)
2. JDK - (1.8)
3. The code has been tested on `crackle1` server

## Steps:

1. Change the working directory to the root folder of this project.
2. Execute command: `mvn clean`
3. Execute command: `mvn package`
4. Now you have an executable jar in *"./target/lab3-mdp-1.jar"*
5. Run this jar file by this command:\
   ```java -jar target/lab3-mdp-1.jar [df=%v] [min] [tol=%v] [iter=%v] [file_path]```

Eg: ```java -jar target/lab3-mdp-1.jar df=1.0 min tol=0.01 iter=100 /home/utkarshtyg/Documents/restaurant.txt```

###
### Important points about passing arguments:

1. All arguments should be passed after the name of the jar file separated by spaces
2. Argument [file_path] is the only mandatory arg and should be passed at the end
3. Do not put spaces between argument and its values, Eg. df=1.0 is valid but df = 1.0 is not.