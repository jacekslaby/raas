package com.j9soft.poc.alarms;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider of session(s) connected to a Cassandra instance.
 */
class CassandraConnector {

    private static final Logger logger = LoggerFactory.getLogger(CassandraConnector.class);

    private Cluster cluster;

    private Session session;

    void connect(final String node, final Integer port) {

        Cluster.Builder b = Cluster.builder().addContactPoint(node);

        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        Metadata metadata = cluster.getMetadata();
        logger.info("ClusterName: " + metadata.getClusterName());

        for (Host host : metadata.getAllHosts()) {
            logger.info("Host.datacenter: " + host.getDatacenter() + " Host.address: " + host.getAddress() + " Host.rack: " + host.getRack());
        }

        session = cluster.connect();
    }

    Session getSession() {
        return this.session;
    }

    void close() {
        session.close();
        cluster.close();
    }
}
