

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.thesis.kbgenerator;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.graph.*;
import org.apache.jena.util.CollectionFactory ;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

/**
 GraphExtract offers a very simple recursive extraction of a subgraph with a
 specified root in some supergraph. The recursion is terminated by triples
 that satisfy some supplied boundary condition.
 */
public class GraphExtractExtended extends org.apache.jena.graph.GraphExtract
{
    protected final TripleBoundary b;


    public GraphExtractExtended( TripleBoundary b )
    {
        super(b);
        this.b = b;
    }

    /**
     Answer a new graph which is the reachable subgraph from <code>node</code>
     in <code>graph</code> with the terminating condition given by the
     TripleBoundary passed to the constructor.
     */
    public Set<String> extractExtend( String node, HDT graph ) throws Exception
    { return extractIntoExtend(new HashSet<>() , node, graph ); }

    /**
     Answer the graph <code>toUpdate</code> augmented with the sub-graph of
     <code>extractFrom</code> reachable from <code>root</code> bounded
     by this instance's TripleBoundary.
     */
    public Set<String> extractIntoExtend( Set<String> toUpdate, String root, HDT extractFrom ) throws Exception
    { new ExtractionExtend( b, toUpdate, extractFrom ).extractIntoExtend( root , 0);
        return toUpdate; }

    /**
     This is the class that does all the work, in the established context of the
     source and destination graphs, the TripleBoundary that determines the
     limits of the extraction, and a local set <code>active</code> of nodes
     already seen and hence not to be re-processed.
     */
    protected static class ExtractionExtend
    {
        protected Set<String> toUpdate;
        protected HDT extractFrom;
        protected Set<CharSequence> active;
        protected TripleBoundary b;

        ExtractionExtend( TripleBoundary b, Set<String> toUpdate, HDT extractFrom )
        {
            this.toUpdate = toUpdate;
            this.extractFrom = extractFrom;
            this.active = CollectionFactory.createHashedSet();
            this.b = b;

        }

        public int extractIntoExtend( CharSequence root , int counter ) throws Exception
        {
            active.add( root );

            IteratorTripleString it = extractFrom.search(root, "", "");
            while (it.hasNext() && counter < 5000)
            {
                TripleString t = it.next();
                String subRoot = t.getObject().toString();
                toUpdate.add( t.asNtriple().toString() );
                if (! (active.contains( subRoot )  ) ) {  //
                    counter = extractIntoExtend( subRoot, counter);
                }
                counter ++;
            }
            return counter;
        }
    }


}
