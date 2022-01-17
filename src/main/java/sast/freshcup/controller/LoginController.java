package sast.freshcup.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sast.freshcup.service.RedisService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * @author: 風楪fy
 * @create: 2022-01-16 20:33
 **/
@RestController
public class LoginController {
    public static final String LOGIN_VALIDATE_CODE = "VAL_CODE:";

    @Autowired
    private DefaultKaptcha kaptchaProducer;
    @Autowired
    private RedisService redisService;


    /**
     * 返回验证码图片，并将验证码存入Redis
     *
     * @param response
     */
    @GetMapping("/getValidateCode")
    public void getImgValidateCode(HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.addHeader("CAPTCHA", uuid);
        response.setContentType("image/jpeg");

        String capText = kaptchaProducer.createText();
        BufferedImage bi = kaptchaProducer.createImage(capText);

        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        redisService.set(LOGIN_VALIDATE_CODE + uuid, capText, 60 * 5);
    }


}
