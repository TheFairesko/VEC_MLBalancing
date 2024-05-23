import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import t
from getConfiguration import get_configuration

def plot_generic_line(row_offset, column_offset, y_label, app_type, legend_pos, calculate_percentage=0, divisor=1, ignore_zero_values=0, hide_lower_values=0, hide_x_axis=None):
    folder_path = get_configuration(1)
    num_simulations = get_configuration(3)
    step_of_x_axis = get_configuration(4)
    scenario_type = get_configuration(5)
    start_of_mobile_device_loop = get_configuration(10)
    step_of_mobile_device_loop = get_configuration(11)
    end_of_mobile_device_loop = get_configuration(12)
    balance_policy = get_configuration(51)
    num_of_mobile_devices = (end_of_mobile_device_loop - start_of_mobile_device_loop) // step_of_mobile_device_loop + 1

    all_results = np.zeros((num_simulations, len(scenario_type), num_of_mobile_devices))
    min_results = np.zeros((len(scenario_type), num_of_mobile_devices))
    max_results = np.zeros((len(scenario_type), num_of_mobile_devices))

    app_type = 'ALL_APPS' if 'app_type' == None else app_type
    divisor = 1 if 'divisor' !=None else divisor
    ignore_zero_values = 0 if 'ignore_zero_values'!=None else ignore_zero_values
    hide_lower_values = 0 if 'hide_lower_values'!=None else hide_lower_values

    if hide_x_axis!=None:
        hide_x_axis_start_value = hide_x_axis[1]
        hide_x_axis_index = hide_x_axis[0]

    for s in range(1,num_simulations+1):
        for i in range(len(scenario_type)):
            for j in range(num_of_mobile_devices):
                try:
                    mobile_device_number = start_of_mobile_device_loop + step_of_mobile_device_loop * j
                    
                    file_path = f"{folder_path}\\ite{s}\\SIMRESULT_ITS_SCENARIO_{scenario_type[i]}_{mobile_device_number}DEVICES_{app_type}_GENERIC.log"

                    with open(file_path) as f:
                        f = f.readlines()

                    read_data=[]
                    scip_rows=0
                    total_task=0
                    
                    for line in f:
                        if scip_rows==1:
                            row=[]
                            w_read_data=line.split(";")
                            w_read_data[-1]=w_read_data[-1][:-1]
                            for o in range(len(w_read_data)):
                                row.append(float(w_read_data[o]))
                            total_task = row[0] + row[1]
                            break
                        scip_rows+=1

                    scip_rows=0
            
                    for line in f:
                        if scip_rows<row_offset:
                            scip_rows+=1
                            continue
                        row=[]
                        w_read_data=line.split(";")
                        w_read_data[-1]=w_read_data[-1][:-1]
                        for o in range(len(w_read_data)):
                            row.append(float(w_read_data[o]))
                        
                        read_data.append(row)

                    value = read_data[0][column_offset-1]
                    if calculate_percentage == 1:
                        value = (100 * value) / total_task
                    all_results[s-1][i][j] = value

                except Exception as err:
                    print(err)

    if num_simulations == 1:
        results = all_results
    else:
        if ignore_zero_values == 1:
            results = np.sum(all_results, axis=0) / np.sum(all_results != 0, axis=0)
            # TODO: change NaN to 0
        else:
            results = np.mean(all_results, axis=0)

    results = np.squeeze(results)

    for i in range(len(scenario_type)):
        for j in range(num_of_mobile_devices):
            if results[i][j] < hide_lower_values:
                results[i][j] = np.nan
            else:
                results[i][j] /= divisor

    if hide_x_axis!=None:
        for j in range(num_of_mobile_devices):
            if j * step_of_mobile_device_loop + start_of_mobile_device_loop > hide_x_axis_start_value:
                results[hide_x_axis_index, j] = np.nan

    # for i in range(len(scenario_type)):
    #     for j in range(num_of_mobile_devices):
    #         x = results[i][j]
    #         SEM = np.std(x) / np.sqrt(len(x))
    #         ts = t.ppf([0.05, 0.95], len(x) - 1)
    #         CI = np.mean(x) + ts * SEM

    #         if CI[0] < 0:
    #             CI[0] = 0

    #         if CI[1] < 0:
    #             CI[1] = 0

    #         min_results[i, j] = results[i, j] - CI[0]
    #         max_results[i, j] = CI[1] - results[i][j]

    types = np.zeros(num_of_mobile_devices)
    for i in range(num_of_mobile_devices):
        types[i] = start_of_mobile_device_loop + (i * step_of_mobile_device_loop)

    fontSizeArray = get_configuration(8)

    # if get_configuration(20) == 1:
    #     for i in range(1, num_of_mobile_devices + 1):
    #         xIndex = start_of_mobile_device_loop + ((i - 1) * step_of_mobile_device_loop)
            
    #         markers = get_configuration(50)
    #         for j in range(len(scenario_type)):
    #             plt.plot(xIndex, results[j, i - 1], marker=markers[j], markerfacecolor=get_configuration(20 + j), color=get_configuration(20 + j))
    #             # plt.gca().set_prop_cycle(None)

    #     for j in range(len(scenario_type)):
    #         if get_configuration(19) == 1:
    #             plt.errorbar(types, results[j, :], min_results[j, :], max_results[j, :], fmt='-k', color=get_configuration(20 + j), linewidth=1)
    #         else:
    #             plt.plot(types, results[j, :], marker=markers[j], color=get_configuration(20 + j), linewidth=1)
    #     plt.gca().set_facecolor('none')
    # else:
    #     markers = get_configuration(40)
    #     for j in range(len(scenario_type)):
    #         if get_configuration(19) == 1:
    #             plt.errorbar(types, results[j, :], min_results[j, :], max_results[j, :], marker=markers[j], markerfacecolor='w', linewidth=1)
    #         else:
    #             plt.plot(types, results[j, :], marker=markers[j], markerfacecolor='w')
    #         # plt.gca().set_prop_cycle(None)

    markers = get_configuration(40)
    for j in range(len(scenario_type)):
        plt.plot(types, results[j, :], marker=markers[j], color=get_configuration(21 + j), linewidth=1)

    legends = get_configuration(6)
    lgnd = plt.legend(legends, fontsize=fontSizeArray[2], loc=legend_pos)

    # plt.title(balance_policy)
    plt.xlabel(get_configuration(9))
    ticks=np.arange(start_of_mobile_device_loop, end_of_mobile_device_loop+100, 200)
    plt.gca().set_xticks(ticks)
    # plt.gca().set_xticklabels(ticks, rotation=90)
    #plt.gca().set_xticklabels(np.arange(step / x_coefficient, end_of_mobile_device_loop / x_coefficient, step / x_coefficient))
    plt.ylabel(y_label)
    plt.gca().set_xlim([start_of_mobile_device_loop - 5, end_of_mobile_device_loop + 5])

    if get_configuration(17) == 1:
        xlim = plt.gca().get_xlim()
        ylim = plt.gca().get_ylim()
        plt.text(1.02 * xlim[1], 0.165 * ylim[1], 'x 10^2')

    # plt.gca().set_xlabel(get_configuration(9), fontsize=fontSizeArray[1])
    # plt.gca().set_ylabel(y_label, fontsize=fontSizeArray[1])
    #plt.ylim(0,25)

    if get_configuration(18) == 1:
        plt.savefig(f"{folder_path}\\{row_offset}_{column_offset}_{app_type}.pdf", format='pdf')

    plt.clf()