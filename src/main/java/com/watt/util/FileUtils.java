package com.watt.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {
    /**
     * 清除某个目录下所有的文件
     *
     * @param path 目标path
     */
    public static void clearPath(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || file.length() == 0) {
                return;
            }
            for (File childFile : childFiles) {
                childFile.delete();
            }
        } else {
            file.delete();
        }
    }

    /**
     * 将文件目录下所有文件全部罗列出来进行
     *
     * @param path 父文件路径
     * @return 所有的文件列表
     */
    public static List<File> listFiles(String path) {
        List<File> result = new ArrayList<>();
        File file = new File(path);
        for (File one : Objects.requireNonNull(file.listFiles())) {
            if (one.isDirectory()) {
                result.addAll(listFiles(one.getPath()));
            } else {
                result.add(one);
            }
        }
        return result;
    }

    /**
     * 给定文件返回读取方法
     *
     * @param file 目标文件
     * @return 大文件读取的文件流
     */
    public static BufferedReader getFileReader(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), Charset.forName("GBK")));
    }

    /**
     * 给定文件返回读取方法
     *
     * @param file 目标文件
     * @return 大文件读取的文件流
     */
    public static BufferedReader getFileReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset));
    }
    /**
     * 给定文件返回读取方法
     *
     * @param file 目标文件
     * @return 大文件读取的文件流
     */
    public static BufferedReader getFileReader(String file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
    }

    public static String readLine(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    /**
     * @throws FileNotFoundException 文件找不到
     */
    public static BufferedWriter getBufferedWriter(String fileName) throws IOException {
        File createFile = new File(fileName);
        if (!createFile.exists()) {
            createFile.createNewFile();
        }
        return new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(createFile))));
    }
    public static void main(String[] args){
        List<File>  files = listFiles("/root/data/corpus/");
        files.forEach(file -> {
            System.out.println(file.getParent()+","+file.getPath());
        });
    }
}
