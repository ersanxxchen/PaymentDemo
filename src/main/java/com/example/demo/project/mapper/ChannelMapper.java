package com.example.demo.project.mapper;

import com.example.demo.project.domain.DO.Channel;
import com.example.demo.project.domain.DO.ChannelPayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChannelMapper {

    public Channel queryChannel(@Param("channelId") Integer channelId);

    public List<ChannelPayInfo> queryChannelPayInfo(@Param("channelId") Integer channelId);

}
