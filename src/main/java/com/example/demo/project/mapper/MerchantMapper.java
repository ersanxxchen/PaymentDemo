package com.example.demo.project.mapper;

import com.example.demo.project.domain.DO.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MerchantMapper {

    public Merchant queryMerchant(@Param("merchantId") Integer merchantId);

}
