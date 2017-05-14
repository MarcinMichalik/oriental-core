package me.michalik.oriental.core.graph.notx.results;


import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

public class ResultElement<T extends OrientElement> {

    private final T orientElement;
    private final OrientGraphNoTx orientGraphNoTx;

    public ResultElement(T orientElement, OrientGraphNoTx orientGraphNoTx) {
        this.orientElement = orientElement;
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public void test(){

    }
}
