from plotGenericLine import plot_generic_line

def plot_avg_failed_task():
    plot_generic_line(1, 2, 'Average Failed Tasks (%)', 'ALL_APPS', 'upper left', 1)
    # plot_generic_line(1, 2, 'Failed Tasks for Danger Assessment App (%)', 'DANGER_ASSESSMENT', 'NorthWest', 1)
    # plot_generic_line(1, 2, 'Failed Tasks for Navigation App (%)', 'TRAFFIC_MANAGEMENT', 'NorthWest', 1)
    # plot_generic_line(1, 2, 'Failed Tasks for Infotainment App (%)', 'INFOTAINMENT', 'NorthWest', 1)

plot_avg_failed_task()