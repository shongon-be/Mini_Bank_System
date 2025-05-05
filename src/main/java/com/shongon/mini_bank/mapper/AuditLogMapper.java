package com.shongon.mini_bank.mapper;

import com.shongon.mini_bank.dto.response.audit_log.AuditLogResponse;
import com.shongon.mini_bank.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditLogMapper {

    @Mapping(target = "username", source = "user.username")
    AuditLogResponse toAuditLogResponse(AuditLog auditLog);
}
