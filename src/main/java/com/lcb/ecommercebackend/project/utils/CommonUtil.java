package com.lcb.ecommercebackend.project.utils;

import com.lcb.ecommercebackend.project.model.requests.ResultRequest;
import com.lcb.ecommercebackend.project.model.requests.WrapperRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CommonUtil {

    public <T> WrapperRequest<T> wrapToWrapperClass(T responseClass, ResultRequest resultRequest){
        WrapperRequest<T> wrapperRequest =  new WrapperRequest<>();
        wrapperRequest.setResult(resultRequest);
        wrapperRequest.setData(responseClass);
        return wrapperRequest;
    }

    public ZonedDateTime getCurrentDate(){
        return ZonedDateTime.now();
    }
}
