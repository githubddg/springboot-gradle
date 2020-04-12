package com.css.utils;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtils
 * @Description httpClient发送远程请求
 * @Author jiashaoqing
 * @Date 2019/10/20 11:05
 * @Version 1.0
 **/
public class HttpClientUtils {
    /**
    * @Description 发送get请求 传参格式为format,可以设置请求头
    * @Date 11:06 2019/10/20
    * @Param [url, param,headers]
    * @return java.lang.String
    **/
    public static String doGet(String url, Map<String,String> param,Map<String,String> headers){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建URI
            URIBuilder builder = new URIBuilder(url);
            if (param != null){
                for (Map.Entry<String,String> entry : param.entrySet()){
                    builder.addParameter(entry.getKey(),entry.getValue());
                }
            }
            URI uri = builder.build();
            //创建http Get请求
            HttpGet httpGet = new HttpGet(uri);
            //设置请求头
            if (headers != null){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpGet.addHeader(entry.getKey(),entry.getValue());
                }
            }
            //执行请求
            response = httpClient.execute(httpGet);
            //判断返回状态是否为200
            if (200 ==  response.getStatusLine().getStatusCode()){
                resultString = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
    
    /**
    * @Description 发送get请求
    * @Date 11:25 2019/10/20
    * @Param [url]
    * @return java.lang.String
    **/
    public static String doGet(String url){
        return doGet(url,null,null);
    }
    
    /**
    * @Description 发送post请求 传参格式为format,可以设置请求头
    * @Date 11:26 2019/10/20
    * @Param [url, param,headers]
    * @return java.lang.String
    **/
    public static String doPost(String url,Map<String,String> param,Map<String,String> headers){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建http Post请求
            HttpPost httpPost = new HttpPost(url);
            //创建参数列表
            if (param != null){
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String,String> entity : param.entrySet()){
                    paramList.add(new BasicNameValuePair(entity.getKey(),entity.getValue()));
                }
                //模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Charset.forName("UTF-8"));
                httpPost.setEntity(entity);
            }
            //设置请求头
            if (headers != null){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(),entry.getValue());
                }
            }
            //执行http请求
            response = httpClient.execute(httpPost);
            //判断返回状态是否为200
            if (200 ==  response.getStatusLine().getStatusCode()){
                resultString = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
    
    /**
    * @Description 发送无参的post请求
    * @Date 11:31 2019/10/20
    * @Param [url]
    * @return java.lang.String
    **/
    public static String doPost(String url){
        return doPost(url,null,null);
    }

    /**
    * @Description 发送post请求 传参格式为json,支持设置请求头
    * @Date 14:06 2019/10/22
    * @Param [url, json,headers]
    * @return java.lang.String
    **/
    public static String doPostJson(String url,String json,Map<String,String> headers){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建http post请求
            HttpPost httpPost = new HttpPost(url);
            //创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            //设置请求头
            if (headers != null){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(),entry.getValue());
                }
            }
            //执行http请求
            response = httpClient.execute(httpPost);
            //判断返回状态是否为200
            if (200 ==  response.getStatusLine().getStatusCode()){
                resultString = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 发送post请求,传参格式为file,支持设置请求头
     * @param url
     * @param file
     * @param headers
     * @return
     */
    public static String doPostFile(String url, File file,Map<String,String> headers){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建http post请求
            HttpPost httpPost = new HttpPost(url);
            //创建请求内容
            FileEntity fileEntity = new FileEntity(file);
            httpPost.setEntity(fileEntity);
            //设置请求头
            if (headers != null){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(),entry.getValue());
                }
            }
            //执行http请求
            response = httpClient.execute(httpPost);
            //判断返回状态是否为200
            if (200 ==  response.getStatusLine().getStatusCode()){
                resultString = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 传递multipartFile类型的文件
     * @param url 路径
     * @param fileParamName 参数名称
     * @param multipartFile 文件
     * @param headers 报头信息
     * @return
     */
    public static String doPostMultipartFile(String url,String fileParamName, MultipartFile multipartFile,Map<String,String> headers){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建http post请求
            HttpPost httpPost = new HttpPost(url);
            //创建请求内容
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            //解决中文乱码问题
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody(fileParamName,multipartFile.getInputStream(),ContentType.MULTIPART_FORM_DATA,multipartFile.getOriginalFilename());
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            //设置请求头
            if (headers != null){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(),entry.getValue());
                }
            }
            //执行http请求
            response = httpClient.execute(httpPost);
            //判断返回状态是否为200
            if (200 ==  response.getStatusLine().getStatusCode()){
                resultString = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
}
