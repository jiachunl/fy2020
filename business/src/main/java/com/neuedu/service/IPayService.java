package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IPayService {
    ServerResponse pay(Long orderNo);
}
