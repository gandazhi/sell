package com.gandazhi.sell.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author gandazhi
 * @create 2017-11-24 下午5:12
 **/
public interface IFileService {

    String upload(MultipartFile file, String path, String openId);
}
