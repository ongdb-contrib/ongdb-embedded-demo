package data.lab.ongdb.demo;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.demo
 * @Description: TODO
 * @date 2021/11/25 11:19
 */
public class EmbeddedOngdb {

    private static final File DATABASE_DIRECTORY = new File("target" + File.separator + "graph.db");

    GraphDatabaseService graphDb;

    enum Labels implements Label {
        // FROM节点标签
        人物,

        // TO节点标签
        电影
    }

    enum RelTypes implements RelationshipType {
        // 关系类型
        出演
    }

    /**
     * @param
     * @return
     * @Description: TODO(创建数据库对象并创建样例数据)
     */
    void createDb() throws IOException {
        FileUtils.deleteRecursively(DATABASE_DIRECTORY);

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DATABASE_DIRECTORY);
        registerShutdownHook(graphDb);

        try (Transaction tx = graphDb.beginTx()) {
            ///创建结点
            Node n1 = graphDb.createNode();
            n1.setProperty("name", "Keanu Reeves");
            n1.addLabel(Labels.人物);

            Node n2 = graphDb.createNode();
            n2.setProperty("name", "The Matrix Revolutions");
            n2.addLabel(Labels.电影);

            ///创建关系
            n1.createRelationshipTo(n2, RelTypes.出演);
            tx.success();
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(关闭数据库对象)
     */
    void shutDown() {
        System.out.println();
        System.out.println("Shutting down database ...");
        graphDb.shutdown();
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread(graphDb::shutdown));
    }

    public static void main(final String[] args) throws IOException {
        EmbeddedOngdb graph = new EmbeddedOngdb();
        graph.createDb();
        graph.shutDown();
    }
}

