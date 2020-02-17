package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;

public interface IProductService {
    ServerResponse addOrUpdate(Product product);
    ServerResponse list(Integer categoryId,String keyword, Integer pageNum, Integer pageSize, String orderBy);

}
