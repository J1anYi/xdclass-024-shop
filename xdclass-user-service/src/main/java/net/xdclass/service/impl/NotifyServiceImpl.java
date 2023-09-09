package net.xdclass.service.impl;


import net.xdclass.component.MailService;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CheckUtil;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    public static final String CONTENT = "您的验证码是%s,有效期为5分钟,打死也不要告诉别人哦!";
    public static final String SUBJECT = "小滴课堂注册验证码";

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        // 用户注册
        if (sendCodeEnum == SendCodeEnum.USER_REGISTER) {
            if (CheckUtil.isEmail(to)) {
                String code = CommonUtil.generateCode(6);
                //发送邮件
                mailService.sendMail(to, SUBJECT, String.format(CONTENT, code));
                return JsonData.buildSuccess();
            } else if (CheckUtil.isPhone(to)) {
                //发送短信
                return JsonData.buildSuccess();
            }
        }

        return null;
    }
}
