package com.thesis.SPARQLengine;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

public class SPARQLExecutioner {

    public static ResultSet SPARQLQuery(Model model, String SPARQLQuery) {
        // Set the result set to null.
        ResultSet results = null;

        // Try to execute the query. Could throw errors when creating the factory.
        try {

            // Use Jena ARQ to execute the query. Firstly creating the query.
            Query query = QueryFactory.create(SPARQLQuery);
            // Executing the query.
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            long TimeOut = 10000; //fetch starting time & to make sure it does not get stuck after 10 sec.
            //Get the selection of the executed query.
            qe.setTimeout(TimeOut);

            results = qe.execSelect();
        } catch (Exception e) {
            // Catch Exception and print the message.
            e.printStackTrace();
        }
        return results;
    }

    public static ResultSet SPARQLQuery(Model model, String SPARQLQuery, long TimeOut) {
        // Set the result set to null.
        ResultSet results = null;

        // Try to execute the query. Could throw errors when creating the factory.
        try {

            // Use Jena ARQ to execute the query. Firstly creating the query.
            Query query = QueryFactory.create(SPARQLQuery);
            // Executing the query.
            QueryExecution qe = QueryExecutionFactory.create(query, model);

            //Get the selection of the executed query.
            qe.setTimeout(TimeOut);

            results = qe.execSelect();
        } catch (Exception e) {
            // Catch Exception and print the message.
            e.printStackTrace();
        }
        return results;
    }

    public static int CounterResultPrinter(Model model, String query){
        //  Run query  and retrieve the results as ResultSet.
        ResultSet results = SPARQLQuery(model, query);
        // Print for every result the result line.
        int counter = 0;
        // This way the query will break after a 60 second execution time.
        try {
            while (results.hasNext()) {
                counter++;
                results.next();
            }
        } catch (Exception e){
            System.out.println("Time out Exception");
        }

        return counter;
    }

    public static int ResultPrinter(Model model, String query){
        //  Run query  and retrieve the results as ResultSet.
        ResultSet results = SPARQLQuery(model, query);
        // Print for every result the result line.
        int counter = 0;
        // This way the query will break after a 60 second execution time.
        try {
            while (results.hasNext()) {
                counter++;
                System.out.println(results.next().toString());
            }
        } catch (Exception e){
            System.out.println("Time out Exception");
        }

        return counter;
    }

    public static int ExistsPrinter(Model model, String query){
        //  Run query  and retrieve the results as ResultSet.
        ResultSet results = SPARQLQuery(model, query);
        // Print for every result the result line.
        int counter = 0;
        // This way the query will break after a 60 second execution time.
        try {
            while (results.hasNext()) {
                return 1;
            }
        } catch (Exception e){
            System.out.println("Time out Exception");
        }

        return counter;
    }
}
