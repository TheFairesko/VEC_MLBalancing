import numpy as np
import matplotlib.pyplot as plt

def plot_delay_reason_as_bar(is_edge):
    folder_path = get_configuration(1)
    num_simulations = get_configuration(3)
    step_of_x_axis = 3  # get_configuration(4)
    start_of_mobile_device_loop = get_configuration(10)
    step_of_mobile_device_loop = get_configuration(11)
    end_of_mobile_device_loop = get_configuration(12)
    num_of_mobile_devices = (end_of_mobile_device_loop - start_of_mobile_device_loop) // step_of_mobile_device_loop + 1

    all_results = np.zeros((num_simulations, num_of_mobile_devices, 2))

    if 'isEdge' not in locals():
        is_edge = 1

    for s in range(num_simulations):
        for j in range(num_of_mobile_devices):
            try:
                mobile_device_number = start_of_mobile_device_loop + step_of_mobile_device_loop * (j)
                file_path = f"{folder_path}\\ite{s}\\SIMRESULT_ITS_SCENARIO_AI_BASED_{mobile_device_number}DEVICES_ALL_APPS_GENERIC.log"

                read_data = np.loadtxt(file_path, delimiter=';', skiprows=1)
                value1 = 0
                value2 = 0
                if is_edge == 1:
                    value1 = read_data[1, 4]
                    value2 = read_data[1, 5]
                else:
                    value1 = read_data[2, 4]
                    value2 = read_data[2, 5]

                all_results[s, j, 0] = value2
                all_results[s, j, 1] = value1 - value2
            except Exception as e:
                print(e)

    results = np.mean(all_results, axis=0) if num_simulations > 1 else all_results
    results = np.squeeze(results)  # remove singleton dimensions

    fig, ax = plt.subplots()
    ax.bar(results, color=[[.45, .45, .45], [.90, .90, .90]])
    ax.set_xlabel(get_configuration(9))
    ax.set_ylabel('Service Time (sec)')
    ax.set_xticks(np.arange(0, num_of_mobile_devices + 1, step_of_x_axis))
    ax.set_xticklabels((start_of_mobile_device_loop * step_of_x_axis):(step_of_x_axis * step_of_mobile_device_loop):end_of_mobile_device_loop)
    ax.set_xlim([0, num_of_mobile_devices + 1])

    if is_edge == 1:
        ax.set_ylabel('Service Time on Edge (sec)')
        ax.legend(['processing time', 'WLAN delay'], loc='upper left')
        filename = 'edge_delay_reason'
    else:
        ax.set_ylabel('Service Time on Cloud (sec)')
        ax.legend(['processing time', 'WAN delay'], loc='upper left')
        filename = 'cloud_delay_reason'

    plt.gcf().set_size_inches(get_configuration(7)[2:])
    plt.gcf().set_dpi(100)

    if get_configuration(18) == 1:
        fig.savefig(f"{folder_path}\\{filename}.pdf", bbox_inches='tight')

    plt.show()