__author__ = 'dperez'

import pylab
import numpy as np
import matplotlib.pyplot as plt


def errorfill(x, y, yerr, ls, color=None, alpha_fill=0.3, ax=None):
    ax = ax if ax is not None else plt.gca()
    if color is None:
        color = ax._get_lines.color_cycle.next()
    if np.isscalar(yerr) or len(yerr) == len(y):
        ymin = [a - b for a,b in zip(y, yerr)]
        ymax = [sum(pair) for pair in zip(y, yerr)]
    elif len(yerr) == 2:
        ymin, ymax = yerr

    ax.plot(x, y, linestyle=ls, color=color)
    #print ymin
    #print y
    #print ymax
    ax.fill_between(x, ymax, ymin, color=color, alpha=alpha_fill)


def plot1(averages, last_line, bandits, numLines, output):
    max = np.max(averages)

    #Create a figure
    fig = pylab.figure()

    #Add a subplot (Grid of plots 1x1, adding plot 1)
    ax = fig.add_subplot(111)

    last_line = int(last_line)
    # ax.errorbar(range(0,last_line) , averages[0:last_line], yerr=std_err[0:last_line], label=alg_name, linestyle='-', linewidth=2)


    last_line = int(np.min((last_line*1.1, numLines)))



    errorfill(range(0,last_line) , averages[0:last_line], std_err[0:last_line], '-')

    ax.axhline(bandits,0,last_line, linestyle='--', linewidth=1, color='g')
    ax.annotate('Goal', xy=(0+1, bandits+1), xytext=(0+0.5, bandits+0.25))

    plt.title('Bandit Based EA')
    plt.xlabel('Evaluations')
    plt.ylabel('Fitness')

    plt.ylim([0,bandits*1.25])
    plt.xlim([0,last_line])

    if True:
        fig.savefig(output)

    print output, averages[last_line]

        # And show it:
    # if showPlot:
    #     plt.show()



NUM_REPS = 100
NUM_COLS = NUM_REPS + 1
showPlot = True
alg_name = "Bandit 1+1" #"1+1 ES" # "Bandit 1+1"
noise = 0
resampling = 1
noise_app = 1 if noise == 1 else 0

tot_avg = []
tot_std_dev = []
tot_std_err = []

for bandits in range(50, 1001, 50):
#for bandits in range(10, 41, 10):
# for bandits in range(250, 951, 10):

    filename = '../exp/resultsBanditNoise'+str(noise)+'_'+str(resampling)+'/results_'+str(NUM_REPS)+'_'+str(bandits)+'_'+str(noise)+'_'+str(resampling)+'.txt'
    output = '../exp/resultsBanditNoise'+str(noise)+'_'+str(resampling)+'/results_'+str(NUM_REPS)+'_'+str(bandits)+'_'+str(noise)+'_'+str(resampling)+'.pdf'

    results = np.genfromtxt(filename, delimiter=' ') # pylab.loadtxt(filename, comments='*', delimiter=' ')

    numLines = results.shape[0]
    averages = []
    std_devs = []
    std_err = []

    last_line = numLines
    found_at = []

    for row_idx in range(numLines):
        row = results[row_idx]

        avg = np.average(row[1:NUM_COLS])
        averages.append(avg)
        std_dev = np.std(row[1:NUM_COLS])
        std_devs.append(std_dev)
        std_err.append(std_dev / np.sqrt(NUM_REPS))

        if last_line == numLines and avg == bandits:
            last_line = row_idx+1

    # for col_idx in range(NUM_COLS-1):
    #     col = results[:,col_idx+1]
    #     for val in range(len(col)):
    #         if col[val] == bandits:
    #             found_at.append(val+1)
    #             break


    tot_avg.append(np.average(found_at))
    stddev = np.std(found_at)
    tot_std_dev.append(stddev)
    tot_std_err.append(stddev / np.sqrt(numLines))


    # plot1(averages, last_line, bandits, numLines, output)


print ("Noise: " + str(noise))
for i in range(len(tot_std_err)):
    print (str((i+1)*50) + "  " + str(tot_avg[i]) + " " + str(tot_std_err[i]))





#
# #Create a figure
# fig = pylab.figure()
#
# #Add a subplot (Grid of plots 1x1, adding plot 1)
# ax = fig.add_subplot(111)
#
# # last_line = int(last_line)
# # # ax.errorbar(range(0,last_line) , averages[0:last_line], yerr=std_err[0:last_line], label=alg_name, linestyle='-', linewidth=2)
# #
# #
# # last_line = int(np.min((last_line*1.1, numLines)))
#
#
#
# errorfill(range(10,1001,10) , tot_avg, tot_std_err, '-')
#
# # ax.axhline(bandits,0,last_line, linestyle='--', linewidth=1, color='g')
# # ax.annotate('Goal', xy=(0+1, bandits+1), xytext=(0+0.5, bandits+0.25))
#
# # plt.title('Bandit Based EA - One Max Problem, Noise ' + str(noise))
# plt.title('1+1 EA - One Max Problem, Noise ' + str(noise))
# plt.xlabel('Dimensions')
# plt.ylabel('Evaluations')
#
# # plt.ylim([0,bandits*1.25])
# # plt.xlim([0,last_line])
#
#
# # if True:
# #     fig.savefig("allNoise1plus1_"+str(noise)+".pdf")
#
#
#     # And show it:
# # if showPlot:
# #     plt.show()