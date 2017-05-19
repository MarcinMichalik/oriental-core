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

}