package me.michalik.oriental.core.graph.notx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultVertexIterable {

    private final Iterable<OrientVertex> orientVertices;
    private final OrientGraphNoTx orientGraphNoTx;

    public ResultVertexIterable(Iterable<OrientVertex> orientVertices, OrientGraphNoTx orientGraphNoTx) {
        this.orientVertices = orientVertices;
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public <R> List<R> map(Function<OrientVertex, R> mapper){
        try{
            return StreamSupport.stream(this.orientVertices.spliterator(), false).map(mapper).collect(Collectors.toList());
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }
}
