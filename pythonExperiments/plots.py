import json
import matplotlib.pyplot as plt

def plotting(FileListInconsistency, FileListKB, Sample):

    for file in FileListInconsistency:
        json_file = open(file, 'r')
        data = json.load(json_file)
        IDCoor = []
        SizeCoor = []
        CountCoor = []
        for elem in data:
            CountCoor.append(elem["Count"])
            SizeCoor.append(elem["Size"])
            IDCoor.append(elem["id"])

        SizeCoor.sort( reverse = True)
        IDCoor.sort()
        CountCoor.sort(reverse = True)
        plt.figure(1)
        plt.title('Plot showing the Distribution of Size')
        plt.plot(IDCoor, SizeCoor)
        plt.ylabel('Size of the "Anti-pattern"')
        plt.xlabel('ID of the "Anti-pattern"')

        plt.figure(2)
        plt.title('Plot showing the Distribution of Occurences')
        plt.plot(IDCoor, CountCoor)
        plt.ylabel('Occurrences of the "Anti-pattern"')
        plt.xlabel('ID of the "Anti-pattern"')


    for file in FileListKB:
        json_file = open(file, 'r')
        data = json.load(json_file)

        XcoorIn = []
        YcoorIn = []
        for key in sorted(data["InDegree"].iterkeys(), key=int):
            XcoorIn.append(int(key))
            YcoorIn.append(int(data["InDegree"][key]))

        plt.figure(3)
        plt.title('Plot showing the Distribution of the nodes with inlinks')
        plt.plot(XcoorIn, YcoorIn)
        plt.ylabel('Occurences of the nodes with inlinks')
        plt.xlabel('Amount of inlinks towards the node')


        XcoorOut = []
        YcoorOut = []
        for key in sorted(data["OutDegree"].iterkeys(), key=int):
            XcoorOut.append(int(key))
            YcoorOut.append(int(data["OutDegree"][key]))

        plt.figure(4)
        plt.title('Plot showing the Distribution of the nodes with outlinks')
        plt.plot(XcoorOut, YcoorOut)
        plt.ylabel('Occurences of the nodes with equal outlinks')
        plt.xlabel('Amount of outlinks outwards the node')

        XcoorCluster = []
        YcoorCluster = []

        for key in sorted(data["ClusteringCoefficient"].iterkeys(), key=float):
            XcoorCluster.append(float(key))
            YcoorCluster.append(int(data["ClusteringCoefficient"][key]))

        plt.figure(5)
        plt.title('Plot showing the Distribution of the nodes with equal ClusteringCoefficient')
        plt.plot(XcoorCluster, YcoorCluster)
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

        # Ensure that the axis ticks only show up on the bottom and left of the plot.
        # Ticks on the right and top of the plot are generally unnecessary chartjunk.
        ax.get_xaxis().tick_bottom()
        ax.get_yaxis().tick_left()
        plt.savefig("plots/"+Sample+'figure%d.eps' % current, bbox_inches="tight")
