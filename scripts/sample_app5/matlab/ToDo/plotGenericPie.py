import matplotlib.pyplot as plt

def plot_generic_pie(vm_type='edge', app_type=1, threshold=0):
    folder_path = "D:\\git-repos\\PhD\\EdgeCloudSim\\sim_results"
    scenario_type = get_configuration(5)
    start_of_mobile_device_loop = 2000
    step_of_mobile_device_loop = 2000
    end_of_mobile_device_loop = 2000
    num_of_mobile_devices = (end_of_mobile_device_loop - start_of_mobile_device_loop) // step_of_mobile_device_loop + 1

    if 'app_type' not in locals():
        app_type = 1

    if 'vm_type' not in locals():
        vm_type = 'edge'

    total = np.zeros(len(scenario_type))
    found = np.zeros(len(scenario_type))

    for s in range(len(scenario_type)):
        for j in range(num_of_mobile_devices):
            try:
                mobile_device_number = start_of_mobile_device_loop + step_of_mobile_device_loop * j
                file_path = f"{folder_path}\\ite11\\SIMRESULT_ITS_SCENARIO_{scenario_type[s]}_{mobile_device_number}DEVICES_SUCCESS.log"

                read_data = np.loadtxt(file_path, delimiter=';', skiprows=1)
                for k in range(len(read_data)):
                    if read_data[k, 6] == app_type and ((vm_type == 'edge' and read_data[k, 2] == 3) or (vm_type == 'cloud' and read_data[k, 2] != 3) or vm_type == 'all'):
                        if read_data[k, 11] - read_data[k, 10] > threshold:
                            found[s] += 1
                        total[s] += 1
            except Exception as err:
                print(err)

    fig, ax = plt.subplots()
    ax.pie(found, labels=scenario_type, autopct='%1.1f%%', startangle=90)
    ax.axis('equal')

    if get_configuration(18) == 1:
        fig.savefig(f"{folder_path}\\{vm_type}_{app_type}.pdf", bbox_inches='tight')

    plt.show()