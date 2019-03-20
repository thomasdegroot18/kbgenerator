import json
import matplotlib.pyplot as plt

FileList = ['StatResults/pizza/InconsistencyStatistics.json', 'StatResults/Sampled/pizza/InconsistencyStatistics.json'];

for file in FileList:
    json_file = open(file, 'r')
    data = json.load(json_file)
    IDCoor = []
    SizeCoor = []
    CountCoor = []
    for elem in data:
        CountCoor.append(elem["Count"])
        SizeCoor.append(elem["Size"])
        IDCoor.append(elem["id"])

    plt.figure(1)
    plt.plot(IDCoor, SizeCoor)
    plt.ylabel('Size of the "Anti-pattern"')
    plt.xlabel('ID of the "Anti-pattern"')

    plt.figure(2)
    plt.plot(IDCoor, CountCoor)
    plt.ylabel('Occurrences of the "Anti-pattern"')
    plt.xlabel('ID of the "Anti-pattern"')


FileList = ['StatResults/pizza/kbStatistics.json', 'StatResults/Sampled/pizza/kbStatistics.json'];

for file in FileList:
    json_file = open(file, 'r')
    data = json.load(json_file)

    XcoorIn = []
    YcoorIn = []
    for key in sorted(data["InDegree"].iterkeys(), key=int):
        XcoorIn.append(int(key))
        YcoorIn.append(int(data["InDegree"][key]))

    plt.figure(3)
    plt.plot(XcoorIn, YcoorIn)
    plt.ylabel('Occurences of the nodes with inlinks')
    plt.xlabel('Amount of inlinks towards the node')


    XcoorOut = []
    YcoorOut = []
    for key in sorted(data["OutDegree"].iterkeys(), key=int):
        XcoorOut.append(int(key))
        YcoorOut.append(int(data["OutDegree"][key]))

    plt.figure(4)
    plt.plot(XcoorOut, YcoorOut)
    plt.ylabel('Occurences of the nodes with equal outlinks')
    plt.xlabel('Amount of outlinks outwards the node')

    XcoorCluster = []
    YcoorCluster = []

    for key in sorted(data["ClusteringCoefficient"].iterkeys(), key=float):
        XcoorCluster.append(float(key))
        YcoorCluster.append(int(data["ClusteringCoefficient"][key]))

    plt.figure(5)
    plt.plot(XcoorCluster, YcoorCluster)
    plt.ylabel('Occurences of the nodes with equal ClusteringCoefficient')
    plt.xlabel('ClusteringCoefficient of the node')



plt.show()
