package com.example.icsdownload.Client;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.File;





public class downloadService {
    public static void main(String[] args) {
        // ICS文件的URL地址
        String url = "https://calendars.icloud.com/holiday/cn_zh.ics";

        // 保存TXT文件的目录和路径
        String outputDirectoryPath = "src/main/java/com/example/icsdownload/output";
        String outputFilePath = outputDirectoryPath + "/output.txt";

        // 如果输出目录不存在，则创建
        createOutputDirectory(outputDirectoryPath);

        // 初始化WebClient
        WebClient webClient = WebClient.create("https://calendars.icloud.com");

        // 下载ICS文件内容
        String icsContent = downloadIcsFile(webClient, url);

        // 将ICS文件内容转换为纯文本并保存为TXT文件
        convertIcsToTxtFile(icsContent, outputFilePath);

        System.out.println("ICS内容已转换并保存至: " + outputFilePath);
    }

    // 使用WebClient下载.ics文件的方法
    private static String downloadIcsFile(WebClient webClient, String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 为了简化使用阻塞操作
    }

    // 将.ics内容转换为纯文本并保存为.txt文件的方法
    private static void convertIcsToTxtFile(String icsContent, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // 直接将每一行写入文件
            for (String line : icsContent.split("\n")) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    // 如果输出目录不存在，则创建目录的方法
    private static void createOutputDirectory(String outputDirectoryPath) {
        File directory = new File(outputDirectoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("输出目录已创建: " + outputDirectoryPath);
            } else {
                System.err.println("创建输出目录失败: " + outputDirectoryPath);
            }
        }
    }
}
