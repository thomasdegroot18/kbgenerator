import plots
import tables

SString = "StatResults/Sampled/"
sString = "StatResults/"
incon = "/InconsistencyStatistics.json"
kbStaaa = "/kbStatistics.json"

FileListInconsistency = 	   [sString+'DBpedia'+incon,
								sString+'Yago'+incon,
								sString+'wordnet'+incon,
								sString+'DBLP'+incon,
								sString+'swdf'+incon,
								sString+'LOD'+incon]
# FileListInconsistencySample =  [SString+'dbpedia2016-04en'+incon,
# 								SString+'yago2s'+incon,
# 								SString+'wordnet31'+incon,
# 								SString+'dblp-2012-11-28b'+incon,
# 								SString+'swdf'+incon]
FileListKB =            	   [sString+'DBpedia'+kbStaaa,
								sString+'Yago'+kbStaaa,
								sString+'wordnet'+kbStaaa,
								sString+'DBLP'+kbStaaa,
								sString+'swdf'+kbStaaa]
# FileListKBSample =  			[SString+'dbpedia2016-04en'+kbStaaa,
# 								SString+'yago2s'+kbStaaa,
# 								SString+'wordnet31'+kbStaaa,
# 								SString+'dblp-2012-11-28b'+kbStaaa,
# 								SString+'swdf'+kbStaaa]


# FileListInconsistency = 	   [sString+'DBpedia'+incon,
# 								sString+'Yago'+incon]
# FileListInconsistencySample =  [SString+'DBpedia'+incon,
# 								SString+'Yago'+incon]
# FileListKB =            	   [sString+'DBpedia'+kbStaaa,
# 								sString+'Yago'+kbStaaa,
# 								sString+'DBLP'+kbStaaa]
# FileListKBSample =  			[SString+'DBpedia'+kbStaaa,
# 								SString+'Yago'+kbStaaa,
# 								SString+'DBLP'+kbStaaa]
# FileListInconsistency = 	   [sString+'DBpedia'+incon,
# 								sString+'Yago'+incon,
# 								sString+'dblp-2012-11-28b'+incon]
# FileListInconsistencySample =  [SString+'dbpedia2016-04en'+incon,
# 								SString+'yago2s'+incon,
# 								SString+'dblp-2012-11-28b'+incon]
# FileListKB =            	   [sString+'DBpedia'+kbStaaa,
# 								sString+'Yago'+kbStaaa,
# 								sString+'dblp-2012-11-28b'+kbStaaa]
# FileListKBSample =  			[SString+'dbpedia2016-04en'+kbStaaa,
# 								SString+'yago2s'+kbStaaa,
# 								SString+'dblp-2012-11-28b'+kbStaaa]
plots.plotting(FileListInconsistency, FileListKB, "")
# plots.plotting(FileListInconsistencySample, FileListKBSample, "Sample")

# # # tables stats not sampled
# tables.tabling(FileListInconsistency, FileListKB)

# # # tables stats sampled
# tables.tabling(FileListInconsistencySample, FileListKBSample)
