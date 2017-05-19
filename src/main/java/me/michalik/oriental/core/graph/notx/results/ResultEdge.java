package me.michalik.oriental.core.graph.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultEdge {

    private final OrientEdge orientEdge;
    private final OrientGraphNoTx orientGraphNoTx;

    public ResultEdge(OrientEdge orientEdge, OrientGraphNoTx orientGraphNoTx) {
        this.orientEdge = orientEdge;
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public <R> R map(Function<OrientEdge, R> mapper){
        try{
            return mapper.apply(this.orientEdge);
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public ResultEdge throwIf(RuntimeException e, Predicate<OrientEdge> predicate) throws Exception {
        if(predicate.test(this.orientEdge)){
            return this;
        }else{
            this.orientGraphNoTx.shutdown();
            throw e;
        }
    }

    public ResultEdge notExistThrow(RuntimeException e){
        if(this.orientEdge==null){
            this.orientGraphNoTx.shutdown();
            throw e;
        }else{
            return this;
        }
    }

    public void remove(){
        try{
            this.orientEdge.remove();
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }
}