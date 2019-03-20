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

    plt.plot(IDCoor, SizeCoor)
    plt.plot(IDCoor, CountCoor)



FileList = ['StatResults/pizza/kbStatistics.json', 'StatResults/Sampled/pizza/kbStatistics.json'];

for file in FileList:
    json_file = open(file, 'r')
    data = json.load(json_file)

    XcoorIn = []
    YcoorIn = []
    for key in sorted(data["InDegree"].iterkeys(), key=int):
        XcoorIn.append(int(key))
        YcoorIn.append(int(data["InDegree"][key]))

    plt.plot(XcoorIn, YcoorIn)
    XcoorOut = []
    YcoorOut = []
    for key in sorted(data["OutDegree"].iterkeys(), key=int):
        XcoorOut.append(int(key))
        YcoorOut.append(int(data["OutDegree"][key]))

    plt.plot(XcoorOut, YcoorOut)

    XcoorCluster = []
    YcoorCluster = []

    for key in sorted(data["ClusteringCoefficient"].iterkeys(), key=float):
        XcoorCluster.append(float(key))
        YcoorCluster.append(int(data["ClusteringCoefficient"][key]))

    plt.plot(XcoorCluster, YcoorCluster)




plt.show()
