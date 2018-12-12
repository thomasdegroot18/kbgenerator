

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

import java.util.Iterator;
import java.util.Set;

import org.apache.jena.graph.*;
import org.apache.jena.util.CollectionFactory ;

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
    public Graph extractExtend( Node node, Graph graph )
    { return extractIntoExtend( Factory.createGraphMem(), node, graph ); }

    /**
     Answer the graph <code>toUpdate</code> augmented with the sub-graph of
     <code>extractFrom</code> reachable from <code>root</code> bounded
     by this instance's TripleBoundary.
     */
    public Graph extractIntoExtend( Graph toUpdate, Node root, Graph extractFrom )
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
        protected Graph toUpdate;
        protected Graph extractFrom;
        protected Set<Node> active;
        protected TripleBoundary b;

        ExtractionExtend( TripleBoundary b, Graph toUpdate, Graph extractFrom )
        {
            this.toUpdate = toUpdate;
            this.extractFrom = extractFrom;
            this.active = CollectionFactory.createHashedSet();
            this.b = b;
        }

        public int extractIntoExtend( Node root , int counter )
        {
            active.add( root );
            Iterator<Triple> it = extractFrom.find( root, Node.ANY, Node.ANY );

            while (it.hasNext() && counter < 20000)
            {
                Triple t = it.next();
                Node subRoot = t.getObject();
                toUpdate.add( t );
                if (! (active.contains( subRoot ) || b.stopAt( t )  ) ) {  //
                    counter = extractIntoExtend( subRoot, counter);
                }
                counter ++;
            }
            return counter;
        }
    }


}
