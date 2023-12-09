package com.example.OCRBackEnd.Controller;

import cn.hutool.core.util.NumberUtil;
import com.example.OCRBackEnd.POJO.Eval;
import com.example.OCRBackEnd.POJO.Section;
import com.example.OCRBackEnd.POJO.Thesis;
import com.example.OCRBackEnd.Service.impl.*;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static cn.hutool.core.util.NumberUtil.min;
import static cn.hutool.core.util.NumberUtil.roundStr;
import static java.lang.Math.max;

@RestController
public class ThesisController {
    private final ThesisServiceImpl thesisService;
    private final UserServiceImpl userService;
    private final KeywordServiceImpl keywordService;
    private static EvalServiceImpl evalService;
    private static SectionServiceImpl sectionService;
    private String curUploader;

    public ThesisController(ThesisServiceImpl thesisService, UserServiceImpl userService, KeywordServiceImpl keywordService, EvalServiceImpl evalService, SectionServiceImpl sectionService){
        this.thesisService = thesisService;
        this.userService = userService;
        this.keywordService = keywordService;
        this.evalService = evalService;
        this.sectionService = sectionService;
        this.curUploader = "";
    }

    @RequestMapping(value = "/api/like")
    public String LikeResponse(@RequestBody Map<String, Object> params){
        try {
            int thesisId = Integer.parseInt(params.get("id").toString());
            int eValue = Integer.parseInt(params.get("like").toString());
            String userName = params.get("username").toString();
            if(thesisId <= 0 || (eValue != 1 && eValue != -1 && eValue != 2 && eValue != -2) || userName == null){
                return new JSONObject().put("code", 403).toString();
            }
            Eval existEval = evalService.GetEvalByNameAndId(userName, thesisId);
            boolean isSuccess = false;
            if (existEval == null) {
                Eval newEval = new Eval(userName, thesisId, eValue);
                isSuccess = evalService.CreateEval(newEval);
            } else {
                existEval.seteValue(existEval.geteValue() + eValue);
                isSuccess = evalService.UpdateEval(existEval);
            }
            if (isSuccess) {
                return new JSONObject().put("code", 0).toString();
            } else
                return new JSONObject().put("code", 403).toString();
        } catch (Exception e){
            return new JSONObject().put("code", 403).toString();
        }
    }

    @RequestMapping(value = "/api/getFile")
    public String GetThesisResponse(@RequestBody Map<String, Object> params){
        try {
            Object __keyword = params.get("keyWord");
            String keyword = (__keyword == null ? "" : (String) __keyword);
            int page = Integer.parseInt(params.get("page").toString());
            String userName = params.get("username").toString();
            List<Thesis> thesisList = thesisService.GetThesisByKeyword(keyword);
            if(!keyword.equals("")){
                keywordService.UpdateKeywordByName(keyword);
            }
            return GetThesisPage(thesisList, page, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new JSONObject().put("code", 403).put("data", new JSONObject().put("token", "GetFile Failure: Exception Occurs")).toString();
        }
    }

    public static String GetThesisPage(List<Thesis> thesisList, int page, String userName){
        if(thesisList == null || userName == null || page <= 0){
            return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "GetFile Failure: Input Message Error")
                        ).toString();
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", thesisList.size());
            if(thesisList.size() == 0){
                return new JSONObject().put("code", 403).toString();
            }
            int topEval = 0, topRel = 0, topRate = 0;
            for(Thesis thesis: thesisList){
                if(thesis.getThesisTitle().equals("新型冠状病毒肺炎疫情下儿科急诊疾病谱的变化及分析") && thesisList.size() > 1){
                    continue;
                }
                topEval = max(topEval, thesis.getThesisEval());
                topRel = max(topRel, thesis.getThesisRelevance());
                topRate = max(topRate, thesis.getThesisRate());
            }
            List<JSONObject> thesisJsonList = new ArrayList<>();
            for(int cnt = 0; cnt < thesisList.size(); ++cnt){
                if(cnt >= (page - 1) * 5 && cnt < page * 5){
                    Thesis thesis = thesisList.get(cnt);
                    Eval eval = evalService.GetEvalByNameAndId(userName, thesis.getThesisId());
                    int likeVal = eval == null ? 0 : eval.geteValue();
                    System.out.println(thesis.getThesisTitle() + " likeVal = " + likeVal);
                    int evalScore = topEval == 0 ? 10 : (int) ((double) thesis.getThesisEval() / (double) topEval * 10);
                    int relevanceScore = topRel == 0 ? 0 : (int) ((double) thesis.getThesisRelevance() / (double) topRel * 60);
                    int rateScore = topRate == 0 ? 0 : (int) ((double) thesis.getThesisRate() / (double) topRate * 30);

                    JSONObject thesisObject = new JSONObject();
                    thesisObject.put("id", thesis.getThesisId());
                    thesisObject.put("title", thesis.getThesisTitle());
                    thesisObject.put("authors", thesis.getThesisAuthors());
                    thesisObject.put("uploader", thesis.getThesisUploader());
                    thesisObject.put("content", thesis.getThesisExtract().substring(0, min(102, thesis.getThesisExtract().length() - 1)) + "...");
                    thesisObject.put("like", likeVal);
                    thesisObject.put("score", new JSONObject().put("like", evalScore).put("relevance", relevanceScore).put("rate", rateScore));
                    thesisJsonList.add(thesisObject);
                }
                else if(cnt >= page * 5)
                    break;
            }
            return new JSONObject().put("code", 0).put("data", jsonObject.put("thesisList", thesisJsonList)).toString();
        }
    }

    @Value("${filePath.root}")
    private String filePathRoot;

    @Value("${msgPath.root}")
    private String msgPathRoot;

    @Value("${webPath.root}")
    private String webPathRoot;

    @RequestMapping("/api/download")
    public void DownloadResponse(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            int thesisId = Integer.parseInt(params.get("id").toString());
            Thesis srcThesis = thesisService.GetThesisById(thesisId);
            thesisService.UpdateThesisRateById(thesisId);
            File file = new File(filePathRoot + "/" + srcThesis.getFileName());
            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("/api/getUrl")
    public String GetUrlResponse(@RequestBody Map<String, Object> params){
        try{
            int thesisId = Integer.parseInt(params.get("id").toString());
            Thesis thesis = thesisService.GetThesisById(thesisId);
            thesisService.UpdateThesisRateById(thesisId);
            if(thesis == null)
                return new JSONObject().put("code", 403).put("data", new JSONObject().put("url", "").put("fileName", "")).toString();
            else {
                System.out.println(webPathRoot);
                return new JSONObject().put("code", 0).put("data",
                        new JSONObject().put("url", webPathRoot + "/file/" + thesis.getFileName())
                                .put("fileName", thesis.getFileName())).toString();
            }
        } catch (Exception e){
            return new JSONObject().put("code", 403).put("data", new JSONObject().put("url", "").put("fileName", "")).toString();
        }
    }


    @RequestMapping(value = "/api/lock")
    public String TestResponse(@RequestBody Map<String,Object> params){
        try{
            String uploader = (String) params.get("token");
    //        String token = (String) params.get("token");
            String action = (String) params.get("msg");
            System.out.println("token: " + uploader + "\n msg: " + action);
            if(action.equals("start")){
                if(curUploader.equals("")){
                    curUploader = uploader;
                    return new JSONObject().put("code", 0).toString();
                }
                else{
                    return new JSONObject().put("code", 403).toString();
                }
            }
            else if(action.equals("end")){
                if(!curUploader.equals(uploader)){
                    return new JSONObject().put("code", 403).toString();
                }
                else{
                    curUploader = "";
                    return new JSONObject().put("code", 0).toString();
                }
            }else{
                return new JSONObject().put("code", 0).toString();
            }
        } catch (Exception e){
            return new JSONObject().put("code", 403).toString();
        }
    }

    @PostMapping(value = "/api/uploadFile")
    public String UploadResponse(@RequestParam(required = false, value = "file") MultipartFile multipartFile){
//    public String UploadResonse(@RequestBody Map<String, Object> params){
//        MultipartFile[] multipartFiles = (MultipartFile[]) params.get("files");
            boolean ini = false;
            try {
                multipartFile.transferTo(new File(new File(filePathRoot), multipartFile.getOriginalFilename()));
                Thesis thesis = new Thesis();
                thesis.setFileName(multipartFile.getOriginalFilename());
                String msgFileName = thesis.getFileName().substring(0, thesis.getFileName().length() - 4) + ".txt";
                List<String> fileMessage = Files.readAllLines(Paths.get(msgPathRoot + "/" + msgFileName), StandardCharsets.UTF_8);
                thesis.setThesisTitle(fileMessage.get(0));
                thesis.setThesisAuthors(fileMessage.get(1));
                thesis.setThesisExtract(fileMessage.get(2));
                thesis.setThesisUploader(curUploader);
                System.out.println("curUploader = " + curUploader);
                thesis.setFilePath(filePathRoot + "/" + thesis.getFileName());
                if(ini)
                    thesisService.CreateThesis(thesis);
                int curThesisId = thesisService.GetThesisIdByFileName(thesis.getFileName());//thesisService.GetThesisIdByFileName(thesis.getFileName());
                int sectionPage = 1, sectionId = 1;
                boolean sectionType = false;
                for (int i = 2; i < fileMessage.size(); ++i) {
                    if(fileMessage.get(i).length() == 0)
                        continue;
                    if (fileMessage.get(i).charAt(0) == '$') {
                        sectionPage++;
                        sectionId = 0;
                    } else if(fileMessage.get(i).charAt(0) == '#'){
                        sectionPage = 1;
                        sectionId = 0;
                        sectionType = true;
                    } else {
                        sectionId++;
                        if(ini)
                            sectionService.CreateSection(new Section(curThesisId, sectionPage, sectionId, sectionType, fileMessage.get(i).replaceAll("(Cid:[0-9]+)", "")));
                    }
                }
                return new JSONObject().put("code", 0).put("data", new JSONObject().put("token", "Upload Success")).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return new JSONObject().put("code", 0).put("data", new JSONObject().put("token", "Upload Failure")).toString();
            }
    }

    @RequestMapping("/api/getDetail")
    public String GetDetailResponse(@RequestBody Map<String, Object> params){
        List<JSONObject> jsonObjectList = new ArrayList<>();
        try {
            int thesisId = Integer.parseInt(params.get("id").toString());
            String keyword = params.get("keyWord").toString();
            Thesis thesis = thesisService.GetThesisById(thesisId);
            List<Section> sectionList = sectionService.GetSectionsByThesisAndKey(thesisId, keyword);
            if(sectionList.size() == 0){
                return new JSONObject().put("code", 403).toString();
            }
            Collections.sort(sectionList, new SectionComparator());
            int curPage = -1;
            List<String> contentList = new ArrayList<>();
            for(int i = 0; i < sectionList.size(); ++i){
                Section section = sectionList.get(i);
                System.out.println(section.getSectionContent());
                if(curPage != section.getSectionPage()){
                    if(curPage != -1){
                        jsonObjectList.add(new JSONObject().put("page", curPage).put("content", contentList));
                        contentList.clear();
                    }
                    curPage = section.getSectionPage();
                }
                contentList.add(section.getSectionContent());
            }
            if(contentList != null){
                jsonObjectList.add(new JSONObject().put("page", curPage).put("content", contentList));
                contentList.clear();
            }
            return new JSONObject().put("code", 0).put("data", new JSONObject().put("url", webPathRoot + "/file/" + thesis.getFileName()).put("keyPage", jsonObjectList)).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new JSONObject().put("code", 403).put("data", new JSONObject().put("url", "").put("keyPage", jsonObjectList)).toString();
        }
    }
}

class SectionComparator implements Comparator<Section>{

    @Override
    public int compare(Section section1, Section section2) {
        if(section1.getSectionPage() != section2.getSectionPage())
            return section1.getSectionPage() - section2.getSectionPage();
        if(section1.isSectionType() != section2.isSectionType())
            return section1.isSectionType() == true ? -1 : 1;
        return section1.getSectionId() - section2.getSectionId();
    }
}
