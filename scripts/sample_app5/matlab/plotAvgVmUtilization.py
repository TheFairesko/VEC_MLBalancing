from plotGenericLine import plot_generic_line

def plot_avg_vm_utilization():
    plot_generic_line(2, 8, 'Average VM Utilization of Edge (%)', 'ALL_APPS', 'upper left', 0)
    plot_generic_line(3, 8, 'Average VM Utilization of Cloud (%)', 'ALL_APPS', 'upper left', 0)

plot_avg_vm_utilization()