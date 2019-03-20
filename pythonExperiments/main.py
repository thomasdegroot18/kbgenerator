import plots
import tables

FileListInconsistency = ['StatResults/pizza/InconsistencyStatistics.json', 'StatResults/Sampled/pizza/InconsistencyStatistics.json']
FileListKB =            ['StatResults/pizza/kbStatistics.json', 'StatResults/Sampled/pizza/kbStatistics.json']


plots.plotting(FileListInconsistency, FileListKB, "Sample")
plots.plotting(FileListInconsistency, FileListKB, "")

# tables stats not sampled
tables.tabling(FileListInconsistency, FileListKB)

# tables stats sampled
tables.tabling(FileListInconsistency, FileListKB)
