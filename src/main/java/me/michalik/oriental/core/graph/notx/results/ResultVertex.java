package me.michalik.oriental.core.graph.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultVertex {

    private final OrientVertex orientVertex;
    private final OrientGraphNoTx orientGraphNoTx;

    public ResultVertex(OrientVertex orientVertex, OrientGraphNoTx orientGraphNoTx) {
        this.orientVertex = orientVertex;
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public <R> R map(Function<OrientVertex, ? extends R> mapper){
        try{
            return mapper.apply(this.orientVertex);
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public ResultVertex throwIf(RuntimeException e, Predicate<OrientVertex> predicate) throws Exception {
        if(predicate.test(this.orientVertex)){
            return this;
        }else{
            this.orientGraphNoTx.shutdown();
            throw e;
        }
    }

    public ResultVertex notExistThrow(RuntimeException e){
        if(this.orientVertex==null){
            this.orientGraphNoTx.shutdown();
            throw e;
        }else{
            return this;
        }
    }

    public void remove(){
        try{
            this.orientVertex.remove();
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }
}