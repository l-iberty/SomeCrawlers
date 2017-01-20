import com.CrawlUrl;

import util.*;
import util.bloomfilter.*;
import util.frontier.*;

import java.util.*;

public class MyCrawler {
    private static final String MARK = "http://www.uestc.edu.cn/";
    private static final int PAGE_NUM = 300; // 抓取的网页数量
    private BDBFrontier bdbFrontier = null;
    private SimpleBloomFilter visitedUrl = new SimpleBloomFilter();

    /**
     * 使用种子初始化数据库
     */
    private void initCrawlerWithSeeds(String[] seeds) {
        try {
            bdbFrontier = new BDBFrontier("bdb\\");

            for (String seed : seeds) {
                CrawlUrl crawlUrl = new CrawlUrl();
                crawlUrl.setUrl(seed);
                bdbFrontier.putUrl(crawlUrl);
            }
        } catch (Exception e) {
            System.err.println("Database error");
        }
    }

    /**
     * 执行抓取
     */
    public void crawl(String[] seeds) {
        LinkFilter linkFilter = new LinkFilter() {
            @Override
            public boolean accept(String url) {
                if (url.startsWith(MARK)
                        && !url.contains("#")
                        /*&& !url.contains("?")*/)
                    return true;
                else
                    return false;
            }
        };

        initCrawlerWithSeeds(seeds);

        for (int i = 0; i < PAGE_NUM && bdbFrontier.getSize() > 0; i++) {
            try {
                String curUrl = bdbFrontier.getNext().getUrl();
                if (curUrl == null || visitedUrl.contains(curUrl))
                    continue;

                DownLoadFile downLoader = new DownLoadFile();
                downLoader.downloadFile(curUrl);
                visitedUrl.add(curUrl);

                // 只有html页面能够被提取链接
                if (!FileUtilities.isImageFile(curUrl)
                        && !FileUtilities.isXXXFile(curUrl, ".js")
                        && !FileUtilities.isXXXFile(curUrl, ".css")) {
                    CrawlUrl crawlUrl = new CrawlUrl();

                    Set<String> links = HtmlParserUtilities.extraLinks(curUrl, linkFilter);
                    for (String newLink : links) {
                        crawlUrl.setUrl(newLink);
                        bdbFrontier.putUrl(crawlUrl);
                    }

                    Set<String> css = HtmlParserUtilities.extractCSS(curUrl);
                    for (String newCss : css) {
                        crawlUrl.setUrl(newCss);
                        bdbFrontier.putUrl(crawlUrl);
                    }

                    Set<String> images = HtmlParserUtilities.extractImages(curUrl);
                    for (String newImg : images) {
                        crawlUrl.setUrl(newImg);
                        bdbFrontier.putUrl(crawlUrl);
                    }

                    Set<String> scripts = HtmlParserUtilities.extractScripts(curUrl);
                    for (String newScript : scripts) {
                        crawlUrl.setUrl(newScript);
                        bdbFrontier.putUrl(crawlUrl);
                    }

                    Set<String> bgImages = HtmlParserUtilities.extractBgImages(curUrl);
                    for (String newBgImage : bgImages) {
                        crawlUrl.setUrl(newBgImage);
                        bdbFrontier.putUrl(crawlUrl);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bdbFrontier.close();
    }

    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        String[] seeds = new String[1];
        seeds[0] = "http://www.uestc.edu.cn/";
        crawler.crawl(seeds);
    }
}
