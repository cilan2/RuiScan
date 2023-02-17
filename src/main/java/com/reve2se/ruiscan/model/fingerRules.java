package com.reve2se.ruiscan.model;


public class fingerRules {
    public String shiroRules = new String(" && (header=\"rememberme=deleteMe\" || header=\"shiroCookie\")");
    public String fastjsonRules = new String("");
    public String ueditorRules = new String(" && app=\"百度-UEditor\" && server=\"Microsoft-IIS/7.5\"");
    public String springbootRules = new String(" && app=\"vmware-SpringBoot-framework\"");
    public String weaverRules = new String(" && (header=\"Set-Cookie: ecology_JSessionId=\" || app=\"泛微-EMobile\")");
    public String seeyonRules = new String(" && (app=\"用友-致远OA\" && title =\"A8\" || app=\"致远互联-A8-V5\" || app=\"致远互联-OA\")");
    public String yonyouRules = new String(" && app=\"用友-UFIDA-NC\"");
    public String tongdaRules = new String(" && app=\"TDXK-通达OA\"");
    public String kingdeeRules = new String(" && (body=\"easSessionId\" || body=\"/kdgs/script/kdgs.js\")");


}
