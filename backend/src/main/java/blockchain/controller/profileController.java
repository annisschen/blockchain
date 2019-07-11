package blockchain.controller;

import blockchain.entity.Organization;
import blockchain.service.Impl.OrganizationServiceImpl;
import blockchain.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//    个人主页
@RestController
public class profileController {

    @Autowired
    private OrganizationServiceImpl organizationService;

    @RequestMapping("/profile")
    @ResponseBody
    public Organization profile(@RequestParam("orgName") String orgName)
    {
        // TODO: 2019/7/4 可能需要新建数据库的表来记录
        String orgID = organizationService.queryOrgIDByOrgName(orgName);
        return organizationService.queryOrganizationByOrgID(orgID);
    }
//    修改信息
//    @RequestMapping("/profile/update")
//    public Result updateProfile(@RequestBody Organization organization)
//    {
//        try{
//            // TODO: 2019/7/4 updateOrganization实现
//            organizationService.updateOrganization(organization);
//            return new Result(true,"success","200");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new Result(false,e.getMessage(),"404");
//        }
//    }
//    修改密码
    // TODO: 2019/7/4  修改密码
}
