package me.michalik.oriental.core.graph.notx;


import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import me.michalik.oriental.core.graph.notx.results.ResultEdge;
import me.michalik.oriental.core.graph.notx.results.ResultVertex;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Consumer;
import java.util.function.Function;

public class GraphNoTxOperation {

    private final OrientGraphNoTx orientGraphNoTx;

    public GraphNoTxOperation(OrientGraphNoTx orientGraphNoTx) {
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public <R> R findVertexById(ORID orid, Function<OrientVertex, ? extends R> mapper) {
        try {
            return mapper.apply(this.orientGraphNoTx.getVertex(orid));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public ResultVertex findVertexById(ORID orid){
        return new ResultVertex(this.orientGraphNoTx.getVertex(orid), this.orientGraphNoTx);
    }

    public <R> R findEdgeById(ORID orid, Function<OrientEdge, ? extends R> mapper) {
        try {
            return mapper.apply(this.orientGraphNoTx.getEdge(orid));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public ResultEdge findEdgeById(ORID orid){
        return new ResultEdge(this.orientGraphNoTx.getEdge(orid), this.orientGraphNoTx);
    }

    public <R> R query(String query, Function<Iterable<OrientElement>, ? extends R> mapper, Object... objects) {
        return this.query(new OSQLSynchQuery<R>(query), mapper, objects);
    }

    public <R> R query(OSQLQuery<R> query, Function<Iterable<OrientElement>, ? extends R> mapper, Object... objects){
        try{
//            TODO - tutaj chyba trzeba zrobić loopa na iterable i mapper wywołać na pojedynczym
            return mapper.apply(this.orientGraphNoTx.command(query).execute(objects));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public <R> R queryVertex(String query, Function<Iterable<OrientVertex>, ? extends R> mapper, Object... objects) {
        return this.queryVertex(new OSQLSynchQuery<R>(query), mapper, objects);
    }

    public <R> R queryVertex(OSQLQuery<R> query, Function<Iterable<OrientVertex>, ? extends R> mapper, Object... objects){
        try{
            return mapper.apply(this.orientGraphNoTx.command(query).execute(objects));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public <R> R queryEdge(String query, Function<Iterable<OrientEdge>, ? extends R> mapper, Object... objects) {
        return this.queryEdge(new OSQLSynchQuery<R>(query), mapper, objects);
    }

    public <R> R queryEdge(OSQLQuery<R> query, Function<Iterable<OrientEdge>, ? extends R> mapper, Object... objects){
        try{
            return mapper.apply(this.orientGraphNoTx.command(query).execute(objects));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public <R> R update(ORID orid, Consumer<OrientElement> update, Function<OrientElement, ? extends R> mapper) {
        try {
            OrientElement orientElement = this.orientGraphNoTx.getElement(orid);
            update.accept(orientElement);
            orientElement.save();
            return mapper.apply(orientElement);
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }




    public <T> T save() {
        OrientVertex orientVertex = this.orientGraphNoTx.addVertex(null);
        orientVertex.save();
        throw new NotImplementedException();
    }


    public void delete(){
        throw new NotImplementedException();
    }

    public ResultVertex testById(ORID orid){
        try {

            return new ResultVertex(this.orientGraphNoTx.getVertex(orid), orientGraphNoTx);
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

//    public <R> R test(ORID orid, Function<OrientVertex, ? extends R> mapper) {
//        /*
//        * Package java.util.function
//        *
//        * Predicate<? super T> predicate
//        * Comparator<? super T> comparator
//        * Collector<? super T, A, R> collector
//        *
//        * */
//        OrientVertex orientVertex = this.orientGraphNoTx.getVertex(orid);
//        return mapper.apply(orientVertex);
//    }
//
//    public <R> R test2(ORID orid, Function<ResultVertex, ? extends R> mapper) {
//        OrientVertex orientVertex = this.orientGraphNoTx.getVertex(orid);
//        return mapper.apply(new ResultVertex(orientVertex, this.orientGraphNoTx));
//    }
}