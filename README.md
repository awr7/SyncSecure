<h1 align="center">SyncSecure</h1>

## Project Overview
SyncSecure is a Java-based project designed to generate and validate One-Time Passwords (OTPs), operating similarly to the well-known Duo application. This project is particularly significant in the context of enhancing security for various authentication processes.

### Main Features:

**Two-Component Architecture**: SyncSecure is divided into two main components:

+ DuoT (Token): Responsible for generating OTPs. It runs a GUI for user interaction, displaying the OTP and handling the generation logic.
+ DuoV (Validator): Serves as an authentication server. It validates the OTPs received from users against the expected values.

**Dynamic OTP Generation**:

Utilizing a mathematical algorithm based on seed values and random number generation, the project dynamically generates OTPs. This ensures a high level of security as each password is unique and valid only for a short duration.

**Network Communication**: 

The components communicate over a network, where DuoT sends seed and other required parameters to DuoV, which then calculates and validates the OTP.

**Efficient Communication Protocol:**

Efficient communication protocol between DuoT (Token) and DuoV (Validator). Initially, DuoT establishes a connection with DuoV and transmits just 6 bytes of data. After this brief exchange, the connection is closed but both components remain synchronized in generating and validating OTPs.

**Embedded Web Server:** 

DuoV includes an embedded Tomcat server, handling HTTP requests and providing a web interface for OTP validation.

**GUI Interface for OTP Display:**

DuoT features a user-friendly graphical interface displaying the current OTP and a button to generate a new one.

**Security Measures:** 

The project includes mechanisms to prevent OTP reuse and ensure that only the most recent OTPs are valid, enhancing security against replay attacks.

**Timer-Based OTP Refresh:** 

SyncSecure employs timers to refresh OTPs at regular intervals, making it more secure and user-friendly.

**Customizability:**

With its modular structure, the project allows for easy customization and integration into various systems requiring secure authentication methods.

In summary, SyncSecure is as a robust, flexible, and secure solution for generating and validating one-time passwords, tailored for systems where security and user authentication are top priority.

## Setting Up
Clone the repository and change directory.

`git clone https://github.com/awr7/SyncSecure`

`cd SyncSecure`

## Configuring the Dependencies
The project relies on external libraries located in the S2 folder. Specify the path to this folder by setting the T environment variable. Replace /path/to/S2 with the actual path where the S2 folder is located.

After editing the makefile run the following command:

`make`

This will compile the Java source files using the classpath specified in the Makefile.

## Running the project

Run the following commands once per session:

`$env:CPLIB = "path\to\S2\"`

`$env:CP = "$env:CPLIB\catalina.jar;$env:CPLIB\servlet-api.jar;$env:CPLIB\tomcat-juli.jar;$env:CPLIB\tomcat-util.jar;$env:CPLIB\tomcat-api.jar;$env:CPLIB\tomcat-coyote.jar"`

Then run these commands to start V (the authenticator) and T (the OTP generator)

`java -cp "$env:CP;." Duo V 2001` Starts V in port 2001 for example

`java Duo T localhost 2002` Start T in whaterver port V is in + 1

Then in your browser you can navigate to:

http://localhost:2001/go 

> [!NOTE]
> If you used a different port for Duo V, replace 2001 with the chosen port.
