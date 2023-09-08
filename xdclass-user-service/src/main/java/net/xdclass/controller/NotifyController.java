package net.xdclass.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 通知控制器
 * </p>
 *
 */


@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/notify/v1/")
@Slf4j
public class NotifyController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String CAPTCHA_KEY = "user_service:captcha:";
    private final static long CAPTCHA_KEY_EXPIRE_TIME = 60 * 10 * 1000;

    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String text = captchaProducer.createText();
        log.info("验证码:{}", text);
        //将验证码存入redis
        redisTemplate.opsForValue().set(getCaptchaKey(request), text, CAPTCHA_KEY_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        BufferedImage image = captchaProducer.createImage(text);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            //输出流输出图片，格式jpg
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("图形验证码异常：" + e.getMessage());
        }
    }

    private String getCaptchaKey(HttpServletRequest request) {

        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = CAPTCHA_KEY + CommonUtil.MD5(ip + userAgent);

//        log.info("ip={},userAgent={},key={}", ip, userAgent, key);

        return key;
    }
}
