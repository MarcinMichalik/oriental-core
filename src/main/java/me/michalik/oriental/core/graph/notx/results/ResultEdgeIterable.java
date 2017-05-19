package me.michalik.oriental.core.graph.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultEdgeIterable {

    private final Iterable<OrientEdge> orientEdges;
    private final OrientGraphNoTx orientGraphNoTx;


    public ResultEdgeIterable(Iterable<OrientEdge> orientEdges, OrientGraphNoTx orientGraphNoTx) {
        this.orientEdges = orientEdges;
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public <R> List<R> map(Function<OrientEdge, R> mapper){
        try{
            return StreamSupport.stream(this.orientEdges.spliterator(), false).map(mapper).collect(Collectors.toList());
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }
}