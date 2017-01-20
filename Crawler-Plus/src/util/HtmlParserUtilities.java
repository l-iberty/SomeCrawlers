package util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.*;

public class HtmlParserUtilities {
    /**
     * 获取网页上的链接，filter 用来过滤
     */
    public static Set<String> extraLinks(String url, LinkFilter filter) {
        Set<String> linksSet = new HashSet<>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            // frameFilter处理 <frame> & <a> 标签，提取标签的 src 属性值
            NodeFilter frameFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    if (node.getText().startsWith("frame")) {
                        return true;
                    } else
                        return false;
                }
            };

            // 过滤 <frame> & <a> 标签
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
            // OrFilter, 逻辑或, 两个Filter只要满足一个就行
            NodeList nodeList = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                Node tag = nodeList.elementAt(i);
                if (tag instanceof LinkTag) { // <a>
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();
                    if (filter.accept(linkUrl))
                        linksSet.add(linkUrl);
                } else { // <frame>
                    // 提取src属性值: 形如 <frame src="somepage.html"/>
                    String frame = tag.getText();
                    int startPos = frame.indexOf("src=") + 5;
                    int endPos;
                    endPos = startPos;
                    if (frame.contains("\'") && !frame.contains("\"")) {
                        while (frame.charAt(endPos++) != '\'') ;
                    } else {
                        while (frame.charAt(endPos++) != '\"') ;
                    }
                    endPos--;

                    String frameUrl = frame.substring(startPos, endPos);
                    if (filter.accept(frameUrl))
                        linksSet.add(frameUrl);

                }
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return linksSet;
    }

    /**
     * 提取 <link> 标签引用的 css
     */
    public static Set<String> extractCSS(String url) {
        // <link rel="stylesheet" href="style.css">
        Set<String> cssSet = new HashSet<>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter cssFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    String nodeText = node.getText();
                    if (nodeText.startsWith("link") && nodeText.contains("stylesheet")) {
                        return true;
                    } else
                        return false;
                }
            };

            NodeList nodeList = parser.extractAllNodesThatMatch(cssFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                String text = nodeList.elementAt(i).getText();
                int startPos = text.indexOf("href=") + 6;
                int endPos;
                endPos = startPos;
                // 大多数html标签内的属性值存在于双引号内，但有时会遇到单引号的情况
                if (text.contains("\'") && !text.contains("\"")) {
                    while (text.charAt(endPos++) != '\'') ;
                } else {
                    while (text.charAt(endPos++) != '\"') ;
                }
                endPos--;

                String cssUrl = text.substring(startPos, endPos);
                if (!cssUrl.startsWith("http://")) {
                    cssUrl = parser.getURL() + cssUrl;
                }
                cssSet.add(cssUrl);
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return cssSet;
    }

    /**
     * 提取 <img> 标签引用的图片
     */
    public static Set<String> extractImages(String url) {
        // <img src="image.png"/>
        Set<String> imagesSet = new HashSet<>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter imgFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    if (node.getText().startsWith("img")) {
                        return true;
                    } else
                        return false;
                }
            };

            NodeList nodeList = parser.extractAllNodesThatMatch(imgFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                String text = nodeList.elementAt(i).getText();
                int startPos = text.indexOf("src=") + 5;
                int endPos;
                endPos = startPos;
                if (text.contains("\'") && !text.contains("\"")) {
                    while (text.charAt(endPos++) != '\'') ;
                } else {
                    while (text.charAt(endPos++) != '\"') ;
                }
                endPos--;

                String imgUrl = text.substring(startPos, endPos);
                if (!imgUrl.startsWith("http://")) {
                    imgUrl = parser.getURL() + imgUrl;
                }
                imagesSet.add(imgUrl);
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return imagesSet;
    }

    /**
     * 提取 javascript
     */
    public static Set<String> extractScripts(String url) {
        Set<String> scriptsSet = new HashSet<>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter scriptFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    String nodeText = node.getText();
                    if (nodeText.startsWith("script") && nodeText.contains(".js\"")) {
                        return true;
                    } else
                        return false;
                }
            };

            NodeList nodeList = parser.extractAllNodesThatMatch(scriptFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                String text = nodeList.elementAt(i).getText();
                int startPos = text.indexOf("src=") + 5;
                int endPos;
                endPos = startPos;
                if (text.contains("\'") && !text.contains("\"")) {
                    while (text.charAt(endPos++) != '\'') ;
                } else {
                    while (text.charAt(endPos++) != '\"') ;
                }
                endPos--;

                String scriptUrl = text.substring(startPos, endPos);
                if (!scriptUrl.startsWith("http://")) {
                    scriptUrl = parser.getURL() + scriptUrl;
                }
                scriptsSet.add(scriptUrl);
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return scriptsSet;
    }

    /**
     * 提取背景图片 background-image
     */
    public static Set<String> extractBgImages(String url) {
        // <body style="background-image:url(bgimage.jpg)">
        Set<String> bgImagesSet = new HashSet<>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter bgImageFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    if (node.getText().contains("background-image")) {
                        return true;
                    } else
                        return false;
                }
            };

            NodeList nodeList = parser.extractAllNodesThatMatch(bgImageFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                String text = nodeList.elementAt(i).getText();
                int startPos = text.indexOf("url") + 4;
                int endPos;
                endPos = startPos;
                while (text.charAt(endPos++) != ')') ;
                endPos--;

                String bgImageUrl = text.substring(startPos, endPos);
                if (!bgImageUrl.startsWith("http://")) {
                    bgImageUrl = parser.getURL() + bgImageUrl;
                }
                bgImagesSet.add(bgImageUrl);
            }
        } catch (ParserException e) {
            //e.printStackTrace();
        }

        return bgImagesSet;
    }
}
