package me.michalik.oriental.core.graph.notx;


import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import me.michalik.oriental.core.graph.notx.results.ResultEdge;
import me.michalik.oriental.core.graph.notx.results.ResultEdgeIterable;
import me.michalik.oriental.core.graph.notx.results.ResultVertex;
import me.michalik.oriental.core.graph.notx.results.ResultVertexIterable;

import java.util.function.Consumer;

public class GraphNoTxOperation {

    private final OrientGraphNoTx orientGraphNoTx;

    public GraphNoTxOperation(OrientGraphNoTx orientGraphNoTx) {
        this.orientGraphNoTx = orientGraphNoTx;
    }

    public void db(Consumer<OrientGraphNoTx> graphNoTxConsumer){
        try{
            graphNoTxConsumer.accept(this.orientGraphNoTx);
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public ResultVertex findVertexById(ORID orid){
        return new ResultVertex(this.getVertexById(orid), this.orientGraphNoTx);
    }

    public ResultEdge findEdgeById(ORID orid){
        return new ResultEdge(this.getEdgeById(orid), this.orientGraphNoTx);
    }

    public ResultVertexIterable queryVertex(String query, Object... objects){
        return this.queryVertex(new OSQLSynchQuery(query), objects);
    }

    public ResultVertexIterable queryVertex(OSQLQuery query, Object... objects) {
        return new ResultVertexIterable(this.orientGraphNoTx.command(query).execute(objects), this.orientGraphNoTx);
    }

    public ResultEdgeIterable queryEdge(String query, Object... objects){
        return this.queryEdge(new OSQLSynchQuery(query), objects);
    }

    public ResultEdgeIterable queryEdge(OSQLQuery query, Object... objects) {
        return new ResultEdgeIterable(this.orientGraphNoTx.command(query).execute(objects), this.orientGraphNoTx);
    }

    public ORecordId saveVertex(Object id, Consumer<OrientVertex> consumer){
        return this.saveVertex(this.orientGraphNoTx.addVertex(id), consumer);
    }

    public ORecordId saveVertex(String className, String clusterName, Consumer<OrientVertex> consumer){
        return this.saveVertex(this.orientGraphNoTx.addVertex(className, clusterName), consumer);
    }

    private ORecordId saveVertex(OrientVertex orientVertex, Consumer<OrientVertex> consumer){
        consumer.accept(orientVertex);
        return this.save(orientVertex);
    }

    public ORecordId saveEdge(Object id, Consumer<OrientEdge> consumer){
        return this.saveEdge(this.orientGraphNoTx.addVertex(id), consumer);
    }

    public ORecordId saveEdge(String className, String clusterName, Consumer<OrientEdge> consumer){
        return this.saveEdge(this.orientGraphNoTx.addVertex(className, clusterName), consumer);
    }

    private ORecordId saveEdge(OrientEdge orientEdge, Consumer<OrientEdge> consumer){
        consumer.accept(orientEdge);
        return this.save(orientEdge);
    }

    private ORecordId save(OrientElement orientElement){
        try {
            return new ORecordId(orientElement.getIdentity());
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public void updateVertex(ORID orid, Consumer<OrientVertex> consumer){
        try {
            consumer.accept(this.getVertexById(orid));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    public void updatEdge(ORID orid, Consumer<OrientEdge> consumer){
        try {
            consumer.accept(this.getEdgeById(orid));
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraphNoTx.shutdown();
        }
    }

    private OrientVertex getVertexById(ORID orid){
        return this.orientGraphNoTx.getVertex(orid);
    }

    private OrientEdge getEdgeById(ORID orid){
        return this.orientGraphNoTx.getEdge(orid);
    }



}