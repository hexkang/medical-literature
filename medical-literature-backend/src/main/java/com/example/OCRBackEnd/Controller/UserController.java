package com.example.OCRBackEnd.Controller;

import com.example.OCRBackEnd.POJO.MailRequest;
import com.example.OCRBackEnd.POJO.Thesis;
import com.example.OCRBackEnd.POJO.User;
import com.example.OCRBackEnd.Service.impl.EvalServiceImpl;
import com.example.OCRBackEnd.Service.impl.MailServiceImpl;
import com.example.OCRBackEnd.Service.impl.ThesisServiceImpl;
import com.example.OCRBackEnd.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.json.*;


@RestController
public class UserController {

    @Autowired
    private MailServiceImpl sendMailService;

    private final UserServiceImpl userService;
    private final ThesisServiceImpl thesisService;
    private final EvalServiceImpl evalService;

    public UserController(UserServiceImpl userService, ThesisServiceImpl thesisService, EvalServiceImpl evalService) {
        this.userService = userService;
        this.thesisService = thesisService;
        this.evalService = evalService;
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String LoginResponse(@RequestBody Map<String, Object> params){
        try {
            String userName = (String) params.get("username");
            String password = (String) params.get("password");
            if(userName == null || password == null){
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "Login Failure: Request Message Error")
                        ).toString();
            }
            User LoginUser = userService.GetUserByNameAndPassword(userName, password);
            if (LoginUser != null) {
                return new JSONObject().
                        put("code", 0).
                        put("data",
                                new JSONObject().
                                        put("token", userName)
                        ).toString();
            } else {
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "Login Failure: User Does Not Exist")
                        ).toString();
            }
        } catch (Exception e){
            return new JSONObject().
                    put("code", 403).
                    put("data",
                            new JSONObject().
                                    put("token", "Login Failure: Exception Occurs")
                    ).toString();
        }
    }

    @RequestMapping(value =  "/api/changePass")
    public String ChangePassResponse(@RequestBody Map<String, Object> params){
        int returnCode = 403;
        try{
            String userName = params.get("username").toString();
            String oldPassword = params.get("oldPass").toString();
            String newPassword = params.get("newPass").toString();
            System.out.println("oldPass = " + oldPassword + "\nnewPass = " + newPassword);
            User user = userService.GetUserByNameAndPassword(userName, oldPassword);
            if(user != null){
                user.setPassword(newPassword);
                System.out.println(user);
                userService.UpdateUser(user);
                returnCode = 0;
            }
            return new JSONObject().put("code", returnCode).toString();
        } catch (Exception e){
            return new JSONObject().put("code", returnCode).toString();
        }
    }

    @RequestMapping(value = "/api/findPass")
    public String FindPassResponse(@RequestBody Map<String, Object> params){
        try{
            String userName = params.get("username").toString();
            String userEmail = params.get("email").toString();
            User user = userService.GetUserByName(userName);
            System.out.println(user);
            if(user.getUserEmail().equals(userEmail)){
                MailRequest mailRequest = new MailRequest();
                mailRequest.setSubject("MedicalLiteratureViewer Password Found");
                mailRequest.setText("您的原密码是：" + user.getPassword());
                mailRequest.setFilePath(""); // 没有附件
                mailRequest.setSendTo(userEmail);
                sendMailService.sendSimpleMail(mailRequest);
                return new JSONObject().put("code", 0).toString();
            }
            else
                return new JSONObject().put("code", 403).toString();
        } catch (Exception e){
            return new JSONObject().put("code", 403).toString();
        }
    }

    @RequestMapping(value = "/api/signup")
    public String SignUpResponse(@RequestBody Map<String, Object> params){
        try {
            String userName = (String) params.get("username");
            String password = (String) params.get("password");
            String userEmail = (String) params.get("email");
            if(userName == null || password == null || userEmail == null){
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "Signup Failure: Request Message Error")
                        ).toString();
            }
            User existUser = userService.GetUserByName(userName);
            if (existUser == null) {
                User newUser = new User(userName, password, userEmail);
                boolean isSuccess = userService.CreateUser(newUser);
                if (isSuccess) {
                    return new JSONObject().
                            put("code", 0).
                            put("data",
                                    new JSONObject().
                                            put("token", "Signup Success")
                            ).toString();
                } else {
                    return new JSONObject().
                            put("code", 403).
                            put("data",
                                    new JSONObject().
                                            put("token", "Signup Failure: Database Manipulation Failure")
                            ).toString();
                }
            } else {
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "Signup Failure: User Already Exists")
                        ).toString();
            }
        } catch (Exception e){
            return new JSONObject().
                    put("code", 403).
                    put("data",
                            new JSONObject().
                                    put("token", "Signup Failure: Exception Occurs")
                    ).toString();
        }
    }


    @RequestMapping(value = "/api/emailCheck")
    public String EmailCheckResponse(@RequestBody Map<String, Object> params){
        try {
            String SendTo = (String) params.get("email");
            if(SendTo == null){
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "EmailCheck Failure: Request Message Error")
                        ).toString();
            }
            Random randObj = new Random();
            int checkCode = 100000 + randObj.nextInt(900000);
            MailRequest mailRequest = new MailRequest();
            mailRequest.setSubject("MedicalLiteratureViewer CAPTCHA");
            mailRequest.setText("您的验证码是：" + checkCode);
            mailRequest.setFilePath(""); // 没有附件
            mailRequest.setSendTo(SendTo);
            sendMailService.sendSimpleMail(mailRequest);
            return new JSONObject().
                    put("code", 0).
                    put("data",
                            new JSONObject().
                                    put("captcha", checkCode)
                    ).toString();
        }catch (Exception e){
            return new JSONObject().
                    put("code", 403).
                    put("data",
                            new JSONObject().
                                    put("captcha", "")
                    ).toString();
        }
    }

    @RequestMapping(value = "/api/getLikeFile")
    public String UserLikeResponse(@RequestBody Map<String, Object> params){
        try {
            String userName = (String) params.get("username");
            int page = (int) params.get("page");
            if(userName == null || page <= 0){
                return new JSONObject().
                        put("code", 403).
                        put("data",
                                new JSONObject().
                                        put("token", "GetLikeFile Failure: Request Message Error")
                        ).toString();
            }
            List<Integer> likedList = evalService.GetLikedIdsByName(userName);
            List<Thesis> thesisList = new ArrayList<>();
            for (int thesisId : likedList) {
                thesisList.add(thesisService.GetThesisById(thesisId));
            }
            return ThesisController.GetThesisPage(thesisList, page, userName);
        } catch (Exception e){
            return new JSONObject().
                    put("code", 403).
                    put("data",
                            new JSONObject().
                                    put("token", "GetLikeFile Failure: Exception Occurs")
                    ).toString();
        }
    }
}
