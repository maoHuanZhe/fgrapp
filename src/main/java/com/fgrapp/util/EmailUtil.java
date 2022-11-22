package com.fgrapp.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fgrapp.result.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author fgr
 * @date 2022-11-02 20:11
 **/
@Slf4j
@Component
public class EmailUtil {
    @Value("${ali.accessKeyId}")
    private String accessKeyId;
    @Value("${ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${ali.endpoint}")
    private String endpoint;
    @Value("${ali.message.signName}")
    private String signName;
    @Value("${ali.message.templateCode}")
    private String templateCode;

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception Exception
     */
    public Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }

    public boolean sendPhoneMessage(String phone,String code){
        Client client;
        try {
            client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\""+code+"\"}");
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.getBody();
            String bodyCode = body.getCode();
            //记录短信发送日志
            log.info("body:{},phone:{},code:{}",body,phone,code);
            return "OK".equals(bodyCode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sendEmail;

    @Autowired
    private TemplateEngine templateEngine;

    @Async("commonThreadPool")
    public void sendThymeleafMail(String email,String code) {
        try {
            //根据邮件发送短信
            log.info("给{}发送验证码:{}",email,code);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.setSubject("【FGRAPP】注册登录验证码");
            helper.setFrom(sendEmail);
            helper.setTo(email);
            helper.setSentDate(new Date());
            // 这里引入的是Template的Context
            Context context = new Context();
            // 设置模板中的变量
            context.setVariable("code", code);
            // 第一个参数为模板的名称
            String process = templateEngine.process("register.html", context);
            // 第二个参数true表示这是一个html文本
            helper.setText(process,true);
            javaMailSender.send(mimeMessage);
        }  catch (Exception e){
            log.info("给{}发送验证码:{},失败",email,code);
            throw new ResultException("邮件发送失败");
        }
    }

}

