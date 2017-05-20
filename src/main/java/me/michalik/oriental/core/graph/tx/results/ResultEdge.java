package me.michalik.oriental.core.graph.tx.results;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultEdge {

    private final OrientEdge orientEdge;
    private final OrientGraph orientGraph;

    public ResultEdge(OrientEdge orientEdge, OrientGraph orientGraph) {
        this.orientEdge = orientEdge;
        this.orientGraph = orientGraph;
    }

    public <R> R map(Function<OrientEdge, R> mapper){
        try{
            R result = mapper.apply(this.orientEdge);
            this.orientGraph.commit();
            return result;
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public ResultEdge throwIf(RuntimeException e, Predicate<OrientEdge> predicate) throws Exception {
        if(predicate.test(this.orientEdge)){
            return this;
        }else{
            this.orientGraph.rollback();
            this.orientGraph.shutdown();
            throw e;
        }
    }

    public ResultEdge notExistThrow(RuntimeException e){
        if(this.orientEdge==null){
            this.orientGraph.rollback();
            this.orientGraph.shutdown();
            throw e;
        }else{
            return this;
        }
    }

    public void remove(){
        try{
            this.orientEdge.remove();
            this.orientGraph.commit();
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }
}