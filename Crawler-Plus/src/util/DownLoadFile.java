package util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.htmlparser.util.ParserException;

import java.io.*;

public class DownLoadFile {

    /**
     * 根据 URL 和网页类型生成需要保存的网页文件名，去除 URL 中的非文件名字符
     */
    public String getFileNameByUrl(String rootDir, String url, String contentType) {
        // 去除"http://"
        if (url.startsWith("http://")) {
            url = url.substring(7);
        }

        // contentType形如"text/html; CHARSET=UTF-8"或"image/jpeg"
        String filename;
        String extensionName; // 扩展名
        filename = url.replaceAll("[\\?:*<>|]", "_");
        contentType = contentType.toLowerCase();

        // 先从 Content-Type 提取可能的文件扩展名
        if (contentType.contains("charset")) {
            extensionName = contentType.substring(contentType.indexOf('/') + 1, contentType.indexOf(';'));
        } else {
            extensionName = contentType.substring(contentType.indexOf('/') + 1);
        }
        extensionName = "." + extensionName;

        // 对于某些特殊文件，Content-Type 与其真实文件扩展名不一致，需特殊处理
        if (filename.endsWith(extensionName)
                || FileUtilities.isImageFile(filename)
                || FileUtilities.isXXXFile(filename, ".js")) {
            filename = rootDir + filename;
        } else {
            filename = rootDir + filename + extensionName;
        }

        if (FileUtilities.isImageFile(filename)
                || FileUtilities.isXXXFile(filename, ".js")) {
            extensionName = FileUtilities.getExtensionName(filename);
        }

        FileUtilities.createFile(filename, extensionName);

        // 存在于html中的文件路径分隔符为"\", 在Windows系统下需要进行转换
        return FileUtilities.getPathname(filename);
    }

    /**
     * 保存网页字节数组到本地文件，filepath 为要保存的文件路径
     */
    private void saveToLocal(InputStream datas, String filepath) {
        try {
            FileOutputStream out = new FileOutputStream(new File(filepath));
            int curByte;
            while ((curByte = datas.read()) != -1) {
                out.write(curByte);
            }
            out.flush();
            out.close();
            System.out.println(filepath + " saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载 URL 指向的网页
     */
    public String downloadFile(String url) {
        String filepath = null;

        // 创建 HttpClient 对象并设置参数
        HttpClient httpClient = new HttpClient();

        // 设置连接超时为5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        // 使用 GET 请求，请求超时为5s
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        // 执行 GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
                return null;
            }

            // 处理响应内容
            InputStream responseBody = getMethod.getResponseBodyAsStream();
            filepath = getFileNameByUrl("temp\\", url,
                    getMethod.getResponseHeader("Content-Type").getValue());
            saveToLocal(responseBody, filepath);
        } catch (HttpException e) {
            System.err.println("Check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Error!");
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }

        return filepath;
    }
}
