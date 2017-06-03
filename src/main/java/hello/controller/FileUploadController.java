package hello.controller;

/**
 * Created by dongxie on 2017/5/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.List;

@Controller
public class FileUploadController {

    private static Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "upload failed" + e.getMessage();
            }
            return "upload success";
        } else {
            return "upload failed,because file is empty";
        }
    }

    @RequestMapping(value = "/get_request", method = RequestMethod.GET)
    @ResponseBody
    public String getRequest(HttpServletRequest request) {
        log.info(request.getQueryString());
        log.info("--------request.getHeader()--------");
        Enumeration<String> em = request.getHeaderNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getHeader(name);
            log.info(name + "=" + value);
        }
        return "Hello World!";

    }


    @RequestMapping(value = "/multiUpload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => "
                            + e.getMessage();

                }
            } else {
                return "You failed to upload " + i
                        + " because the file was empty.";
            }

        }
        return "upload successful";

    }

    @RequestMapping(value = "/multipleSave", method = RequestMethod.POST)
    @ResponseBody
    public String multipleSave(@RequestParam("file") MultipartFile[] files) {
        BufferedOutputStream stream = null;
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.flush();
                    stream.close();

                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + file.getOriginalFilename() + " => "
                            + e.getMessage();
                } finally {
                    if (null != stream) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return "You failed to upload " + file.getOriginalFilename()
                        + " because the file was empty.";
            }

        }
        return "upload successful";

    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse res) {
        String fileName = "spring_test.jpg";
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
            while (bis.read(buff) != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
