package com.bit.code.assemble.service;

import com.bit.code.assemble.para.Table;
import com.bit.code.config.datasource.DataMappingHolder;
import com.bit.code.config.mapping.SystemConfig;

import java.sql.Connection;
import java.util.List;

public interface VariableParaService {

    public List<Table> wrapTablePara(DataMappingHolder dataMappingHolder);

    public List<Table> wrapTablePara(SystemConfig systemConfig);

    public List<Table> wrapTablePara(String sqlType, Connection connection, DataMappingHolder dataMappingHolder);
}
