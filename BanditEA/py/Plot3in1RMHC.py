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
    pylab.ylim([0,700000])
    ax.fill_between(x, ymax, ymin, color=color, alpha=alpha_fill)

tot_avg = []
tot_std_dev = []
tot_std_err = []


#filenames = ["../exp/resultsBanditNoise0_1.txt", "../exp/resultsBanditNoise1_1.txt", "../exp/resultsBanditNoise1_2.txt",  "../exp/resultsBanditNoise1_3.txt", "../exp/resultsBanditNoise1_4.txt", "../exp/resultsBanditNoise1_5.txt", "../exp/resultsBanditNoise1_10.txt"]
#algs = ["Bandit-EA Noise 0.0 (1)", "Bandit-EA Noise 1.0 (1)", "Bandit-EA Noise 1.0 (2)", "Bandit-EA Noise 1.0 (3)", "Bandit-EA Noise 1.0 (4)", "Bandit-EA Noise 1.0 (5)", "Bandit-EA Noise 1.0 (10)"]
#output = "../exp/banditEAOneMax.pdf"


filenames = ["../exp/results1plus1Noise0_1.txt", "../exp/results1plus1Noise1_1.txt", "../exp/results1plus1Noise1_2.txt", "../exp/results1plus1Noise1_3.txt", "../exp/results1plus1Noise1_4.txt", "../exp/results1plus1Noise1_5.txt", "../exp/results1plus1Noise1_10.txt"]
algs = ["RMHC Noise 0.0 (1)", "RMHC Noise 1.0 (1)", "RMHC Noise 1.0 (2)", "RMHC Noise 1.0 (3)", "RMHC Noise 1.0 (4)", "RMHC Noise 1.0 (5)", "RMHC Noise 1.0 (10)"]
output = "../exp/RMHCOneMax.pdf"


# filenames = ["../exp/resultsBanditNoise0_1.txt", "../exp/results1plus1Noise0_1.txt", "../exp/resultsBanditNoise1_2.txt", "../exp/results1plus1Noise1_2.txt"]#, "../exp/resultsBanditNoise1_3.txt", "../exp/results1plus1Noise1_3.txt"]
# algs = [     "Bandit-EA Noise 0.0 (1)",          "RMHC Noise 0.0 (1)",             "Bandit-EA Noise 1.0 (2)",          "RMHC Noise 1.0 (2)"]#,            "Bandit-EA Noise 1.0 (3)",          "RMHC Noise 1.0 (3)"]
# output = "../exp/OneMax12.pdf"

line_styles=['-','-','-','-','-','-','-']
num_algs = len(filenames)

steps = []
all_averages = []
all_stderr = []

for filename in filenames:

    results = np.genfromtxt(filename, delimiter=' ') # pylab.loadtxt(filename, comments='*', delimiter=' ')
    numLines = results.shape[0]

    if len(steps) < len (results[:,0]):
        steps = results[:,0]
    all_averages.append(results[:,2])
    std_err = results[:,3]
    all_stderr.append(std_err)


#Create a figure
fig = pylab.figure()

#Add a subplot (Grid of plots 1x1, adding plot 1)
ax = fig.add_subplot(111)

ax.set_color_cycle(['blue','green','red','cyan','magenta','yellow','black'])
#ax.set_color_cycle(['blue','red','cyan','magenta','yellow','black'])
for i in range(num_algs):

    errorfill(steps[0:len(all_averages[i])], all_averages[i], all_stderr[i], line_styles[i], None, 0.3, ax)
    #ax.errorbar(steps , all_averages[i], yerr=all_stderr[i], label=algs[i], linestyle=line_styles[i], linewidth=2)

#plt.title('Bandit Based EA - One Max Problem')
plt.title('RMHC - One Max Problem')
# plt.title('One Max Problem')

plt.xlabel('Dimension')
plt.ylabel('Evaluations')

plt.legend(algs, loc=2)

# plt.ylim([0,bandits*1.25])
# plt.xlim([0,last_line])


if True:
    fig.savefig(output)
