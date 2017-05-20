package me.michalik.oriental.core.graph.tx;


import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import me.michalik.oriental.core.graph.tx.results.ResultEdge;
import me.michalik.oriental.core.graph.tx.results.ResultEdgeIterable;
import me.michalik.oriental.core.graph.tx.results.ResultVertex;
import me.michalik.oriental.core.graph.tx.results.ResultVertexIterable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GraphOperation {

    private final OrientGraph orientGraph;

    public GraphOperation(OrientGraph orientGraph) {
        this.orientGraph = orientGraph;
    }

    public void db(Consumer<OrientGraph> consumer){
        try{
            this.orientGraph.begin();
            consumer.accept(this.orientGraph);
            this.orientGraph.commit();
        }catch (Exception e){
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public ResultVertex findVertexById(ORID orid){
        return new ResultVertex(this.orientGraph.getVertex(orid), this.orientGraph);
    }

    public ResultEdge findEdgeById(ORID orid){
        return new ResultEdge(this.orientGraph.getEdge(orid), this.orientGraph);
    }

    public ResultVertexIterable queryVertex(String query, Object... objects){
        return this.queryVertex(new OSQLSynchQuery(query), objects);
    }

    public ResultVertexIterable queryVertex(OSQLQuery query, Object... objects) {
        return new ResultVertexIterable(this.orientGraph.command(query).execute(objects), this.orientGraph);
    }

    public ResultEdgeIterable queryEdge(String query, Object... objects){
        return this.queryEdge(new OSQLSynchQuery(query), objects);
    }

    public ResultEdgeIterable queryEdge(OSQLQuery query, Object... objects) {
        return new ResultEdgeIterable(this.orientGraph.command(query).execute(objects), this.orientGraph);
    }

    public ORecordId saveVertex(Object id, Consumer<OrientVertex> consumer){
        return this.saveVertex(this.orientGraph.addVertex(id), consumer);
    }

    public ORecordId saveVertex(String className, String clusterName, Consumer<OrientVertex> consumer){
        return this.saveVertex(this.orientGraph.addVertex(className, clusterName), consumer);
    }

    private ORecordId saveVertex(OrientVertex orientVertex, Consumer<OrientVertex> consumer){
        consumer.accept(orientVertex);
        return this.save(orientVertex);
    }

    public ORecordId saveEdge(Object id, Consumer<OrientEdge> consumer){
        return this.saveEdge(this.orientGraph.addVertex(id), consumer);
    }

    public ORecordId saveEdge(String className, String clusterName, Consumer<OrientEdge> consumer){
        return this.saveEdge(this.orientGraph.addVertex(className, clusterName), consumer);
    }

    private ORecordId saveEdge(OrientEdge orientEdge, Consumer<OrientEdge> consumer){
        consumer.accept(orientEdge);
        return this.save(orientEdge);
    }

    private ORecordId save(OrientElement orientElement){
        try {
            this.orientGraph.commit();
            return new ORecordId(orientElement.getIdentity());
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public void updateVertex(ORID orid, Consumer<OrientVertex> consumer){
        try {
            consumer.accept(this.orientGraph.getVertex(orid));
            this.orientGraph.commit();
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public void updatEdge(ORID orid, Consumer<OrientEdge> consumer){
        try {
            consumer.accept(this.orientGraph.getEdge(orid));
            this.orientGraph.commit();
        }catch (Exception e){
            this.orientGraph.rollback();
            throw e;
        }finally {
            this.orientGraph.shutdown();
        }
    }

    public ResultVertexIterable findVertices(String className){
        return this.findVertices(className, true);
    }

    public ResultVertexIterable findVertices(String className, boolean polymorphic){
        List<OrientVertex> orientVertices = StreamSupport
                .stream(this.orientGraph.getVerticesOfClass(className, polymorphic).spliterator(), false)
                .map(vertex -> (OrientVertex) vertex)
                .collect(Collectors.toList());
        return new ResultVertexIterable(orientVertices, this.orientGraph);
    }

    public ResultVertexIterable findVertices(String label, String[] key, Object[] value){
        List<OrientVertex> orientVertices = StreamSupport
                .stream(this.orientGraph.getVertices(label, key, value).spliterator(), false)
                .map(vertex -> (OrientVertex) vertex)
                .collect(Collectors.toList());
        return new ResultVertexIterable(orientVertices, this.orientGraph);
    }

    public ResultEdgeIterable findEdges(String className){
        return this.findEdges(className, true);
    }

    public ResultEdgeIterable findEdges(String className, boolean polymorphic){
        List<OrientEdge> orientEdges = StreamSupport
                .stream(this.orientGraph.getEdgesOfClass(className, polymorphic).spliterator(), false)
                .map(edge -> (OrientEdge) edge)
                .collect(Collectors.toList());
        return new ResultEdgeIterable(orientEdges, this.orientGraph);
    }

    public ResultEdgeIterable findEdges(String key, Object value){
        List<OrientEdge> orientEdges = StreamSupport
                .stream(this.orientGraph.getEdges(key, value).spliterator(), false)
                .map(edge -> (OrientEdge) edge)
                .collect(Collectors.toList());
        return new ResultEdgeIterable(orientEdges, this.orientGraph);
    }
}