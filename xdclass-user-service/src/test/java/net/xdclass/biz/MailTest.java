package net.xdclass.biz;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.UserApplication;
import net.xdclass.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailTest {

    @Autowired
    private MailService mailService;
    @Test
    public void testSendMail() {

        String to = "517462915@qq.com";
        String subject = "Appointment Request at KBC Bank - Inquiry and Address Confirmation";
        String content = "Subject: \n" +
                "\n" +
                "Dear KBC Bank Customer Service Team,\n" +
                "\n" +
                "I hope this email finds you well. My name is Zhou Jianyi, and I am writing to request an appointment with KBC Bank to discuss my banking needs and inquire about the documentation required for my visit. I am currently residing at Rue César Franck 76, 1050 Ixelles.\n" +
                "\n" +
                "I have been considering opening a new bank account, and I believe that KBC Bank may be a suitable option for my financial needs. However, I would like to schedule an appointment to discuss the various banking services and account options available, as well as to clarify any questions I may have.\n" +
                "\n" +
                "Could you please provide me with the following information:\n" +
                "\n" +
                "1. How can I schedule an appointment with a KBC Bank representative at the branch located closest to Rue César Franck 76, 1050 Ixelles?\n" +
                "2. What documents or identification should I bring with me to the appointment to facilitate the account opening process?\n" +
                "\n" +
                "Additionally, if possible, I would appreciate it if you could arrange the appointment date as close to today's date as feasible. This would be most convenient for me.\n" +
                "\n" +
                "Furthermore, I would like to inquire about the different banking services and account types offered by KBC Bank. If you could provide me with some information or recommendations on which banking solution might be better suited to my needs, it would be greatly appreciated.\n" +
                "\n" +
                "I look forward to your prompt response and assistance in scheduling an appointment at your earliest convenience. Thank you for your time and attention to my request.\n" +
                "\n" +
                "Please feel free to contact me at pp517462915@gmail.com or 0456 39 42 49 if you require any additional information or have any further instructions.\n" +
                "\n" +
                "Thank you for your assistance, and I am excited about the possibility of becoming a KBC Bank customer.\n" +
                "\n" +
                "Warm regards,\n" +
                "\n" +
                "Zhou Jianyi\n" +
                "pp517462915@gmail.com\n" +
                "0456 39 42 49";

        mailService.sendMail(to, subject, content);
    }
}
