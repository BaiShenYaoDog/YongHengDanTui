package cn.ChengZhiYa.Bot;

import cn.mrcsh.qhbotspringbootstarter.annotation.EventHandler;
import cn.mrcsh.qhbotspringbootstarter.annotation.ZFBot;
import cn.mrcsh.qhbotspringbootstarter.events.BotLoginSuccessfulEvent;
import cn.mrcsh.qhbotspringbootstarter.events.GroupAtMessageEvent;
import cn.mrcsh.qhbotspringbootstarter.ws.message.send.message.InitiativeMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.FormatUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static cn.ChengZhiYa.Bot.Util.*;
import static cn.mrcsh.qhbotspringbootstarter.utils.MediaUtil.getGroupMedia;

@ZFBot
@Component
@Slf4j
public final class Listeners {

    @EventHandler
    public void Message(GroupAtMessageEvent event) throws InterruptedException {
        System.out.println("[日志]接受到消息 > " + event.getMessage().getContent());
        if (event.getMessage().getContent().equals(" 菜单")) {
            try {
                File BackgroundImgFile = new File("./Assets/功能列表底图.png");
                BufferedImage Img = ImageIO.read(BackgroundImgFile);
                Graphics2D graphics2D = Img.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);
                graphics2D.setColor(new Color(60,144,255));
                graphics2D.setFont(new Font("微软雅黑", Font.BOLD, 27));
                graphics2D.drawString(RandomText(),50,440);
                graphics2D.drawString(getDate() + " " + getDay() + " " + getTime(),50,480);
                graphics2D.dispose();
                ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File("./Temp/功能列表底图.png"));
                Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName("png");
                if (imageWriterIterator.hasNext()) {
                    ImageWriter imageWriter = imageWriterIterator.next();
                    imageWriter.setOutput(imageOutputStream);
                    imageWriter.write(Img);
                }
                imageOutputStream.close();

                InitiativeMessage message = new InitiativeMessage();
                message.setMsg_type(7);
                message.setMedia(JSONObject.parseObject(getGroupMedia(event.getMessage(),"http://mhdf.love:5842/images/功能列表底图.png")));

                event.send(message);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getMessage().getContent().equals(" 运行状态") || event.getMessage().getContent().equals(" 机器人状态")) {
            SystemInfo systemInfo = new SystemInfo();
            int Mem = Integer.parseInt(new DecimalFormat("#").format(Double.parseDouble(FormatUtil.formatBytes((systemInfo.getHardware().getMemory().getTotal()-systemInfo.getHardware().getMemory().getAvailable())).replaceAll(" GiB","").replaceAll(" MiB",""))/Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()).replaceAll(" GiB","").replaceAll(" MiB","")) * 100));
            int PageMem = Integer.parseInt(new DecimalFormat("#").format(100 - Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()-systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()).replaceAll(" GiB","").replaceAll(" MiB",""))/Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()).replaceAll(" GiB","").replaceAll(" MiB","")) * 100));
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            TimeUnit.SECONDS.sleep(1);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                    - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                    - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
            int CPUUsage = Math.round(Float.parseFloat(new DecimalFormat("#.##").format(100 - idle * 1.0 / totalCpu * 100)));
            if (CPUUsage > 100) {
                CPUUsage = 100;
            }
            if (Mem > 100) {
                Mem = 100;
            }
            if (PageMem > 100) {
                PageMem = 100;
            }
            try {
                File BackgroundImgFile = new File("./Assets/运行状态底图.png");
                BufferedImage Img = ImageIO.read(BackgroundImgFile);
                Graphics2D graphics2D = Img.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);

                graphics2D.setColor(new Color(0,0,0));
                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                graphics2D.drawString("永恒单推机 | " + getHitokoto(),300,155);
                graphics2D.drawString(getDate() + " " + getDay() + " " + getTime(),300,175);

                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 25));
                graphics2D.drawString("CPU",120,360);
                graphics2D.drawString("物理内存",90,408);
                graphics2D.drawString("虚拟内存",90,458);

                graphics2D.setColor(new Color(237,90,101));
                graphics2D.fillRect(201,336,CPUUsage * 9, 28);
                graphics2D.fillRect(201,386,Mem * 9, 28);
                graphics2D.fillRect(201,436,PageMem * 9, 28);

                graphics2D.setColor(new Color(92,34,35));
                graphics2D.drawString(CPUUsage + "%",621,359);
                graphics2D.drawString(Mem + "%",621,409);
                graphics2D.drawString(PageMem + "%",621,459);

                graphics2D.setColor(new Color(237,90,101));
                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 18));

                graphics2D.drawString("CPU:",130,500);
                graphics2D.drawString("CPU频率: 5.0GHz", 130,520);

                graphics2D.drawString("物理内存:",480,500);
                graphics2D.drawString("总内存: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()), 480,520);
                graphics2D.drawString("内存占用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()-systemInfo.getHardware().getMemory().getAvailable()), 480,540);
                graphics2D.drawString("内存可用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getAvailable()), 480,560);

                graphics2D.drawString("虚拟内存:",930,500);
                graphics2D.drawString("总内存: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()), 930,520);
                graphics2D.drawString("内存占用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()), 930,540);
                graphics2D.drawString("内存可用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()-systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()), 930,560);

                graphics2D.dispose();
                ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File("./Temp/运行状态底图.png"));
                Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName("png");
                if (imageWriterIterator.hasNext()) {
                    ImageWriter imageWriter = imageWriterIterator.next();
                    imageWriter.setOutput(imageOutputStream);
                    imageWriter.write(Img);
                }
                imageOutputStream.close();

                InitiativeMessage message = new InitiativeMessage();
                message.setMsg_type(7);
                message.setMedia(JSONObject.parseObject(getGroupMedia(event.getMessage(),"http://mhdf.love:5842/images/运行状态底图.png")));

                event.send(message);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getMessage().getContent().equals(" 一言")) {
            InitiativeMessage message = new InitiativeMessage();
            message.setContent("\n永恒单推机|一言\n" + getHitokoto());
            event.send(message);
        }
    }

    @EventHandler
    public void BotLoginSuccess(BotLoginSuccessfulEvent event) {
        System.out.println("[日志]" + event.getBotInfo().getUser().getUsername() + " 登录成功!");
    }
}
