__author__ = 'dperez'

import numpy as np

showPlot = True
noise = 0
noise_app = 1 if noise == 1 else 0

tot_avg = []
tot_std_err = []

NUM_REPS = 100
noises = [1]
resampling = 3

for noise in noises:
    print ("Noise: " + str(noise))
    i = 0
    for bandits in range(50, 1001, 50):

        #filename = '../exp/resultsBanditNoise'+str(noise)+'_'+str(resampling)+'/results_'+str(NUM_REPS)+'_'+str(bandits)+'_'+str(noise)+'_'+str(resampling)+'.res'
        filename = '../exp/results1plus1Noise'+str(noise)+'_'+str(resampling)+'/results_'+str(NUM_REPS)+'_'+str(bandits)+'_'+str(noise)+'_'+str(resampling)+'.res'

        # results = np.genfromtxt(filename, delimiter=' ') # pylab.loadtxt(filename, comments='*', delimiter=' ')

        with open(filename) as f:
            data = f.readlines()


            average = (data[3].split('=')[1])[1:-1]
            stderr = (data[5].split('=')[1])[1:-1]

            tot_avg.append(average)
            tot_std_err.append(stderr)

            print (str((i+1)*50) + "  " + str(tot_avg[i]) + " " + str(tot_std_err[i]))
            i=i+1

