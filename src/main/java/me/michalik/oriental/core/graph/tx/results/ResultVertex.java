package me.michalik.oriental.core.graph.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultVertex {

    private final OrientVertex orientVertex;
    private final OrientGraph orientGraph;

    public ResultVertex(OrientVertex orientVertex, OrientGraph orientGraph) {
        this.orientVertex = orientVertex;
        this.orientGraph = orientGraph;
    }

    public <R> R map(Function<OrientVertex, ? extends R> mapper){
        try{
            R result = mapper.apply(this.orientVertex);
            this.orientGraph.commit();
            return result;
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public ResultVertex throwIf(RuntimeException e, Predicate<OrientVertex> predicate) throws Exception {
        if(predicate.test(this.orientVertex)){
            return this;
        }else{
            this.orientGraph.rollback();
            this.orientGraph.shutdown();
            throw e;
        }
    }

    public ResultVertex notExistThrow(RuntimeException e){
        if(this.orientVertex==null){
            this.orientGraph.rollback();
            this.orientGraph.shutdown();
            throw e;
        }else{
            return this;
        }
    }

    public void remove(){
        try{
            this.orientVertex.remove();
            this.orientGraph.commit();
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }
}
