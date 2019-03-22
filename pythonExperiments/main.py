import plots
import tables

FileListInconsistency = 	['StatResults/Yago/InconsistencyStatistics.json', 'StatResults/pizza/InconsistencyStatistics.json']
FileListInconsistencySample = 	['StatResults/Sampled/Yago/InconsistencyStatistics.json', 'StatResults/Sampled/pizza/InconsistencyStatistics.json']
FileListKB =            	['StatResults/Yago/kbStatistics.json', 'StatResults/pizza/kbStatistics.json']
FileListKBSample =            	['StatResults/Sampled/Yago/kbStatistics.json', 'StatResults/Sampled/pizza/kbStatistics.json']

#plots.plotting(FileListInconsistencySample, FileListKBSample, "Sample")
plots.plotting(FileListInconsistency, FileListKB, "")

# tables stats not sampled
tables.tabling(FileListInconsistency, FileListKB)

# tables stats sampled
#tables.tabling(FileListInconsistencySample, FileListKBSample)
