package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductFrontController {

    @Autowired
    IProductService productService;
    /**
     * 产品搜索及动态排序List
     * @param categoryId 类别id
     * @param keyword 关键字
     * @param pageNum 第几页
     * @param pageSize 一页多少条
     * @param orderBy 按照字段排序
     */
    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(required = false,defaultValue = "-1")Integer categoryId,
                               @RequestParam(required = false,defaultValue = "")String keyword,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(required = false,defaultValue = "")String orderBy){
        return productService.list(categoryId, keyword, pageNum, pageSize, orderBy);
    }
}
