import matplotlib.pyplot as plt

def plot_avg_network_delay():
    plot_generic_line(1, 7, 'Average Network Delay (sec)', 'ALL_APPS', 'NorthWest', 0)
    plot_generic_line(5, 1, 'Average WLAN Delay (sec)', 'ALL_APPS', 'NorthWest', 0)
    plot_generic_line(5, 2, 'Average MAN Delay (sec)', 'ALL_APPS', 'NorthWest', 0)
    plot_generic_line(5, 3, 'Average WAN Delay (sec)', 'ALL_APPS', 'NorthWest', 0, 1, 1)
    plot_generic_line(5, 4, 'Average GSM Delay (sec)', 'ALL_APPS', 'NorthWest', 0, 1, 1, 0, [4, 1700])