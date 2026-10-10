package com.bjb.uloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.AuditLog;

// save action via AuditLog
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}