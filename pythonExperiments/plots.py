import json
import matplotlib.pyplot as plt
import numpy as np
from scipy.interpolate import spline


Colors = [ '#4363d8', '#f58231', '#911eb4', '#46f0f0',  '#46f0f0', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000']

def plotting(FileListInconsistency, FileListKB, Sample):
    x = 0
    SString = "StatResults/Sampled/"
    sString = "StatResults/"
    incon = "/InconsistencyStatistics.json"
    kbStaaa = "/kbStatistics.json"
    labels1 = []

    ColorScheme = {'DBpedia': '#f5a3b8', 'Yago': '#94dc9b', 'DBLP': '#f58231', "LOD" : "#4363d8"} 
    FileListInconsistencySample =  ["StatResults/Sampled/"+'DBpedia'+incon, "StatResults/Sampled/"+'Yago'+incon]
    labels2 = []
    counter = 0
    for file in FileListInconsistencySample:
        datasetName = file.split("/")[-2]
        if not(datasetName in ColorScheme):
            ColorScheme[datasetName] = Colors[len(ColorScheme.keys())]
        labels1.append(datasetName+"-20%")
        json_file = open(file, 'r')
        data = json.load(json_file)
        IDCoor = []
        Size = [i for i in range(0,25)]
        SizeCount = [0 for i in range(0,25)]
        SizeCoor = []
        SizeSum = [0 for i in range(0,25)]
        CountCoor = []
        for elem in data:
            CountCoor.append(elem["Count"])
            SizeCoor.append(elem["Size"])
            IDCoor.append(elem["id"])
            if(elem["Count"] > 0):
                SizeCount[elem["Size"]] += 1

            SizeSum[elem["Size"]] += elem["Count"]
        plt.figure(1+x)
        plt.title('Plot showing the Distribution of Size')
        plt.plot(Size, SizeCount, color=ColorScheme[datasetName],label=datasetName+"-20%")
        plt.ylabel('Count of different "Anti-pattern" types with the same size')
        plt.xlabel('Size of the "Anti-pattern"')

        fig = plt.figure(2+x)
        ax = fig.add_subplot(111)
        SizeSum[:] = [x / (float(sum(SizeSum))+0.0000000001) for x in SizeSum]
        plt.title('Plot showing the normalised Distribution of Occurences')
        Size = np.arange(0,25)
        try:
            ax.bar(Size+0.5*counter, SizeSum, 0.5, color=ColorScheme[datasetName],label=datasetName+"-20%")
        except:
            pass
        counter += 1
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('Size of the "Anti-pattern"')
        CountCoor.sort(reverse = True)
        CountCoor[:] = [x / float(sum(CountCoor)+0.0000001) for x in CountCoor]
        plt.figure(3+x)
        plt.title('Plot showing the normalised Distribution of Occurences')
        plt.plot( CountCoor, color=ColorScheme[datasetName],label=datasetName+"-20%")
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('"Anti-pattern" type')

    x = 0
    ColorScheme = {'DBpedia': '#e6194b', 'Yago': '#3cb44b', 'DBLP': '#f58231', "LOD" : "#4363d8"} 
    counter = 0

    labels2 = []
    width = (1-0.1)/(len(FileListInconsistency)*2)
    for file in FileListInconsistency:
        datasetName = file.split("/")[-2]
        if not(datasetName in ColorScheme):
            ColorScheme[datasetName] = Colors[len(ColorScheme.keys())]
        labels1.append(datasetName)
        json_file = open(file, 'r')
        data = json.load(json_file)
        IDCoor = []
        Size = [i for i in range(0,25)]
        SizeCount = [0 for i in range(0,25)]
        SizeCoor = []
        SizeSum = [0 for i in range(0,25)]
        CountCoor = []
        for elem in data:
            CountCoor.append(elem["Count"])
            SizeCoor.append(elem["Size"])
            IDCoor.append(elem["id"])
            if(elem["Count"] > 0):
                SizeCount[elem["Size"]] += 1

            SizeSum[elem["Size"]] += elem["Count"]
        plt.figure(1+x)
        plt.title('Plot showing the Distribution of Size')
        plt.plot(Size, SizeCount, color=ColorScheme[datasetName], label=datasetName)
        plt.ylabel('Count of different "Anti-pattern" types with the same size')
        plt.xlabel('Size of the "Anti-pattern"')

        fig = plt.figure(2+x)
        ax = fig.add_subplot(111)
        SizeSum[:] = [x / (float(sum(SizeSum))+0.0000000001) for x in SizeSum]
        plt.title('Plot showing the normalised Distribution of Occurences')
        Size = np.arange(0,25)
        try:
            ax.bar(Size+0.5*counter, SizeSum, 0.25, color=ColorScheme[datasetName], label=datasetName)
        except:
            pass
        counter += 1
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('Size of the "Anti-pattern"')
        CountCoor.sort(reverse = True)
        CountCoor[:] = [x / float(sum(CountCoor)+0.0000001) for x in CountCoor]
        plt.figure(3+x)
        plt.title('Plot showing the normalised Distribution of Occurences')
        plt.plot( CountCoor, color=ColorScheme[datasetName], label=datasetName)
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('"Anti-pattern" type')


    for file in FileListKB:
        json_file = open(file, 'r')
        data = json.load(json_file)

        datasetName = file.split("/")[-2]
        labels2.append(datasetName)
        if not(datasetName in ColorScheme):
            ColorScheme[datasetName] = Colors[len(ColorScheme.keys())]
        XcoorIn = []
        YcoorIn = []
        for key in sorted(data["InDegree"].keys(), key=int):
            XcoorIn.append(int(key))
            YcoorIn.append(int(data["InDegree"][key]))

        plt.figure(4+x)
        plt.title('Plot showing the Distribution of the nodes with inlinks')
        plt.plot(XcoorIn, YcoorIn, color=ColorScheme[datasetName])
        plt.ylabel('Occurences of the nodes with inlinks')
        plt.xlabel('Amount of inlinks towards the node')


        XcoorOut = []
        YcoorOut = []
        for key in sorted(data["OutDegree"].keys(), key=int):
            XcoorOut.append(int(key))
            YcoorOut.append(int(data["OutDegree"][key]))

        plt.figure(5+x)
        plt.title('Plot showing the Distribution of the nodes with outlinks')
        plt.plot(XcoorOut, YcoorOut, color=ColorScheme[datasetName])
        plt.ylabel('Occurences of the nodes with equal outlinks')
        plt.xlabel('Amount of outlinks outwards the node')

        XcoorCluster = []
        YcoorCluster = []

        for key in sorted(data["ClusteringCoefficient"].keys(), key=float):
            XcoorCluster.append(float(key))
            YcoorCluster.append(int(data["ClusteringCoefficient"][key]))
        # xnew = np.linspace(min(XcoorCluster),max(XcoorCluster),300) #300 represents number of points to make between T.min and T.max

        # power_smooth = spline(XcoorCluster,YcoorCluster,xnew)
        plt.figure(6+x)
        plt.title('Plot showing the Distribution of the nodes with equal ClusteringCoefficient')
        #plt.plot(xnew,power_smooth, color=ColorScheme[datasetName])
        plt.plot(XcoorCluster, YcoorCluster, color=ColorScheme[datasetName])
        plt.ylabel('Occurences of the nodes with equal ClusteringCoefficient')
        plt.xlabel('ClusteringCoefficient of the node')



    for current in plt.get_fignums():



        fig = plt.figure(current)
        ax = plt.gca()
        # # Remove the plot frame lines. They are unnecessary chartjunk.

        ax.spines["top"].set_visible(False)
        ax.spines["bottom"].set_visible(False)
        ax.spines["right"].set_visible(False)
        ax.spines["left"].set_visible(False)

        if current == 2:
            ax.set_yscale("log", nonposy='clip')
        if current >= 3:
            ax.set_yscale("log", nonposy='clip')
            ax.set_xscale("log", nonposy='clip')
        # # Ensure that the axis ticks only show up on the bottom and left of the plot.
        # # Ticks on the right and top of the plot are generally unnecessary chartjunk.
        ax.get_xaxis().tick_bottom()
        ax.get_yaxis().tick_left()
        ax.legend(loc='upper right')
        elemPLT = int(current)
        print(elemPLT)
        plt.savefig("plots/figure%d.eps" % elemPLT, bbox_inches="tight", format='eps', dpi=500)
        plt.savefig("plots/figure%d.png" % elemPLT, bbox_inches="tight", format='png', dpi=500)