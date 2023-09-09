package net.xdclass.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.MailService;
import net.xdclass.constants.CacheKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CheckUtil;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String CONTENT = "您的验证码是%s,有效期为5分钟,打死也不要告诉别人哦!";
    public static final String SUBJECT = "小滴课堂注册验证码";

    // 验证码有效期10min
    public static final long CODE_EXPIRED = 60 * 1000 * 10;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY, sendCodeEnum.name().toLowerCase(), to);
        String cacheValue = (String) redisTemplate.opsForValue().get(cacheKey);

        // 查看是否在60s内发送过验证码
        if (StringUtils.isNotBlank(cacheValue)) {
            // 查看时间戳是否小于60s
            long gap = CommonUtil.getCurrentTimestamp() - Long.parseLong(cacheValue.split("_")[1]);
            if (gap < 60 * 1000) {
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
            // 如果大于60s，不用删除旧验证码，下面会覆盖掉
        }

        // 生成验证码
        String code = CommonUtil.generateCode(6);
        log.info("mail code:{}", code);
        String value = code + "_" + CommonUtil.getCurrentTimestamp();

        // 存储验证码
        redisTemplate.opsForValue().set(cacheKey, value, CODE_EXPIRED, TimeUnit.MILLISECONDS);

        // 用户注册
        if (CheckUtil.isEmail(to)) {
            //发送邮件
            mailService.sendMail(to, SUBJECT, String.format(CONTENT, code));
            log.info("mail send success, mail:{}, code:{}", to, code);
            return JsonData.buildSuccess();
        } else if (CheckUtil.isPhone(to)) {
            //发送短信
            return JsonData.buildSuccess();
        }

        return null;
    }
}
