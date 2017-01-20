import util.*;

import java.util.Set;

public class MyCrawler {
    private static final String MARK = "http://www.uestc.edu.cn/";
    private static final int PAGE_NUM = 500; // 抓取的网页数量
    private LinkQueue linkQueue = new LinkQueue();

    /**
     * 使用种子初始化 URL 队列
     */
    private void initCrawlerWithSeeds(String[] seeds) {
        for (String seed : seeds) {
            linkQueue.addUnvisitedUrl(seed);
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

        // 循环条件：待抓取的网页不为空且抓取的网页不多于 PAGE_NUM
        //int count = 0; debug的时候需要在第count次循环时中断
        while (!linkQueue.unVisitedUrlIsEmpty()
                && linkQueue.getVisitedUrlNum() <= PAGE_NUM) {
            /**
             *  if (++count == 26) {
             *      int a = 0;
             *  }
             */


            String curUrl = (String) linkQueue.unVisitedUrlDequeue();
            if (curUrl == null)
                continue;

            DownLoadFile downLoader = new DownLoadFile();
            downLoader.downloadFile(curUrl);
            linkQueue.addVisitedUrl(curUrl);

            // 只有html页面能够被提取链接
            if (!FileUtilities.isImageFile(curUrl)
                    && !FileUtilities.isXXXFile(curUrl, ".js")
                    && !FileUtilities.isXXXFile(curUrl, ".css")) {

                Set<String> links = HtmlParserUtilities.extraLinks(curUrl, linkFilter);
                for (String newLink : links) {
                    linkQueue.addUnvisitedUrl(newLink);
                }

                Set<String> css = HtmlParserUtilities.extractCSS(curUrl);
                for (String newCss : css) {
                    linkQueue.addUnvisitedUrl(newCss);
                }

                Set<String> images = HtmlParserUtilities.extractImages(curUrl);
                for (String newImg : images) {
                    linkQueue.addUnvisitedUrl(newImg);
                }

                Set<String> scripts = HtmlParserUtilities.extractScripts(curUrl);
                for (String newScript : scripts) {
                    linkQueue.addUnvisitedUrl(newScript);
                }

                Set<String> bgImages = HtmlParserUtilities.extractBgImages(curUrl);
                for (String newBgImage : bgImages) {
                    linkQueue.addUnvisitedUrl(newBgImage);
                }
            }
        }
    }

    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        String[] seeds = new String[1];
        seeds[0] = "http://www.uestc.edu.cn/";
        crawler.crawl(seeds);
    }
}
