package com.example.demo.project.mapper;

import com.example.demo.project.domain.DO.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChannelMapper {

    public Channel queryChannel(@Param("channelId") Integer channelId);
}
