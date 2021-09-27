@echo off
title blueberry-1.0.0
java -Xloggc:gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -server -jar blueberry-1.0.0.jar