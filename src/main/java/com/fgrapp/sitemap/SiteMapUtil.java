package com.fgrapp.sitemap;

import com.fgrapp.domain.FuncTopicDo;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * @author fgr
 * @date 2022-11-27 09:43
 **/
@Slf4j
public class SiteMapUtil {
    /**
     * 日期格式化
     */
    private static final W3CDateFormat DATE_FORMAT = new W3CDateFormat(W3CDateFormat.Pattern.DAY);

    /**
     * 网站域名
     */
    private static final String DOMAIN = "http://www.fgrapp.com/page/detail/";

    public static void generator(List<FuncTopicDo> list) {
        log.info("开始生成site.xml");
        try {
            //获取sitemap的文件存放路径（这里测试使用直接使用临时文件夹）
            File sitemapDir = new File("./");
            if (!sitemapDir.exists()) {
                //新建文件夹
                sitemapDir.mkdir();
            }
            //创建generator，通过generator生成站点地图
            WebSitemapGenerator generator =  WebSitemapGenerator.builder(DOMAIN, sitemapDir)
                        .fileNamePrefix("site")
                        .dateFormat(DATE_FORMAT)
                        .build();

            list.forEach(item -> {
                try {
                    //新建一个网页地址，有多少个网页就在这里新建多少个WebSitemapUrl对象
                    //options这里填网页的url地址，lastMod填写网页最后一次修改时间
                    WebSitemapUrl indexSitemap = new WebSitemapUrl
                            .Options(DOMAIN + item.getId())
                            .lastMod(Date.from(item.getLastUpdateTime().toInstant(ZoneOffset.UTC))).build();
                    //将网页地址添加到generator
                    generator.addUrl(indexSitemap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
            //生成站点地图文件
            generator.write();
            log.info("site.xml生成成功");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error("site.xml生成失败");
        }
    }
}
