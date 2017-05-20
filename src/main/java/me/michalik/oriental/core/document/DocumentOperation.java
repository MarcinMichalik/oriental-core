package me.michalik.oriental.core.document;


import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OConcurrentResultSet;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import me.michalik.oriental.core.document.results.DocumentIterableResult;
import me.michalik.oriental.core.document.results.DocumentResult;

import java.util.function.Consumer;

public class DocumentOperation {

    private final ODatabaseDocumentTx oDatabaseDocumentTx;

    public DocumentOperation(ODatabaseDocumentTx oDatabaseDocumentTx) {
        this.oDatabaseDocumentTx = oDatabaseDocumentTx;
    }

    public void db(Consumer<ODatabaseDocumentTx> consumer){
        consumer.accept(this.oDatabaseDocumentTx);
//        this.oDatabaseDocumentTx.setUser(new OUser(""));
    }

    public DocumentResult load(ORID orid){
        return this.load(orid, null);
    }

    public DocumentResult load(ORID orid, String fetchPlan){
        return new DocumentResult(this.oDatabaseDocumentTx.load(orid, fetchPlan), this.oDatabaseDocumentTx);
    }

    public DocumentResult load(ORID orid, String fetchPlan, boolean ignoreCache){
        return new DocumentResult(this.oDatabaseDocumentTx.load(orid, fetchPlan, ignoreCache), this.oDatabaseDocumentTx);
    }

    public DocumentResult load(ORecord oRecord){
        return this.load(oRecord, null);
    }

    public DocumentResult load(ORecord oRecord, String fetchPlan){
        return new DocumentResult(this.oDatabaseDocumentTx.load(oRecord, fetchPlan), this.oDatabaseDocumentTx);
    }

    public DocumentResult load(ORecord oRecord, String fetchPlan, boolean ignoreCache){
        return new DocumentResult(this.oDatabaseDocumentTx.load(oRecord, fetchPlan, ignoreCache), this.oDatabaseDocumentTx);
    }

    public DocumentIterableResult query(String query, Object... objects){
        return this.query(new OSQLSynchQuery(query), objects);
    }

    public DocumentIterableResult query(OSQLQuery query, Object... objects){
        OConcurrentResultSet<ODocument> documents = this.oDatabaseDocumentTx.query(query, objects);
        return new DocumentIterableResult(documents.iterator(), this.oDatabaseDocumentTx);
    }

    public ORecordId save(String className, Consumer<ODocument> consumer){
        return this.save(className, null, consumer);
    }

    public ORecordId save(String className, String clusterName, Consumer<ODocument> consumer){
        try {
            ODocument document = new ODocument(className);
            consumer.accept(document);
            document.save(clusterName);
            this.oDatabaseDocumentTx.commit();
            return new ORecordId(document.getIdentity());
        }catch (Exception e){
            this.oDatabaseDocumentTx.rollback();
            throw e;
        }finally {
            this.oDatabaseDocumentTx.close();
        }
    }

    public void update(ORID orid, Consumer<ODocument> consumer){
        try {
            ODocument document = this.oDatabaseDocumentTx.load(orid);
            consumer.accept(document);
            document.save();
            this.oDatabaseDocumentTx.commit();
        }catch (Exception e){
            this.oDatabaseDocumentTx.rollback();
            throw e;
        }finally {
            this.oDatabaseDocumentTx.close();
        }
    }

    public long countClass(String className){
        return this.countClass(className, true);
    }

    public long countClass(String className, boolean polymorphic){
        return this.count(this.oDatabaseDocumentTx.countClass(className, polymorphic));
    }

    public long countClusterElements(int clusterId){
        return this.countClusterElements(new int[]{clusterId});
    }

    public long countClusterElements(int[] clusterIds){
        return this.count(this.oDatabaseDocumentTx.countClusterElements(clusterIds));
    }

    public long countClusterElements(String clusterName){
        return this.count(this.oDatabaseDocumentTx.countClusterElements(clusterName));
    }

    private long count(long result){
        try {
            this.oDatabaseDocumentTx.commit();
            return result;
        }catch (Exception e){
            this.oDatabaseDocumentTx.rollback();
            throw e;
        }finally {
            this.oDatabaseDocumentTx.close();
        }
    }

    public DocumentIterableResult browseClass(String className){
        return this.browseClass(className, true);
    }

    public DocumentIterableResult browseClass(String className, boolean polymorphic){
        ORecordIteratorClass<ODocument> documents = this.oDatabaseDocumentTx.browseClass(className, polymorphic);
        return new DocumentIterableResult(documents.iterator(), this.oDatabaseDocumentTx);
    }

    public DocumentIterableResult browseCluster(String clusterName){
        ORecordIteratorCluster<ODocument> documents = this.oDatabaseDocumentTx.browseCluster(clusterName);
        return new DocumentIterableResult(documents.iterator(), this.oDatabaseDocumentTx);
    }
}