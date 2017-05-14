package me.michalik.oriental.core.document;


import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

public class DocumentOperation {

    private final ODatabaseDocumentTx oDatabaseDocumentTx;

    public DocumentOperation(ODatabaseDocumentTx oDatabaseDocumentTx) {
        this.oDatabaseDocumentTx = oDatabaseDocumentTx;
    }
}