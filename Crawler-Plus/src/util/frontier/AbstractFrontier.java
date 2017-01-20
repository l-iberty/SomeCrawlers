package util.frontier;

import com.CrawlUrl;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;

import java.io.*;

public abstract class AbstractFrontier {
    private Environment env;
    private static final String CLASS_CATALOG = "java_class_catalog";
    protected StoredClassCatalog javaCatalog;
    protected Database catalogDatabase;
    protected Database database;

    public AbstractFrontier(String homeDirectory) throws DatabaseException,
            FileNotFoundException {
        // 环境设置
        System.out.println("Opening environment in " + homeDirectory);
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        this.env = new Environment(new File(homeDirectory), envConfig);

        // 数据库设置
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);

        // 创建并打开数据库 catalogDatabase
        catalogDatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
        javaCatalog = new StoredClassCatalog(catalogDatabase);

        // 创建并打开数据库 database
        database = env.openDatabase(null, "URL", dbConfig);
    }

    public void close() throws DatabaseException {
        database.close();
        catalogDatabase.close();
        javaCatalog.close();
        env.close();
    }

    protected abstract void put(String key, CrawlUrl value);

    protected abstract CrawlUrl get(String key);

    protected abstract CrawlUrl delete(String key);
}
