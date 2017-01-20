package util.frontier;

import com.CrawlUrl;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.*;

import java.io.*;
import java.util.Set;
import java.util.Map.Entry;

public class BDBFrontier extends AbstractFrontier implements Frontier {
    private StoredMap<String, CrawlUrl> pendingUrisDB = null;

    public BDBFrontier(String homeDirectory) throws DatabaseException,
            FileNotFoundException {
        super(homeDirectory);
        EntryBinding<String> keyBinding = new SerialBinding<>(javaCatalog, String.class);
        EntryBinding<CrawlUrl> valueBinding = new SerialBinding<>(javaCatalog, CrawlUrl.class);
        this.pendingUrisDB = new StoredMap<>(database, keyBinding, valueBinding, true);
    }

    @Override
    public CrawlUrl getNext() throws Exception {
        CrawlUrl result = null;
        if (!pendingUrisDB.isEmpty()) {
            Set<Entry<String, CrawlUrl>> entries = pendingUrisDB.entrySet();
            //System.out.println("entries:\n" + entries);
            Entry<String, CrawlUrl> entry =
                    pendingUrisDB.entrySet().iterator().next();

            result = entry.getValue();
            delete(entry.getKey());
        }
        return result;
    }

    @Override
    public void putUrl(CrawlUrl crawlUrl) throws Exception {
        put(crawlUrl.getUrl(), crawlUrl);
    }

    @Override
    protected void put(String key, CrawlUrl value) {
        pendingUrisDB.put(calcUrlKey(key), value);
    }

    @Override
    protected CrawlUrl get(String key) {
        return pendingUrisDB.get(key);
    }

    @Override
    protected CrawlUrl delete(String key) {
        return pendingUrisDB.remove(key);
    }

    public int getSize(){
        return pendingUrisDB.size();
    }

    /**
     * 根据 URL 计算键值, 可以采用 MD5
     */
    private String calcUrlKey(String url) {
        return url;
    }
}
