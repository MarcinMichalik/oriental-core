package me.michalik.oriental.core.document;


import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

import java.util.function.Consumer;

public class DocumentOperation {

    private final ODatabaseDocumentTx oDatabaseDocumentTx;

    public DocumentOperation(ODatabaseDocumentTx oDatabaseDocumentTx) {
        this.oDatabaseDocumentTx = oDatabaseDocumentTx;
    }

    public void db(Consumer<ODatabaseDocumentTx> consumer){
        consumer.accept(this.oDatabaseDocumentTx);
    }
}