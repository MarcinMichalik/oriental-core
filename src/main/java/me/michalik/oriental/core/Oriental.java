package me.michalik.oriental.core;


import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import me.michalik.oriental.core.document.DocumentOperation;
import me.michalik.oriental.core.graph.notx.GraphNoTxOperation;
import me.michalik.oriental.core.graph.tx.GraphOperation;

public class Oriental {

    private final OrientGraphFactory orientGraphFactory;

    public Oriental(OrientalProperties orientalProperties){
        this.orientGraphFactory = new OrientGraphFactory(orientalProperties.getUrl(), orientalProperties.getUser(), orientalProperties.getPassword());
        orientGraphFactory.setupPool(orientalProperties.getMinPool(), orientalProperties.getMaxPool());
        orientGraphFactory.setAutoStartTx(orientalProperties.getAutoStartTx());
        orientGraphFactory.setRequireTransaction(orientalProperties.getRequireTransaction());
        orientGraphFactory.setKeepInMemoryReferences(orientalProperties.getKeepInMemoryReference());
        orientGraphFactory.setMaxRetries(orientalProperties.getMaxRetries());
    }

    public GraphOperation graphOperation(){
        return new GraphOperation(orientGraphFactory.getTx());
    }

    public GraphNoTxOperation graphNoTxOperation(){
        return new GraphNoTxOperation(orientGraphFactory.getNoTx());
    }

    public DocumentOperation documentOperation(){
        return new DocumentOperation(orientGraphFactory.getDatabase());
    }



//    public <T> T findBydId(ORID orid, MapGraphTx mapGraphTx) throws Exception {
//        OrientGraph orientGraph = this.orientGraphFactory.getTx();
//
//        try{
//            OrientElement orientElement = orientGraph.getElement(orid);
//            return (T) mapGraphTx.mapVertex(orientElement, orientGraph);
//        }catch (Exception e){
//            throw e;
//        }finally {
//            orientGraph.shutdown();
//        }
//    }
//
//    public <T> T findBydId(ORID orid, MapGraphNoTx mapGraphNoTx) throws Exception {
//        OrientGraphNoTx orientGraphNoTx = this.orientGraphFactory.getNoTx();
//
//        try{
//            OrientElement orientElement = orientGraphNoTx.getElement(orid);
//            OrientVertex orientVertex = (OrientVertex) orientElement;
////            return (T) mapGraphNoTx.mapVertex(orientElement, orientGraphNoTx);
//            return this.findBydId(orid, mapGraphNoTx);
//        }catch (Exception e){
//            throw e;
//        }finally {
//            orientGraphNoTx.shutdown();
//        }
//    }
//
//    private <T> T findById(ORID orid, OrientBaseGraph orientBaseGraph, MapGraph mapGraph){
//        try{
//            OrientElement orientElement = orientBaseGraph.getElement(orid);
//            return (T) mapGraph.map(orientElement, orientBaseGraph);
//        }catch (Exception e){
//            throw e;
//        }finally {
//            orientBaseGraph.shutdown();
//        }
//    }

}