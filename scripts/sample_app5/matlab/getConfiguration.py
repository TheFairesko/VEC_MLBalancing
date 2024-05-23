def get_configuration(argType):
    if argType == 1:
        ret_val = 'C:\Diploma\EdgeCloudSim\sim_results'
    elif argType == 2:
        ret_val = 60  # simulation time (in minutes)
    elif argType == 3:
        ret_val = 1  # Number of iterations
    elif argType == 4:
        ret_val = 3  # x tick interval for number of mobile devices
    elif argType == 5:
        ret_val = ['AI_BASED', 'GAME_THEORY', 'PREDICTIVE', 'MAB', 'RANDOM']
    elif argType == 6:
        ret_val = ['ML-based', 'Game-based', 'SMA-based', 'MAB', 'random']
    elif argType == 7:
        ret_val = [6, 8, 4, 8]  # position of figure
    elif argType == 8:
        ret_val = [13, 12, 12]  # font size for x/y label, legend and x/y axis
    elif argType == 9:
        ret_val = 'Number of Vehicles'  # Common text for x axis
    elif argType == 10:
        ret_val = 100  # min number of mobile device
    elif argType == 11:
        ret_val = 100  # step size of mobile device count
    elif argType == 12:
        ret_val = 2100  # max number of mobile device
    elif argType == 17:
        ret_val = 0  # return 1 if you want to add 10^n text at x axis
    elif argType == 18:
        ret_val = 1  # return 1 if you want to save figure as pdf
    elif argType == 19:
        ret_val = 0  # return 1 if you want to plot errors
    elif argType == 20:
        ret_val = 1  # return 1 if graph is plotted colorful
    elif argType == 21:
        ret_val = '#800080'  # color of first line
    elif argType == 22:
        ret_val = "#4169e1"  # color of second line
    elif argType == 23:
        ret_val = "#ff1493"  # color of third line
    elif argType == 24:
        ret_val =  "#000080"  # color of fourth line
    elif argType == 25:
        ret_val = "#8b4513"  # color of fifth line
    elif argType == 26:
        ret_val = (0, 0.8, 0.8)  # color of sixth line
    elif argType == 27:
        ret_val = (0.8, 0.4, 0)  # color of seventh line
    elif argType == 28:
        ret_val = (0.8, 0.8, 0)  # color of eighth line
    elif argType == 40:
        ret_val = ['*', 'o', 's', 'v', 'p', 'd', 'x', 'h']  # line style (marker) of the colorless line
    elif argType == 50:
        ret_val = ['*', 'o', 's', 'v', 'p', 'd', 'x', 'h']  # line style (marker) of the colorful line
    elif argType == 51:
        ret_val = "MULTILAYER-PERCEPRTRON"
    else:
        ret_val = None

    return ret_val