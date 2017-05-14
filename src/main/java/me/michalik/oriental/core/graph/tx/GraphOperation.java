package me.michalik.oriental.core.graph.tx;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GraphOperation {

    private final OrientGraph orientGraph;

    public GraphOperation(OrientGraph orientGraph) {
        this.orientGraph = orientGraph;
    }
}