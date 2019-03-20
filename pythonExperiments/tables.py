import json


def tabling(FileListInconsistency, FileListKB):
    print("""\\begin{table}[ht]
\\begin{tabular}{|l|l|l|l|}
\\hline
           & Amount of 'Anti-patterns' & sum of 'Anti-patterns' &  Largest Inconsistency  \\\\ \\hline \\hline """)

    for file in FileListInconsistency:
        dataset = file.split("/")[-2]
        json_file = open(file, 'r')
        data = json.load(json_file)
        IDCoor = []
        SizeCoor = []
        CountCoor = []
        for elem in data:
            CountCoor.append(elem["Count"])
            SizeCoor.append(elem["Size"])
            IDCoor.append(elem["id"])

        AmountOfAntiPatterns = sum(i > 0 for i in CountCoor)

        print("     "+" & ".join([str(dataset),str(AmountOfAntiPatterns),str(sum(CountCoor)),str(max(SizeCoor))])+"\\\\ \\hline")

    print("""\\end{tabular}
\\end{table}""")

    print("")

    print("""\\begin{table}[ht]
\\begin{tabular}{|l|l|l|l|l|}
\\hline
           & Size & Expressivity & Namespaces & Distinct predicates \\\\ \\hline \\hline""")

    for file in FileListKB:
        dataset = file.split("/")[-2]
        json_file = open(file, 'r')
        data = json.load(json_file)
        print("     "+" & ".join([str(dataset),str(data["Size"]),str(data["Expressivity"]),str(len(data["Predicates"])),str(len(data["Namespaces"]))])+"\\\\ \\hline")
    print("""\\end{tabular}
\\end{table}""")
