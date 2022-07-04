//package com.quickmarket.order.util;
//
//import org.apache.commons.lang3.StringUtils;
//import org.csource.common.NameValuePair;
//import org.csource.fastdfs.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
///**
// * @program: quickmarket
// * @author: cx
// * @create: 2022-03-12 11:21
// * @description:
// **/
//@Component
//public class FastDFSClient implements CommandLineRunner {
//
//    @Value("${fastdfs.config.connect_timeout}")
//    private String CONF_FILENAME ;
//
//    @Value("${fastdfs.config.network_timeout}")
//    private String network_timeout;
//
//    @Value("${fastdfs.config.charset}")
//    private String charset;
//
//    @Value("${fastdfs.config.tracker_server}")
//    private String tracker_server;
//
//    private static StorageClient storageClient = null;
//
//    @Override
//    public void run(String... args) throws Exception {
//        ClientGlobal.init(CONF_FILENAME);
//        TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
//        TrackerServer trackerServer = trackerClient.getConnection();
//        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
//        storageClient = new StorageClient(trackerServer, storageServer);
//    }
//
//
//
//    /**
//     * 更新文件
//     * @param inputStream
//     * @param fileName
//     * @return
//     */
//    public String[] uploadFile(InputStream inputStream, String fileName) {
//        try {
//            // 文件的元数据
//            NameValuePair[] meta_list = new NameValuePair[2];
//            // 第一组元数据，文件的原始名称
//            meta_list[0] = new NameValuePair("file name", fileName);
//            // 第二组元数据
//            meta_list[1] = new NameValuePair("file length", inputStream.available()+"");
//            // 准备字节数组
//            byte[] file_buff = null;
//            if (inputStream != null) {
//                // 查看文件的长度
//                int len = inputStream.available();
//                System.out.println(len);
//                // 创建对应长度的字节数组
//                file_buff = new byte[len];
//                // 将输入流中的字节内容，读到字节数组中。
//                inputStream.read(file_buff);
//            }
//            // 上传文件。参数含义：要上传的文件的内容（使用字节数组传递），上传的文件的类型（扩展名），元数据
//            String[] fileids = storageClient.upload_file(file_buff, getFileExt(fileName), meta_list);
//            return fileids;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//
//    /**
//     * 获取文件后缀名（不带点）.
//     * @return 如："jpg" or "".
//     */
//    private  String getFileExt(String fileName) {
//        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
//            return "";
//        } else {
//            // 不带最后的点
//            return fileName.substring(fileName.lastIndexOf(".") + 1);
//        }
//    }
//
//    /**
//     * 文件下载
//     * @param groupName 卷名
//     * @param remoteFileName 文件名
//     * @return 返回一个流
//     */
//    public  InputStream downloadFile(String groupName, String remoteFileName) {
//        try {
//            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
//            InputStream inputStream = new ByteArrayInputStream(bytes);
//            return inputStream;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    public  NameValuePair[] getMetaDate(String groupName, String remoteFileName){
//        try{
//            NameValuePair[] nvp = storageClient.get_metadata(groupName, remoteFileName);
//            return nvp;
//        }catch(Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//}
