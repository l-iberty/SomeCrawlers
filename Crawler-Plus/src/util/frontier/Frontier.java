package util.frontier;

import com.CrawlUrl;

public interface Frontier {
    public CrawlUrl getNext() throws Exception;
    public void putUrl(CrawlUrl crawlUrl) throws Exception;
}
