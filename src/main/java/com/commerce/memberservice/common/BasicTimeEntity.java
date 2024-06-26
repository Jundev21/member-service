package com.commerce.memberservice.common;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

// audition 사용
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BasicTimeEntity {
	@CreatedDate
	@Column(name = "createdDate")
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(name = "modifiedDate")
	private LocalDateTime modifiedDate;
}
