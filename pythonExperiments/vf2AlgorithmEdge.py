
# Recursive VF2 algorithm.

def recursiveVF2(input1, input2, size1, size2):
    if len(size1) == len(Graph1):
        return true

    for elem in Graph1Out[input1]:
        for elem in Graph2Out[input2]:



if __name__ == '__main__':
    # 1 and 3 should be isomorph 2 not.
    Graph1 = ["1","2","3","4","5","6"]
    Graph2 = ["1","2","3","4","5","6"]
    Graph3 = ["1","2","3","4","5","6"]
    Graph1Out = {"1":["2","6"], "2":["1","3","6"], "3":["2","4"], "4":["3","5"],"5":["4","6"], "6": ["1","2","6"]}
    Graph2Out = {"1":["2","6"], "2":["1","3"], "3":["2","4","6"], "4":["3","5"],"5":["4","6"], "6": ["1","3","6"]}
    Graph3Out = {"1":["2","6"], "2":["1","3"], "3":["2","4","5"], "4":["3","5"],"5":["3","4","6"], "6": ["1","5"]}


    recursiveVF2("1", "2")
