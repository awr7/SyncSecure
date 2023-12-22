# Path to the dependency directory, override as needed
T = /path/to/S2

# Classpath
CP = $(T)/catalina.jar:$(T)/servlet-api.jar:$(T)/tomcat-juli.jar: \
     $(T)/tomcat-util.jar:$(T)/tomcat-api.jar:$(T)/tomcat-coyote.jar

# Java compilation
Duo.class:	OTP.java Duo.java
	javac -cp $(CP):. OTP.java Duo.java

# Add more targets as necessary