package me.michalik.oriental.core.graph.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultVertexIterable {

    private final Iterable<OrientVertex> orientVertices;
    private final OrientGraph orientGraph;

    public ResultVertexIterable(Iterable<OrientVertex> orientVertices, OrientGraph orientGraph) {
        this.orientVertices = orientVertices;
        this.orientGraph = orientGraph;
    }

    public <R> List<R> map(Function<OrientVertex, R> mapper){
        try{
            List<R> result = StreamSupport.stream(this.orientVertices.spliterator(), false).map(mapper).collect(Collectors.toList());
            this.orientGraph.commit();
            return result;
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }
}