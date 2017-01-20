package com;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CrawlUrl implements Serializable {
    private static final long serialVersionUID = 7931672194843948629L;

    private String origUrl;             // 原始 URL, 主机部分是域名
    private String url;                 // URL 的值, 主机部分是IP, 为了防止重复主机的出现
    private int urlNo;                  // URL 编号
    private int statusCode;             // URL 的请求状态码
    private int hitNum;                 // URL 被引用的次数
    private String charSet;             // 此 URL 对应的文章的字符编码
    //private String abstractText;        // 文章摘要
    //private String author;
    private int weight;                 // 文章的权重
    //private String description;         // 文章的描述
    private int fileSize;               // 文章的文件大小
    //private Timestamp lastUpdateTime;   // 最后修改时间
    //private Date timeToLive;            // 过期时间
    //private String title;               // 文章标题
    //private String type;                // 文章类型
    //private String[] urlRef;            // 引用的链接
    private int crawlingLayer;          // 爬取得层次（深度）

    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUrlNo() {
        return urlNo;
    }

    public void setUrlNo(int urlNo) {
        this.urlNo = urlNo;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getHitNum() {
        return hitNum;
    }

    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getCrawlingLayer() {
        return crawlingLayer;
    }

    public void setCrawlingLayer(int crawlingLayer) {
        this.crawlingLayer = crawlingLayer;
    }
}
