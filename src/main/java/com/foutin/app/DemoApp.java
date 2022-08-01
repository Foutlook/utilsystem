package com.foutin.app;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

/**
 * @author xingkai.fan
 * @description
 * @date 2020/6/8
 */
public class DemoApp {

    private static String url = "https://meishi.meituan.com/i/?ci=1&stid_b=1&cevent=imt%2Fhomepage%2Fcategory1%2F1";

    public static void main(String[] args) {

        // 循环分页参数。查询第n页商家列表
        for (int offset=0; offset<=5000;offset+=20) {
            System.err.println("================查询列表"+offset+"开始==================");
            //String curl ="https://www.zhihu.com/node/ExploreRecommendListV2";
            String charset = "utf-8";
            // head参数
            Map<String, Object> headMap = new HashMap<String, Object>();
            headMap.put("Accept", "application/json");
            headMap.put("Accept-Encoding", "gzip, deflate, sdch");
            headMap.put("Accept-Language", "zh-CN,zh;q=0.8");
            headMap.put("Connection", "keep-alive");
            headMap.put("Cookie", "_lx_utm=utm_source%3Dbaidu%26utm_medium%3Dorganic%26utm_term%3D%25E7%25BE%258E%25E5%259B%25A2; _lxsdk_cuid=1660e95eb800-0f2031d9d5be35-5e4f2b18-1fa400-1660e95eb81c8; ci=1; rvct=1; __mta=174322589.1537843043265.1537843043265.1537843043265.1; client-id=ee6bc291-6edb-479a-82f3-199c3edbbd0c; uuid=5c2d0bebd8a84581b5ac.1537842993.1.0.0; _lxsdk_s=1660e95eb82-de6-d8e-17f%7C%7C7");
            headMap.put("Host", "meishi.meituan.com");
            headMap.put("Referer", "http://meishi.meituan.com/i/?ci=1&stid_b=1&cevent=imt%2Fhomepage%2Fcategory1%2F1");
            headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

            // 请求参数
            Map<String, Object> map = new HashMap<String, Object>();
            // 分页显示的数据
            map.put("limit", 20);
            map.put("offset", offset);
            map.put("app", "");
            // areaId 14, cateId 1  朝阳全部
            // areaId 17, cateId 1  海淀全部
            map.put("areaId", 17);
            map.put("cateId", 1);
            map.put("deal_attr_23", "");
            map.put("deal_attr_24", "");
            map.put("deal_attr_25", "");
            map.put("lineId", 0);
            map.put("optimusCode", 10);
            map.put("originUrl", "http://meishi.meituan.com/i/?ci=1&stid_b=1&cevent=imt%2Fhomepage%2Fcategory1%2F1");
            map.put("partner", 126);
            map.put("platform", 3);
            map.put("poi_attr_20033", "");
            map.put("poi_attr_20043", "");
            map.put("riskLevel", 1);
            map.put("sort", "default");
            map.put("stationId", 0);
            map.put("uuid", "5c2d0bebd8a84581b5ac.1537842993.1.0.0");
            map.put("version", "8.3.3");
            Gson gson = new Gson();
            System.err.println("====================================================");
            System.err.println(offset);
            System.err.println("====================================================");
            // 根据httpclient模仿post请求
            String httpOrgCreateTestRtn = doPost(url, map, charset, headMap);
            Map maps = gson.fromJson(httpOrgCreateTestRtn, Map.class);
            Map linkedTreeMap = gson.fromJson(httpOrgCreateTestRtn, Map.class);
            Map dataMap = gson.fromJson(gson.toJson(linkedTreeMap.get("data")), Map.class);
            if (dataMap.get("poiList") == null) {
                System.err.println("============================");
                System.err.println("=============店铺数据查询完毕=============");
                System.err.println("============================");
                break;
            }
            Map poiListMap = gson.fromJson(gson.toJson(dataMap.get("poiList")), Map.class);
            gson.fromJson(gson.toJson(poiListMap.get("poiInfos")), List.class);
        }
    }

    // httpUtil doPost方法如下
    public static String doPost(String url, Map<String,Object> map, String charset, Map<String, Object> headMap){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);

            // 设置header
            Iterator headIterator = headMap.entrySet().iterator();
            while(headIterator.hasNext()){
                Map.Entry<String,Object> elem = (Map.Entry<String, Object>) headIterator.next();
                httpPost.setHeader(elem.getKey(), elem.getValue().toString());
            }

            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,Object> elem = (Map.Entry<String, Object>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue().toString()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    // 因服务器用的gzip编码传输。
                    // 突然resEntity.getContentEncoding() 空指针。。。。不清楚为什么
                    if (resEntity.getContentEncoding() != null) {
                        if("gzip".equalsIgnoreCase(resEntity.getContentEncoding().getValue())){
                            resEntity = new GzipDecompressingEntity(resEntity);
                        } else if("deflate".equalsIgnoreCase(resEntity.getContentEncoding().getValue())){
                            resEntity = new DeflateDecompressingEntity(resEntity);
                        }
                    }
                    result = EntityUtils.toString(resEntity, charset);

                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
