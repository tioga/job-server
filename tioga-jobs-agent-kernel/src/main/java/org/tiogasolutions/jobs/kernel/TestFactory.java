package org.tiogasolutions.jobs.kernel;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.request.CouchFeature;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.jobs.kernel.config.CouchServersConfig;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@Named
@Profile("test")
public class TestFactory {

  public static final String API_KEY = "9999";
  public static final String API_PASSWORD = "unittest";

  @Inject
  public TestFactory(CouchServersConfig domainDbConfig) throws Exception {

    String sysDatabase = "test-jobs";
    String usrDatabase = domainDbConfig.getDomainDatabasePrefix() + "testing" + domainDbConfig.getDomainDatabaseSuffix();

    CouchServer server = new DefaultCouchServer();

    for (String dbName : Arrays.asList(sysDatabase, usrDatabase)) {
      CouchDatabase database = server.database(dbName, CouchFeatureSet.builder()
        .add(CouchFeature.ALLOW_DB_DELETE, true)
        .build());

      if (database.exists()) {
        database.deleteDatabase();
      }
    }
  }

  public String toHttpAuth(String username, String password) {
    byte[] value = (username + ":" + password).getBytes();
    return "Basic " + DatatypeConverter.printBase64Binary(value);
  }
}
