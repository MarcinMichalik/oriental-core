package me.michalik.oriental.core.document.results;


import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DocumentIterableResult {

    private final Iterator<ODocument> documents;
    private final ODatabaseDocumentTx database;

    public DocumentIterableResult(Iterator<ODocument> documents, ODatabaseDocumentTx database) {
        this.documents = documents;
        this.database = database;
    }

    public <R> List<R> map(Function<ODocument, R> mapper){
        try{
            Iterable<ODocument> documentIterable = () -> documents;
            List<R> result = StreamSupport.stream(documentIterable.spliterator(), false).map(mapper).collect(Collectors.toList());
//            List<R> result = documents.stream().map(mapper).collect(Collectors.toList());
            this.database.commit();
            return result;
        }catch (Exception e){
            this.database.rollback();
            throw e;
        }finally {
            this.database.close();
        }
    }
}