#!/bin/sh

sudo opcontrol --stop
sudo opcontrol --dump
sudo opreport -l cpu:1 /usr/bin/java
