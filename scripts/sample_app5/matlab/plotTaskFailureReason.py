from plotGenericLine import plot_generic_line

def plotTaskFailureReason():
    plot_generic_line(1, 10, 'Failed Task due to VM Capacity (%)', 'ALL_APPS', 'upper left', 1)
    
    plot_generic_line(1, 11, 'Failed Task due to Mobility (%)', 'ALL_APPS', 'upper left', 1)
    
    plot_generic_line(5, 5, 'Failed Tasks due to WLAN (%)', 'ALL_APPS', 'upper left', 1)

    plot_generic_line(5, 6, 'Failed Tasks due to MAN failure (%)', 'ALL_APPS', 'upper left', 1)

    plot_generic_line(5, 7, 'Failed Tasks due to WAN failure (%)', 'ALL_APPS', 'upper left', 1)

plotTaskFailureReason()