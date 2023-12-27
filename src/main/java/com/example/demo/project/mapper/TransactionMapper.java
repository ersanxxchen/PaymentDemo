package com.example.demo.project.mapper;

import com.example.demo.project.domain.DO.BlockTransaction;
import com.example.demo.project.domain.DO.CardHolder;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.DO.TransTotal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionMapper {

    public int saveCardHolder(CardHolder cardHolder);

    public int saveBlockTransaction(BlockTransaction blockTransaction);

    public int deleteBlockTransaction(String transNo);

    public int saveNormalTransaction(NormalTransaction normalTransaction);

    public int updateNormalTransaction(NormalTransaction normalTransaction);

    public NormalTransaction queryNormalTransaction(String transNo);

    public List<NormalTransaction> queryUnNoticeTransaction();

    public TransTotal queryTransTotal(@Param("payName") String payName, @Param("payEmail")String payEmail, @Param("payImageUrl")String payImageUrl,
                                      @Param("start")String start, @Param("end")String end);

    public int updateVenmoTransaction(NormalTransaction normalTransaction);


    public int updateVenmoPayId(@Param("transNo") String transNo,@Param("payId") String payId);


    public int updateNotice(NormalTransaction normalTransaction);
}
