import matplotlib.pyplot as plt

def plot_avg_processing_time():
    plot_generic_line(1, 6, 'Average Processing Time (sec)', 'ALL_APPS', 'NorthWest', 0)
    plot_generic_line(2, 6, 'Processing Time on RSU (sec)', 'ALL_APPS', 'NorthWest', 0)
    plot_generic_line(3, 6, 'Processing Time on Cloud (sec)', 'ALL_APPS', 'NorthWest', 0)