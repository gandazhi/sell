package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.common.UploadServiceResponse;
import com.gandazhi.sell.service.IFileService;
import com.gandazhi.sell.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 上传文件controller
 * @author gandazhi
 * @create 2017-11-26 下午1:56
 **/
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private IFileService iFileService;

    @PostMapping("/image")
    public UploadServiceResponse changeDemo(@RequestParam(value = "file_upload", required = false) MultipartFile file, HttpServletRequest request,
                                            Map<String, Object> map){

        if (file == null || file.isEmpty()){
            return UploadServiceResponse.createByError("file不能为空");
        }

        // TODO 从session中获取openId
        String path = request.getSession().getServletContext().getRealPath("upload");
        String openId = "123456";

        String targetFileName = iFileService.upload(file, path, openId);
        if (targetFileName.equals("error")){
            return UploadServiceResponse.createByError("上传图片失败");
        }else if (targetFileName.equals("error1")){
            return UploadServiceResponse.createByError("上传的文件类型不是图片");
        }

        String url = PropertiesUtil.getProperties("qiniu.url") + targetFileName;
        return UploadServiceResponse.createBySuccess(url);
    }
}
