package blockchain.controller;


import blockchain.entity.Organization;
import blockchain.entity.Result;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Controller
public class logInController {

    @Autowired
    private OrganizationServiceImpl organizationService;
//    跳转到这两个页面
//    @GetMapping("/login")
//    public String login()
//    {
//        return "home/login";
//    }
//
//    @GetMapping("/register")
//    public String register()
//    {
//        return "home/register";
//    }

//    注册新用户
    @RequestMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("orgType")String orgType,@RequestParam("orgName")String orgName,@RequestParam("password")String password)
    {
        int creditLimit = 0;
        if(orgType.equals("B")) {
            creditLimit = 100000;
        }
        String orgId = UUID.randomUUID().toString();//生成orgId
        try{
            int store = 1000;
            Boolean result = organizationService.addOrganization(orgId,orgName,password,creditLimit,orgType, store);
            if(result)
            {
                System.out.println("username:"+ orgName + ", password:" + password);
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                attributes.getRequest().getSession().setAttribute("user", new Organization(orgName,password,orgType)); //将登陆用户信息存入到session域对象中

                return new Result(true,"success","200");
                // TODO: 2019/7/4 添加session  --> 7.4 处理
            }
            else return new Result(false,"注册失败","404");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,"注册失败","404");
        }
    }
    
    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("orgName")String orgName,@RequestParam("password")String password)
    {
        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        if(orgID==null)
        {
            return new Result(false,"用户不存在","404");
        }
        String orgType = organizationService.queryOrganizationByOrgID(orgID).getOrgType();
        int result = organizationService.checkPasswordByOrgID(password,orgID);
        if(result==-1)
        {
            return new Result(false,"用户不存在","404");
        }
        else if(result==2)
        {
            return new Result(false,"机构类型不正确","404");
        }
        else if(result==0)
        {
            return new Result(false,"密码错误","404");
        }
        else if(result==1)
        {
//             TODO: 2019/7/4 session处理  -->7.4 16:08 处理
            System.out.println("username:"+ orgName + ", password:" + password);
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            attributes.getRequest().getSession().setAttribute("user", new Organization(orgName,password,orgType)); //将登陆用户信息存入到session域对象中

            return new Result(true,orgType,"200");
        }
        else{
            return new Result(false,"未知错误","404");
        }
    }
}
