package com.czk.diabetes.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
    public static RequestParams getCookBooksNew() {
        RequestParams params = new RequestParams();
        params.add("rows","10");
        params.add("page","1");
        params.add("sessionMemberID","eba63ded5fff28a9f045d0363e4d0f7d");
        params.add("isMyCollected","-1");
        return params;
    }
    /****************************************************************
     * 来自智抗糖的end
     ****************************************************************/

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }


}
