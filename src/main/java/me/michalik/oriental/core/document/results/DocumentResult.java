package me.michalik.oriental.core.document.results;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.function.Function;

public class DocumentResult {

    private final ODocument document;
    private final ODatabaseDocumentTx databaseDocumentTx;

    public DocumentResult(ODocument document, ODatabaseDocumentTx databaseDocumentTx) {
        this.document = document;
        this.databaseDocumentTx = databaseDocumentTx;
    }

    public <R> R map(Function<ODocument, ? extends R> mapper){
        try {
            R result = mapper.apply(this.document);
            this.databaseDocumentTx.commit();
            return result;
        }catch (Exception e){
            this.databaseDocumentTx.rollback();
            throw e;
        }finally {
            this.databaseDocumentTx.close();
        }
    }

    public void delete(){
        try {
            this.document.delete();
            this.databaseDocumentTx.commit();
        }catch (Exception e){
            this.databaseDocumentTx.rollback();
            throw e;
        }finally {
            this.databaseDocumentTx.close();
        }
    }

}