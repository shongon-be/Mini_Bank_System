package com.shongon.mini_bank.dto.response.audit_log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.mini_bank.utils.CustomTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLogResponse {
    Long logId;

    String username;

    String action;
    String entityName;
    Long entityId;
    String details;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime timestamp;
}
