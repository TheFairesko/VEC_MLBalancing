import matplotlib.pyplot as plt

def plot_avg_qoe():
    plot_generic_line(1, 13, 'Average QoE (%)', 'ALL_APPS', 'SouthWest', 0)
    plot_generic_line(1, 13, 'QoE for Danger Assessment App (%)', 'DANGER_ASSESSMENT', 'SouthWest', 0)
    plot_generic_line(1, 13, 'QoE for Navigation App (%)', 'TRAFFIC_MANAGEMENT', 'SouthWest', 0)
    plot_generic_line(1, 13, 'QoE for Infotainment App (%)', 'INFOTAINMENT', 'SouthWest', 0)