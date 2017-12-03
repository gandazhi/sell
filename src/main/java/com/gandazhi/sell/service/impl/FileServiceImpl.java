package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.Const;
import com.gandazhi.sell.service.IFileService;
import com.gandazhi.sell.util.QiniuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author gandazhi
 * @create 2017-11-24 下午5:13
 **/
@Slf4j
@Service("iFileService")
public class FileServiceImpl implements IFileService{

    @Override
    public String upload(MultipartFile file, String path, String openId) {

        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String[] arrayFileExtensionName = {"jpg", "jpeg", "gif", "png", "svg"};
        String uploadFileName =new Date().getTime() + "_" + openId + "." + fileExtensionName;

        boolean isPicture = false;
        for (int i = 0; i < arrayFileExtensionName.length; i++){
            isPicture = fileExtensionName.equals(arrayFileExtensionName[i]);
            if (isPicture){
                break;
            }
        }

        if (!isPicture){
            return "error1"; //n上传的文件不是图片
        }
        log.info("开始上传图片，文件名{}，上传路径{}，新文件名{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            String localPath = path + "/" + uploadFileName;

            //将上传的文件放到七牛云中
            boolean upload = QiniuUtil.uploadForQiniu(Const.Zone.EAST_CHINA, localPath, uploadFileName);
            if (!upload) {
                return "error";
            }
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传图片失败", e);
            return null;
        }

        return targetFile.getName();
    }
}
