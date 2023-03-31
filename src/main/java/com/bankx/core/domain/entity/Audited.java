package com.bankx.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.LocalDateTime;


@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Audited implements Serializable {

    @Column(name="created_by")
    private String createdBy;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    private void setAuditInfo() {

        //fixme current user

        if(!StringUtils.hasText(createdBy)) {
            this.createdBy = "system";
        }

        if(!StringUtils.hasText(updatedBy)) {
            this.updatedBy = "system";
        }

        if(createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }

        if(updatedDate == null) {
            this.updatedDate = LocalDateTime.now();
        }
    }

}