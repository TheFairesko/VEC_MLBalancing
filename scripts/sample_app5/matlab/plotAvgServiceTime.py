from plotGenericLine import plot_generic_line

def plot_avg_service_time():
    plot_generic_line(1, 5, 'Average Service Time (sec)', 'ALL_APPS', 'upper left', 0)
    # plot_generic_line(2, 5, 'Service Time on RSU (sec)', 'ALL_APPS', 'NorthWest', 0)
    # plot_generic_line(3, 5, 'Service Time on Cloud (sec)', 'ALL_APPS', 'NorthWest', 0)

plot_avg_service_time()