package com.example.demo.mapper;

import com.example.demo.dto.Progress;
import com.example.demo.entity.ProgressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ProgressMapper {
    ProgressMapper INSTANCE = Mappers.getMapper( ProgressMapper.class );
    Progress progressEntityToProgress(ProgressEntity progressEntity);
    ProgressEntity progressToProgressEntity(Progress progress);

}
