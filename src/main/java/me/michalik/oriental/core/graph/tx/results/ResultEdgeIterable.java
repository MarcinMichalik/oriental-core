package me.michalik.oriental.core.graph.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultEdgeIterable {

    private final Iterable<OrientEdge> orientEdges;
    private final OrientGraph orientGraph;

    public ResultEdgeIterable(Iterable<OrientEdge> orientEdges, OrientGraph orientGraph) {
        this.orientEdges = orientEdges;
        this.orientGraph = orientGraph;
    }

    public <R> List<R> map(Function<OrientEdge, R> mapper){
        try{
            List<R> result = StreamSupport.stream(this.orientEdges.spliterator(), false).map(mapper).collect(Collectors.toList());
            this.orientGraph.commit();
            return result;
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }
}
