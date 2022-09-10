package com.longbig.multifunction.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileUtils {

    /**
     * 读取文件后以数组形式存放
     *
     * @param inputFile
     * @return
     * @throws Exception
     */
    public static List<String> readFileToStringList(String inputFile) {
        List<String> stringList = Lists.newArrayList();
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                log.info("readFileToStringList:{}", line);
                stringList.add(line);
                line = bufferedReader.readLine();
            }
            fileReader.close();
        } catch (IOException e) {
            log.error("读取输入文件失败：{}", inputFile, e);
        }
        return stringList;
    }

    /**
     * 将二维数组写入到文件中
     *
     * @param strings
     * @param outputFile
     */
    public static void writeStringListToFile(String[][] strings, String outputFile) {
        try {
            File file = new File(outputFile);
            FileWriter out = new FileWriter(file);
            //将数组中的数据写入到文件中，以空格分开
            for (String[] stringList : strings) {
                for (String string : stringList) {
                    if (Objects.nonNull(string)) {
                        out.write(string + " ");
                    }
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            log.error("输出路径有问题：{}", outputFile);
            e.printStackTrace();
        }

    }

    /**
     * 读取文件到缓存
     *
     * @param inputFile
     */
    public static BufferedReader readFile(String inputFile) {
        try {
            File file = new File(inputFile);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            System.out.println(reader.getEncoding());
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将缓存写入文件
     *
     * @param outputFile
     * @param bufferedReader
     */
    public static void writeFile(String outputFile, BufferedReader bufferedReader) {
        if (Objects.isNull(bufferedReader)) {
            return;
        }
        try {
            File writeName = new File(outputFile);
            writeName.createNewFile();
            FileWriter writer = new FileWriter(writeName);
            String line = "";
            while (null != line) {
                line = bufferedReader.readLine();
                BufferedWriter out = new BufferedWriter(writer);
                if (Objects.nonNull(line)) {
                    out.write("\"" + line + "\"" + ",");
                    out.flush();
                }
            }
        } catch (IOException e) {
            log.error("输出路径不存在：{}", outputFile);
            e.printStackTrace();
        }
    }
}
