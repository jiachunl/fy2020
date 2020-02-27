package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping) {
        int count = shippingMapper.insert(shipping);
        if (count <= 0){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDDRESS_ADD_FAIL.getStatus(),StatusEnum.ADDDRESS_ADD_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySuccess(null,shipping.getId());
    }
}
