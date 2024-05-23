#!/bin/sh

javac -classpath "C:/Diploma/EdgeCloudSim/lib/*" WekaModelCreator.java 
java -classpath "C:\Diploma\EdgeCloudSim\bin;C:\Diploma\EdgeCloudSim\lib\cloudsim-4.0.jar;C:\Diploma\EdgeCloudSim\lib\colt.jar;C:\Diploma\EdgeCloudSim\lib\commons-math3-3.6.1.jar;C:\Diploma\EdgeCloudSim\lib\jFuzzyLogic_v3.0.jar;C:\Diploma\EdgeCloudSim\lib\mtj-1.0.4.jar;C:\Diploma\EdgeCloudSim\lib\weka.jar;C:\Diploma\EdgeCloudSim\lib\json-simple-1.1.1.jar" WekaModelCreator config.json
