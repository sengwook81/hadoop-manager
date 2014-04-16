package org.zero.commons.spring.support.database;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.ClassUtils;

public class HsqlDatabaseDataSource implements FactoryBean<DataSource> {

	protected static final Logger log = LoggerFactory.getLogger(HsqlDatabaseDataSource.class);
	
	EmbeddedDatabaseFactory factory = new  EmbeddedDatabaseFactory();
	ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	
	@Value("#{appsProperties['zero.apps.database.name']}")
	String database_Name;
	
	@Value("#{appsProperties['zero.apps.database.id']}")
	String account_Id;
	
	@Value("#{appsProperties['zero.apps.database.pwd']}")
	String account_Password;
	
	String database_Filepath;
	
	String schema_Path;
	
	String ddl_Sql_Postifx;
	String dml_Sql_Postifx;
	
	public String getDatabase_Name() {
		return database_Name;
	}

	public void setDatabase_Name(String database_Name) {
		this.database_Name = database_Name;
	}

	public String getAccount_Id() {
		return account_Id;
	}

	public void setAccount_Id(String account_Id) {
		this.account_Id = account_Id;
	}

	public String getAccount_Password() {
		return account_Password;
	}

	public void setAccount_Password(String account_Password) {
		this.account_Password = account_Password;
	}

	public String getDatabase_Filepath() {
		return database_Filepath;
	}

	public void setDatabase_Filepath(String database_Filepath) {
		if(database_Filepath.length() > 0 && (database_Filepath.endsWith("/") || database_Filepath.endsWith("\\"))) {
			database_Filepath = database_Filepath +File.separatorChar;
		}
		this.database_Filepath = database_Filepath;
	}

	public String getSchema_Path() {
		return schema_Path;
	}

	/**
	 * ex) classpath*:/app_schema/
	 *     file://C:/app_schema/
	 * @param schema_Path
	 */
	public void setSchema_Path(String schema_Path) {
		if(schema_Path.length() > 0 && (schema_Path.endsWith("/") || schema_Path.endsWith("\\"))) {
			schema_Path = schema_Path +File.separatorChar;
		}
		this.schema_Path = schema_Path;
	}

	public String getDdl_Sql_Postifx() {
		return ddl_Sql_Postifx;
	}

	/**
	 * ex) DDL.sql
	 *     will *DDL.sql Scan
	 * @param ddl_Sql_Postifx
	 */
	public void setDdl_Sql_Postifx(String ddl_Sql_Postifx) {
		this.ddl_Sql_Postifx = ddl_Sql_Postifx;
	}

	public String getDml_Sql_Postifx() {
		return dml_Sql_Postifx;
	}

	/**
	 * ex) DML.sql
	 *     will *DML.sql Scan
	 * @param dml_Sql_Postifx
	 */
	public void setDml_Sql_Postifx(String dml_Sql_Postifx) {
		this.dml_Sql_Postifx = dml_Sql_Postifx;
	}
	
	@Override
	public DataSource getObject() throws Exception {
		log.trace("CREATE DB NAME {}",database_Name);
		if(database_Name == null) {
			throw new RuntimeException("DataBase Name is Null");
		}
		// DataBase Exist Check
		File f = new File(database_Filepath +  database_Name + ".properties");
		if(f.exists()) {
			log.trace("Database Already Exist");
		}
		else { 
		// Initialize Database 
			log.trace("Database Not Exist -> {} ",f.getAbsolutePath());
			
			PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = pathMatchingResourcePatternResolver.getResources(schema_Path + "*" + ddl_Sql_Postifx);
			for(Resource ddlRes : resources) {
				populator.addScript(ddlRes);	
			}
			
			resources = pathMatchingResourcePatternResolver.getResources(schema_Path + "*" + dml_Sql_Postifx);
			for(Resource dmlRes : resources) {
				populator.addScript(dmlRes);	
			}
		}
		factory.setDatabaseType( EmbeddedDatabaseType.HSQL);
		HsqlEmbeddedDatabaseConfigurer configureInstance = HsqlEmbeddedDatabaseConfigurer.getInstance();
		configureInstance.setAccount(account_Id, account_Password);
		configureInstance.setDatabaseFilePath(database_Filepath);
		factory.setDatabaseConfigurer(configureInstance);
		factory.setDatabaseName(database_Name);
		factory.setDatabasePopulator(populator);
		return factory.getDatabase();
	}

	@PreDestroy
	private void close() { 
		if(factory.getDatabase() != null) {
			factory.getDatabase().shutdown();
		}
	}
	@Override
	public Class<?> getObjectType() {
		return EmbeddedDatabase.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public static class HsqlEmbeddedDatabaseConfigurer implements EmbeddedDatabaseConfigurer {

		protected static final Logger log = LoggerFactory.getLogger(HsqlDatabaseDataSource.HsqlEmbeddedDatabaseConfigurer.class);
		
		private static final String JDBC_DRIVER_CLASS = "org.hsqldb.jdbcDriver";
		
		private static HsqlEmbeddedDatabaseConfigurer INSTANCE;
		
		private final Class<? extends Driver> driver_Class;
		
		private String accountId ="zero";
		private String accountPwd = "zero";

		private String database_Filepath2;
		
		/**
		 * Get the singleton {@link HsqlEmbeddedDatabaseConfigurer} instance.
		 * @return the configurer
		 * @throws ClassNotFoundException if HSQL is not on the classpath
		 */
		@SuppressWarnings("unchecked")
		public static synchronized HsqlEmbeddedDatabaseConfigurer getInstance() throws ClassNotFoundException {
			if (INSTANCE == null) {
				INSTANCE = new HsqlEmbeddedDatabaseConfigurer(
						(Class<? extends Driver>) ClassUtils.forName(JDBC_DRIVER_CLASS, HsqlEmbeddedDatabaseConfigurer.class.getClassLoader()));
			}
			return INSTANCE;
		}

		public void setDatabaseFilePath(String database_Filepath) {
			database_Filepath2 = database_Filepath;
			
		}

		private HsqlEmbeddedDatabaseConfigurer(Class<? extends Driver> driver_Class) {
			this.driver_Class = driver_Class;
		}

		public void setAccount(String id, String pwd) { 
			log.trace("SET ACCOUNT {} , {}",id,pwd);
			accountId = id; 
			accountPwd = pwd;
		}
		
		public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
			properties.setDriverClass(this.driver_Class);
			properties.setUrl("jdbc:hsqldb:file:" + database_Filepath2 + databaseName + ";shutdown=true;hsqldb.write_delay=false");
			properties.setUsername(accountId);
			properties.setPassword(accountPwd);
		}

		protected final Log logger = LogFactory.getLog(getClass());

		public void shutdown(DataSource dataSource, String databaseName) {
			try {
				log.debug("Try Shutdown!");
				Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();
				stmt.execute("SHUTDOWN");
			}
			catch (SQLException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not shutdown embedded database", ex);
				}
			}
		}
	}
}