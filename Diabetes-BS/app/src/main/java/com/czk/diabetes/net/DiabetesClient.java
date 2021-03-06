package com.czk.diabetes.net;

import android.text.Editable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by 陈忠凯 on 2017/3/30.
 */


public class DiabetesClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    /****************************************************************
     * 来自智抗糖的start
     ****************************************************************/
    private static final String BASE_URL = "https://diabetesintf.izhangkong.com/mobile/tool/";
    private static final String BASE_RECIPE_URL = "http://wikidiabetes.izhangkong.com/mobile/tool/";
    private static final String DVE_ID = "867068024369976";

    public static String getZKTAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static String getZKTRECIPEAbsoluteUrl(String relativeUrl) {
        return BASE_RECIPE_URL + relativeUrl;
    }

    /**
     * 获得历史和热门查询药品
     * @return
     */
    public static RequestParams getHistoryAndHotDrugsParams() {
        RequestParams params = new RequestParams();
        params.add("dev",DVE_ID);
        params.add("dev_type","android");
        params.add("join_id","123");
        params.add("loadFrom","1000115");
        params.add("req_num","");
        params.add("sessionID","eba63ded5fff28a977b89fed9ab34c6c");
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("valid","");
        params.add("ver","59");
        return params;
    }

    /**
     * 添加搜索药品到历史记录
     * @param id
     * @return
     */
    public static RequestParams addDrugsSearchParams(String id) {
        RequestParams params = new RequestParams();
        params.add("dev",DVE_ID);
        params.add("dev_type","android");
        params.add("id",id);
        params.add("join_id","123");
        params.add("loadFrom","1000115");
        params.add("req_num","");
        params.add("sessionID","eba63ded5fff28a977b89fed9ab34c6c");
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("valid","");
        params.add("ver","59");
        return params;
    }

    /**
     * 清空搜索药品历史记录
     * @return
     */
    public static RequestParams delDrugsSearchParams() {
        RequestParams params = new RequestParams();
        params.add("dev",DVE_ID);
        params.add("dev_type","android");
        params.add("join_id","123");
        params.add("loadFrom","1000115");
        params.add("req_num","");
        params.add("sessionID","eba63ded5fff28a977b89fed9ab34c6c");
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("valid","");
        params.add("ver","59");
        return params;
    }

    /**
     * 添加搜索药品到历史记录
     * @param key
     * @return
     */
    public static RequestParams getDrugsListParams(String key) {
        RequestParams params = new RequestParams();
        params.add("dev",DVE_ID);
        params.add("dev_type","android");
        params.add("join_id","123");
        params.add("loadFrom","1000115");
        params.add("page","1");
        params.add("req_num","");
        params.add("rows","20");
        params.add("searchName",key);
        params.add("sessionID","eba63ded5fff28a977b89fed9ab34c6c");
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("valid","");
        params.add("ver","59");
        return params;
    }

    /**
     * 获取食谱
     * @return
     */
    public static RequestParams getCookBooksNew(int page) {
        RequestParams params = new RequestParams();
        params.add("rows","10");
        params.add("page", String.valueOf(page));
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("isMyCollected","-1");
        return params;
    }
    /****************************************************************
     * 来自智抗糖的end
     ****************************************************************/

    /****************************************************************
     * 来自云服务器的start
     ****************************************************************/
    public static final String MY_BASE_URL = "http://120.24.2.161/";
    public static String getAbsoluteUrl(String relativeUrl) {
        return MY_BASE_URL + relativeUrl+".php";
    }

    public static String getUserImgUrl(String imgName) {
        return MY_BASE_URL + "img/users/" + imgName;
    }

    public static String getAthleticImgUrl(String imgName) {
        return MY_BASE_URL + "img/athletics/" + imgName;
    }

    public static RequestParams checkLogIn(String name, String pwd) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        params.add("pwd", pwd);
        return params;
    }

    public static RequestParams getDoctor(int id) {
        RequestParams params = new RequestParams();
        params.add("id",String.valueOf(id));
        return params;
    }

    public static RequestParams getUserInfo(String name) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        return params;
    }

    public static RequestParams getContectList(String name,int id) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        params.add("id",String.valueOf(id));
        return params;
    }
    public static RequestParams sendContactInfo(String name, String text, long time, int id) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        params.add("text",text);
        params.add("time",String.valueOf(time));
        params.add("doctorId",String.valueOf(id));
        return params;
    }
    public static RequestParams creatContactTable(String name) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        return params;
    }

    public static RequestParams saveFile(String path,String savePath,String name) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("path", savePath);
        params.put("name", name);
        params.put("uploadfile", new File(path));
        return params;

    }
    public static RequestParams sign(String name, String pwd, String sex, String years, String img) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("pwd", pwd);
        params.put("sex", sex);
        params.put("year", years);
        params.put("img", img);
        return params;
    }

    public static RequestParams doSqlTow(String aql) {
        RequestParams params = new RequestParams();
        params.put("aql", aql);
        return params;
    }

    public static RequestParams sendAthlitec(String name, String text, String img, long time) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("text", text);
        params.put("img", img);
        params.put("time", time);
        return params;
    }
    /****************************************************************
     * 来自云服务器的end
     ****************************************************************/


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }



}
