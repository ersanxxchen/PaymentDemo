package com.example.demo.project.mapper;

import com.example.demo.project.domain.DO.BlockTransaction;
import com.example.demo.project.domain.DO.CardHolder;
import com.example.demo.project.domain.DO.NormalTransaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper {

    public int saveCardHolder(CardHolder cardHolder);

    public int saveBlockTransaction(BlockTransaction blockTransaction);

    public int deleteBlockTransaction(String transNo);

    public int saveNormalTransaction(NormalTransaction normalTransaction);

    public int updateNormalTransaction(NormalTransaction normalTransaction);
}
