

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


package com.thesis.kbInconsistencyLocator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.jena.graph.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.StmtIteratorImpl;
import org.apache.jena.util.CollectionFactory ;

import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

/**
 GraphExtract offers a very simple recursive extraction of a subgraph with a
 specified root in some superGraph. The recursion is terminated by triples
 that satisfy some supplied boundary condition.

 EXTENDED by:
 Thomas de Groot
 */
public class GraphExtractExtended extends org.apache.jena.graph.GraphExtract
{

    public GraphExtractExtended( TripleBoundary b )
    {
        super(b);
    }

    /**
     Answer a new graph which is the reachable subgraph from <code>node</code>
     in <code>graph</code> with the terminating condition given by the
     TripleBoundary passed to the constructor.
     */
    @SuppressWarnings("unused")
    Set<String> extractExtend( String node, HDT graph ) throws Exception
    { return extractIntoExtend(new HashSet<>() , node, graph ); }

    Set<String> extractExtendBothClean( String node, HDT graph ) throws Exception
    { return extractIntoExtendClean(new HashSet<>() , node, graph ); }

    Set<String> extractExtend( String node, HDT graph, int MaxValue ) throws Exception
    { return extractIntoExtend(new HashSet<>() , node, graph, MaxValue ); }

    public Set<Triple> extractExtendBoth( RDFNode node, Model model, int MaxValue, Model modelRemovedTriples  )
    { return extractIntoExtendBoth(new HashSet<>() , node, model, MaxValue, modelRemovedTriples ); }


    /**
     Answer the graph <code>toUpdate</code> augmented with the sub-graph of
     <code>extractFrom</code> reachable from <code>root</code> bounded
     by this instance's TripleBoundary.
     */
    private Set<String> extractIntoExtend( Set<String> toUpdate, String root, HDT extractFrom ) throws Exception
    { new ExtractionExtend( toUpdate, extractFrom ).extractIntoExtend( root , 0);
        return toUpdate; }

    private Set<String> extractIntoExtendClean( Set<String> toUpdate, String root, HDT extractFrom ) throws Exception
    { new ExtractionExtend( toUpdate, extractFrom ).extractIntoExtendCleanBoth( root , 0);
        return toUpdate; }

    private Set<String> extractIntoExtend( Set<String> toUpdate, String root, HDT extractFrom, int MaxValue ) throws Exception
    { new ExtractionExtend( toUpdate, extractFrom, MaxValue ).extractIntoExtend( root , 0);
        return toUpdate; }

    private Set<Triple> extractIntoExtendBoth(Set<Triple> toUpdate, RDFNode root, Model extractFrom, int MaxValue, Model modelRemovedTriples )
    { new ExtractionExtend( toUpdate, MaxValue ).extractIntoExtendBothWays( root , 0, extractFrom, modelRemovedTriples);
        return toUpdate; }

    /**
     This is the class that does all the work, in the established context of the
     source and destination graphs, the TripleBoundary that determines the
     limits of the extraction, and a local set <code>active</code> of nodes
     already seen and hence not to be re-processed.
     */
    private static class ExtractionExtend
    {
        private Set<String> toUpdate;
        private Set<Triple> toUpdateTriple;
        private HDT extractFrom;
        private Set<CharSequence> active;
        private int maxValue;
        private static Random rand = new Random();

        private ExtractionExtend( Set<String> toUpdate, HDT extractFrom)
        {
            this.toUpdate = toUpdate;
            this.extractFrom = extractFrom;
            this.active = CollectionFactory.createHashedSet();
            this.maxValue = 5000;
        }

        private ExtractionExtend( Set<String> toUpdate, HDT extractFrom, int maxValue)
        {
            this.toUpdate = toUpdate;
            this.extractFrom = extractFrom;
            this.active = CollectionFactory.createHashedSet();
            this.maxValue = maxValue;
        }

        private ExtractionExtend(Set<Triple> toUpdateTriple, int maxValue)
        {
            this.toUpdateTriple = toUpdateTriple;
            this.active = CollectionFactory.createHashedSet();
            this.maxValue = maxValue;
        }

        private int extractIntoExtend( CharSequence root , int counter ) throws Exception
        {
            active.add( root );

            IteratorTripleString it = extractFrom.search(root, "", "");
            while (it.hasNext() && counter < maxValue)
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

        private int extractIntoExtendCleanBoth( CharSequence root , int counter ) throws Exception
        {
            active.add( root );

            IteratorTripleString itForward = extractFrom.search(root, "", "");
            IteratorTripleString itBackward = extractFrom.search("", "", root);
            while ((itBackward.hasNext() || itForward.hasNext()) && counter < maxValue)
            {
                TripleString t;
                String subRoot;
                if( !itBackward.hasNext() ){
                    t = itForward.next();
                    subRoot = t.getObject().toString();
                } else if(!itForward.hasNext() ){
                    t = itBackward.next();
                    subRoot = t.getSubject().toString();
                } else if(rand.nextDouble() < 0.5001){
                    t = itForward.next();
                    subRoot = t.getObject().toString();
                } else{
                    t = itBackward.next();
                    subRoot = t.getSubject().toString();
                }
                toUpdate.add( t.asNtriple().toString() );
                if (! (active.contains( subRoot )  ) ) {  //
                    counter = extractIntoExtendCleanBoth( subRoot, counter);
                }
                counter ++;
            }
            return counter;
        }


        private int extractIntoExtendBothWays( RDFNode root , int counter , Model extractFromModel, Model modelRemovedTriples)
        {
            active.add( root.toString() );
            StmtIterator itForward = null;

            if(root.isResource()){
                itForward = extractFromModel.listStatements(root.asResource(), null, (RDFNode)null);
            }
            StmtIterator itBackward = extractFromModel.listStatements(null, null, root);


            while (((itBackward.hasNext() || (itForward != null && itForward.hasNext())) && counter < maxValue))
            {
                Statement t;
                RDFNode subRoot;
                if( !itBackward.hasNext() ){
                    t = itForward.next();
                    subRoot = t.getObject();
                } else if(itForward != null && !itForward.hasNext() ){
                    t = itBackward.next();
                    subRoot = t.getSubject();
                } else if(rand.nextDouble() < 0.5001 && itForward != null){
                    t = itForward.next();
                    subRoot = t.getObject();
                } else{
                    t = itBackward.next();
                    subRoot = t.getSubject();
                }

                toUpdateTriple.add( t.asTriple() );
                counter ++;
                if ( (!active.contains( subRoot.toString() ) ) && (!modelRemovedTriples.contains(t) )) {  //
                    counter = extractIntoExtendBothWays( subRoot, counter, extractFromModel, modelRemovedTriples);
                }
            }



            return counter;
        }
    }


}
