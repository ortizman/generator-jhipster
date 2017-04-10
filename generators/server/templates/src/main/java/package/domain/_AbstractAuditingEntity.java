package <%=packageName%>.domain;

import java.io.Serializable;

<% if (databaseType == 'sql') { %>
import org.hibernate.envers.Audited;
import <%=packageName%>.config.audit.EntityAuditEventListener;<% } %>
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
<% if (databaseType == 'mongodb') { %>import org.springframework.data.mongodb.core.mapping.Field;
import java.time.ZonedDateTime;
<% } %><% if (databaseType == 'sql') { %>
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;<% } %>

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */<% if (databaseType == 'sql') { %>
@MappedSuperclass
@Audited
@EntityListeners({AuditingEntityListener.class, EntityAuditEventListener.class})<% } %>
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy<% if (databaseType == 'sql') { %>
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)<% } %><% if (databaseType == 'mongodb') { %>
    @Field("created_by")<% } %>
    private String createdBy;

    @CreatedDate<% if (databaseType == 'sql') { %>
    @Column(name = "created_date", nullable = false)<% } %><% if (databaseType == 'mongodb') { %>
    @Field("created_date")<% } %>
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @LastModifiedBy<% if (databaseType == 'sql') { %>
    @Column(name = "last_modified_by", length = 50)<% } %><% if (databaseType == 'mongodb') { %>
    @Field("last_modified_by")<% } %>
    private String lastModifiedBy;

    @LastModifiedDate<% if (databaseType == 'sql') { %>
    @Column(name = "last_modified_date")<% } %><% if (databaseType == 'mongodb') { %>
    @Field("last_modified_date")<% } %>
    private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
