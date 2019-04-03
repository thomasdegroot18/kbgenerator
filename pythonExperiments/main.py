import plots
import tables

FileListInconsistency = 	   ['StatResults/DBpedia/InconsistencyStatistics.json','StatResults/Yago/InconsistencyStatistics.json','StatResults/LOD/InconsistencyStatistics.json']
FileListInconsistencySample =  ['StatResults/Sampled/DBpedia/InconsistencyStatistics.json', 'StatResults/Sampled/Yago/InconsistencyStatistics.json']
FileListKB =            	   ['StatResults/DBpedia/kbStatistics.json','StatResults/Yago/kbStatistics.json','StatResults/wordnet/kbStatistics.json']
FileListKBSample =             ['StatResults/Sampled/DBpedia/kbStatistics.json','StatResults/Sampled/Yago/kbStatistics.json']

#plots.plotting(FileListInconsistencySample, FileListKBSample, "Sample")
plots.plotting(FileListInconsistency, FileListKB, "")

# tables stats not sampled
tables.tabling(FileListInconsistency, FileListKB)

# tables stats sampled
#tables.tabling(FileListInconsistencySample, FileListKBSample)
