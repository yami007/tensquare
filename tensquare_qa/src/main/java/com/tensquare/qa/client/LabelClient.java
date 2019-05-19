package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LableClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tensquare-base",fallback = LableClientImpl.class,path = "/label")
public interface LabelClient {

    /**
     * 根据id查询label
     *
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.GET)
    public Result getById(@PathVariable("labelId") String labelId);
}
