package com.watt.mvc.controller;

import com.watt.util.FileUtils;
import com.watt.util.NLPUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class CorpusController {
    /**
     * 预处理数据
     */
    @RequestMapping("/loadCorpus")
    public String loadCorpus(@RequestParam(name = "path") String path) {
        List<File> files = FileUtils.listFiles(path);
        files.forEach(file -> {
            try {
                NLPUtils.textPreprocessing(file, file.getParent() + "/dump/" + file.getName() + ".seg.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return "success";
    }
}
