package com.fgrapp.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-02 19:52
 **/
public class PageUtil {

    public static  <T> Page<T> getParamPage(Map<String,Object> map, Class<T> responseType){

        int current = Integer.parseInt(map.get("pageNum").toString()) ;
        int size = Integer.parseInt(map.get("pageSize").toString());
        Page<T> page = new Page<>(current,size);

        //排序字段
        String order = (String) map.get("order");
        if (StrUtil.isNotEmpty(order)){
            String[] split = order.split(",");
            for (String s : split) {
                String[] strings = s.split("-");
                if (strings.length > 1 && "desc".equals(strings[1])){
                    page.addOrder(OrderItem.desc(strings[0]));
                }else {
                    page.addOrder(OrderItem.asc(strings[0]));
                }
            }
        }
        return page;
    }
}


