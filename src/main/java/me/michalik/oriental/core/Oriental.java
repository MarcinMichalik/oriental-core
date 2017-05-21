package me.michalik.oriental.core;


import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
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
        return new GraphOperation(this.getOrientGraph());
    }

    public GraphNoTxOperation graphNoTxOperation(){
        return new GraphNoTxOperation(this.getOrientGraphNoTx());
    }

    public DocumentOperation documentOperation(){
        return new DocumentOperation(this.getDatabaseDocumentTx());
    }

    public OrientGraph getOrientGraph(){
        return this.orientGraphFactory.getTx();
    }

    public OrientGraphNoTx getOrientGraphNoTx(){
        return this.orientGraphFactory.getNoTx();
    }

    public ODatabaseDocumentTx getDatabaseDocumentTx(){
        return this.orientGraphFactory.getDatabase();
    }
}