#!/bin/sh

for i in `seq 1 1 5`
do
	java -cp src particlesimulation.ParticleSimulation
done
exit 0
