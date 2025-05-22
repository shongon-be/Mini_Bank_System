package com.shongon.audit_log.mapper;

import com.shongon.audit_log.dto.response.audit_log.AuditLogResponse;
import com.shongon.audit_log.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditLogMapper {

    @Mapping(target = "username", source = "user.username")
    AuditLogResponse toAuditLogResponse(AuditLog auditLog);
}
