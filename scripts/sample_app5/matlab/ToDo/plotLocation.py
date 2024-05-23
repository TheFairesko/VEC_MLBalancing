import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt

def plot_location():
    folder_path = "D:\\git-repos\\PhD\\EdgeCloudSim\\sim_results"
    scenario_type = get_configuration(5)
    start_of_mobile_device_loop = 500
    step_of_mobile_device_loop = 500
    end_of_mobile_device_loop = 1500
    num_of_mobile_devices = (end_of_mobile_device_loop - start_of_mobile_device_loop) // step_of_mobile_device_loop + 1
    place_count = 40

    results = np.zeros((num_of_mobile_devices, place_count))

    for s in range(1, num_of_simulations + 1):
        index_counter = 0
        for i in range(start_of_mobile_device_loop, end_of_mobile_device_loop + 1, step_of_mobile_device_loop):
            try:
                file_path = f"{folder_path}\\ite{s}\\SIMRESULT_ITS_SCENARIO_AI_BASED_{i}DEVICES_LOCATION.log"
                read_data = np.loadtxt(file_path, delimiter=';', skiprows=1)

                for j in range(1, place_count + 1):
                    results[index_counter, j - 1] += np.mean(read_data[:, j])
            except Exception as err:
                print(err)
            index_counter += 1

    results /= num_of_simulations

    x_values = [str(i) for i in range(start_of_mobile_device_loop, end_of_mobile_device_loop + 1, step_of_mobile_device_loop)]
    y_values = [str(i) for i in range(1, place_count + 1)]

    fig, ax = plt.subplots(figsize=(24, 6))
    cmap = sns.color_palette("viridis", as_cmap=True)
    sns.heatmap(results, xticklabels=y_values, yticklabels=x_values, cmap=cmap, ax=ax)

    ax.set_title('Mean number of vehicles per places', fontsize=11)
    ax.set_xlabel('Place IDs')
    ax.set_ylabel('#Vehicle in simulation')

    if get_configuration(18) == 1:
        fig.savefig(f"{folder_path}\\position.pdf", bbox_inches='tight')

    plt.show()