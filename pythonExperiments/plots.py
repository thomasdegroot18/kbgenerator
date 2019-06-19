import json
import matplotlib.pyplot as plt
import numpy as np
from scipy.interpolate import spline


Colors = ['#3cb44b','#e6194b', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6','#ffe119', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000']

def plotting(FileListInconsistency, FileListKB, Sample):
    x = 0
    if len(Sample) > 1:
        x = 10
    ColorScheme = {'dbpedia2016-04en': '#e6194b', 'yago2s': '#3cb44b', 'dblp-2012-11-28b': '#f58231'} 
    counter = 0
    labels1 = []
    labels2 = []
    width = (1-0.1)/len(FileListInconsistency)
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
        plt.plot(Size, SizeCount, color=ColorScheme[datasetName])
        plt.ylabel('Count of different "Anti-pattern" types with the same size')
        plt.xlabel('Size of the "Anti-pattern"')

        plt.figure(2+x)

        SizeSum[:] = [x / float(sum(SizeSum)+0.0000001) for x in SizeSum]
        plt.title('Plot showing the normalised Distribution of Occurences')
        Size = np.arange(0,25)
        try:
            plt.bar(Size+width*counter, SizeSum, width, color=ColorScheme[datasetName])
        except:
            pass
        counter += 1
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('Size of the "Anti-pattern"')

        CountCoor.sort(reverse = True)
        CountCoor[:] = [x / float(sum(CountCoor)+0.0000001) for x in CountCoor]
        plt.figure(3+x)
        plt.title('Plot showing the normalised Distribution of Occurences')
        plt.plot( CountCoor, color=ColorScheme[datasetName])
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


        plt.figure(current)
        # Remove the plot frame lines. They are unnecessary chartjunk.
        ax = plt.subplot(111)
        ax.spines["top"].set_visible(False)
        ax.spines["bottom"].set_visible(False)
        ax.spines["right"].set_visible(False)
        ax.spines["left"].set_visible(False)
        if current == 1:
            pass
        elif current == 2:
            ax.set_yscale("log")
        else:
            ax.set_yscale("log")
            ax.set_xscale("log")

        if(current > 3):
            ax.legend(labels2)
        else:
            ax.legend(labels1)

        # Ensure that the axis ticks only show up on the bottom and left of the plot.
        # Ticks on the right and top of the plot are generally unnecessary chartjunk.
        ax.get_xaxis().tick_bottom()
        ax.get_yaxis().tick_left()
        elemPLT = int(current) -10
        plt.savefig("plots/"+Sample+'figure%d.png' % elemPLT, bbox_inches="tight")
