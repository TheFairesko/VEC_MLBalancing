#!/bin/sh
rm -rf ../../bin
mkdir ../../bin
javac -classpath "C:/Diploma/EdgeCloudSim/lib/*" -sourcepath ../../src ../../src/edu/boun/edgecloudsim/applications/sample_app5/VehicularMainApp.java -d ../../bin
