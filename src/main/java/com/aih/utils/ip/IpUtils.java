package com.aih.utils.ip;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    /**
     * 根据ip获取地址
     * @param ip
     */
    public static String getAddressByIp(String ip) {
        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip="+ ip + "&json=true";
        String str = HttpUtil.get(url);
        if(!StrUtil.hasBlank(str)){
//            String substring = str.substring(str.indexOf("{"), str.indexOf("}")+1);
            JSONObject jsonObject = JSONUtil.parseObj(str);
//            System.out.println(jsonObject);
            return jsonObject.getStr("addr").split(" ")[0];
        }
        return null;
    }
    /**
     * 获取真实ip地址,不返回内网地址
        * @param request
     */
    public static String getIpAddr(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "";
        }
    }

}
