import matplotlib.pyplot as plt
import numpy as np


def plot_generic_scatter(y_label, x_label, legend_pos, vm_type='edge', app_type=1, draw_line=None):
    folder_path = "D:\\git-repos\\PhD\\EdgeCloudSim\\sim_results"
    scenario_type = get_configuration(5)
    simulation_time = get_configuration(2)
    start_of_mobile_device_loop = 2000
    step_of_mobile_device_loop = 2000
    end_of_mobile_device_loop = 2000
    num_of_mobile_devices = (end_of_mobile_device_loop - start_of_mobile_device_loop) // step_of_mobile_device_loop + 1

    if 'app_type' not in locals():
        app_type = 1

    if 'vm_type' not in locals():
        vm_type = 'edge'

    result_x = np.full((len(scenario_type), 5000), np.nan)
    result_y = np.full((len(scenario_type), 5000), np.nan)

    for s in range(len(scenario_type)):
        index = 0
        first_device_id = -1
        for j in range(num_of_mobile_devices):
            try:
                mobile_device_number = start_of_mobile_device_loop + step_of_mobile_device_loop * j
                file_path = f"{folder_path}\\ite11\\SIMRESULT_ITS_SCENARIO_{scenario_type[s]}_{mobile_device_number}DEVICES_SUCCESS.log"

                read_data = np.loadtxt(file_path, delimiter=';', skiprows=1)
                for k in range(len(read_data)):
                    if read_data[k, 6] == app_type and ((vm_type == 'edge' and read_data[k, 2] == 3) or (vm_type == 'cloud' and read_data[k, 2] != 3) or vm_type == 'all'):
                        if first_device_id == -1:
                            first_device_id = read_data[k, 1]
                        if read_data[k, 1] == first_device_id:
                            result_y[s, index] = read_data[k, 11] - read_data[k, 10]
                            result_x[s, index] = read_data[k, 11] / 60
                            index += 1
            except Exception as err:
                print(err)

    fig, ax = plt.subplots()
    for i in range(len(scenario_type)):
        ax.scatter(result_x[i], result_y[i])
        if draw_line is not None:
            y = [draw_line, draw_line]
            x = [0, simulation_time]
            ax.plot(x, y)

    legends = get_configuration(6)
    lgnd = ax.legend(legends, loc=legend_pos)
    if get_configuration(20) == 1:
        lgnd.set_visible(False)

    ax.axis('equal')
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

    if get_configuration(18) == 1:
        fig.savefig(f"{folder_path}\\{vm_type}_{app_type}.pdf", bbox_inches='tight')

    plt.show()