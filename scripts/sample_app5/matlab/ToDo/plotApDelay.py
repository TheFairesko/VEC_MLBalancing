import numpy as np
import matplotlib.pyplot as plt

def plotApDelay():
    folderPath = getConfiguration(1)
    numOfSimulations = getConfiguration(3)
    stepOfxAxis = getConfiguration(4)
    startOfMobileDeviceLoop = getConfiguration(10)
    stepOfMobileDeviceLoop = getConfiguration(11)
    endOfMobileDeviceLoop = getConfiguration(12)
    numOfMobileDevices = int((endOfMobileDeviceLoop - startOfMobileDeviceLoop) / stepOfMobileDeviceLoop) + 1
    placeTypes = ['AP 1 (60 km/h)', 'AP 4 (40 km/h)', 'AP 11 (20 km/h)']

    results = np.zeros((len(placeTypes), numOfMobileDevices))

    for s in range(1, numOfSimulations + 1):
        indexCounter = 0
        for i in range(startOfMobileDeviceLoop, endOfMobileDeviceLoop + 1, stepOfMobileDeviceLoop):
            try:
                filePath1 = f"{folderPath}\\ite{s}\\SIMRESULT_ITS_SCENARIO_AI_BASED_{i}DEVICES_AP_UPLOAD_DELAY.log"
                filePath2 = f"{folderPath}\\ite{s}\\SIMRESULT_ITS_SCENARIO_AI_BASED_{i}DEVICES_AP_DOWNLOAD_DELAY.log"
                readData1 = np.loadtxt(filePath1, delimiter=';', skiprows=60)
                readData2 = np.loadtxt(filePath2, delimiter=';', skiprows=60)

                for j in range(len(placeTypes)):
                    results[j, indexCounter] += np.mean(readData1[:, j + 1]) + np.mean(readData2[:, j + 1])
            except Exception as err:
                print(err)
            indexCounter += 1

    results /= numOfSimulations

    types = np.zeros(numOfMobileDevices)
    for i in range(numOfMobileDevices):
        types[i] = startOfMobileDeviceLoop + (i * stepOfMobileDeviceLoop)

    hFig = plt.figure()
    pos = getConfiguration(7)
    fontSizeArray = getConfiguration(8)
    hFig.set_size_inches(pos[2], pos[3])
    plt.rcParams['font.family'] = 'Times New Roman'
    plt.rcParams['font.size'] = fontSizeArray[2]

    if getConfiguration(20) == 1:
        for i in range(stepOfxAxis, numOfMobileDevices + 1, stepOfxAxis):
            xIndex = startOfMobileDeviceLoop + ((i - 1) * stepOfMobileDeviceLoop)

            markers = [':k*', ':ko', ':ks', ':kv']
            for j in range(len(placeTypes)):
                plt.plot(xIndex, results[j, i], markers[j], markerfacecolor=getConfiguration(20 + j), color=getConfiguration(20 + j))
                plt.hold(True)

        for j in range(len(placeTypes)):
            plt.plot(types, results[j, :], ':k', color=getConfiguration(20 + j), linewidth=1.5)
            plt.hold(True)

        plt.gca().set_facecolor('none')
    else:
        markers = ['-k*', '-ko', '-ks', '-kv']
        for j in range(len(placeTypes)):
            plt.plot(types, results[j, :], markers[j], markerfacecolor='w', linewidth=1.4)
            plt.hold(True)

    lgnd = plt.legend(placeTypes, loc='upper left')
    if getConfiguration(20) == 1:
        lgnd.set_facecolor('none')

    if getConfiguration(17) == 0:
        manualXAxisCoefficent = 1

    plt.hold(False)
    plt.axis('square')
    plt.xlabel(getConfiguration(9))
    plt.xticks(np.arange(startOfMobileDeviceLoop + stepOfMobileDeviceLoop, (stepOfxAxis * stepOfMobileDeviceLoop) + 1, endOfMobileDeviceLoop))
    plt.ylabel('Average Network Delay (sec)')
    plt.xlim(startOfMobileDeviceLoop - 5, endOfMobileDeviceLoop + 5)

    if getConfiguration(17) == 1:
        xlim = plt.xlim()
        ylim = plt.ylim()
        plt.text(1.02 * xlim[1], 0.165 * ylim[1], 'x 10^2')

    plt.xlabel(getConfiguration(9), fontsize=fontSizeArray[0])
    plt.ylabel('Average Network Delay (sec)', fontsize=fontSizeArray[0])
    lgnd.set_fontsize(fontSizeArray[1])

    if getConfiguration(18) == 1:
        plt.savefig(f"{folderPath}\\apDelay.pdf", format='pdf')

    plt.show()

plotApDelay()