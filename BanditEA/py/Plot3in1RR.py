__author__ = 'dperez'

import pylab
import numpy as np
import matplotlib.pyplot as plt
import matplotlib
import math


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
    ax.fill_between(x, ymax, ymin, color=color, alpha=alpha_fill)

tot_avg = []
tot_std_dev = []
tot_std_err = []


# filenames = ["../exp/results1plus1RRNoise0_1.txt", "../exp/results1plus1RRNoise1_1.txt"]
# algs = ["RMHC Noise 0.0 (1)", "RMHC Noise 1.0 (1)"]
# output = ["../exp/RMHCRoyalRoad_0_1.pdf", "../exp/RMHCRoyalRoad_1_1.pdf"]


filenames = ["../exp/resultsBanditRRNoise0_1.txt", "../exp/results1plus1RRNoise0_1.txt"]
algs = ["Bandit EA Noise 0.0 (1)", "RMHC Noise 0.0 (1)"]
output = ["../exp/BanditEARoyalRoad_0_1.pdf", "../exp/RMHCRoyalRoad_0_1.pdf"]
titles = ["Bandit EA - Royal Road Problem", "RMHC - Royal Road Problem"]


# filenames = ["../exp/results1plus1Noise0_1.txt", "../exp/results1plus1Noise1_2.txt", "../exp/results1plus1Noise1_3.txt", "../exp/results1plus1Noise1_4.txt", "../exp/results1plus1Noise1_5.txt", "../exp/results1plus1Noise1_10.txt"]
# algs = ["Bandit-EA Noise 0.0 (1)", "Bandit-EA Noise 1.0 (1)", "Bandit-EA Noise 1.0 (2)", "Bandit-EA Noise 1.0 (3)", "Bandit-EA Noise 1.0 (4)", "Bandit-EA Noise 1.0 (5)", "Bandit-EA Noise 1.0 (10)"]
# output = "../exp/banditEAOneMax.pdf"

# filenames = ["../exp/resultsBanditNoise0_1.txt", "../exp/results1plus1Noise0_1.txt", "../exp/resultsBanditNoise1_2.txt"]#, "../exp/results1plus1Noise1_2.txt"]#, "../exp/resultsBanditNoise1_3.txt", "../exp/results1plus1Noise1_3.txt"]
# algs = [     "Bandit-EA Noise 0.0 (1)",          "RMHC Noise 0.0 (1)",             "Bandit-EA Noise 1.0 (2)"]#,          "RMHC Noise 1.0 (2)"]#,            "Bandit-EA Noise 1.0 (3)",          "RMHC Noise 1.0 (3)"]
# output = "../exp/OneMax11.pdf"

line_styles=['-','-','-','-','-','-','-']
num_algs = len(filenames)
bandits = [64,128,256,512,1024]
blocks = [2,4,8,16,32]

steps = []
all_averages = []
all_stderr = []
n_bandits = len(bandits)
n_blocks = len(blocks)


f_idx = 0
for filename in filenames:


    #Create a figure
    fig = pylab.figure()

    #Add a subplot (Grid of plots 1x1, adding plot 1)
    ax = fig.add_subplot(111)


    ax.get_yaxis().set_major_formatter(
        matplotlib.ticker.FuncFormatter(lambda x, p: format(int(x), ',')))


    ax.get_xaxis().set_major_formatter(
        matplotlib.ticker.FuncFormatter(lambda x, p: format(int(x), ',')))

    results = np.genfromtxt(filename, delimiter=' ') # pylab.loadtxt(filename, comments='*', delimiter=' ')
    numLines = results.shape[0]
    n_bandits = len(bandits)
    legend = []

    for l in range(len(blocks)):

        avg_block = []
        stderr_block = []
        valid = 0

        for i in range(len(bandits)):

            line = results[l*n_bandits + i]
            if not math.isnan(line[4]):
                valid += 1

            avg_block.append(line[4])
            stderr_block.append(line[5])

        if valid>1:
            legend.append(algs[f_idx] + " - Block size: " + str(blocks[l]))
            errorfill(bandits, avg_block, stderr_block, '-', None, 0.3, ax)


    #plt.title('Bandit Based EA - Royal Road Problem')
    plt.title(titles[f_idx])
    plt.xlabel('Dimensions')
    plt.ylabel('Evaluations')

    plt.legend(legend, loc=2)

    # plt.ylim([0,bandits*1.25])
    # plt.xlim([0,last_line])

    plt.xticks(bandits)
    fig.savefig(output[f_idx])

    f_idx += 1