package com.entity;


import com.SystemException;
import com.api.persistence.db.orm.OrmBean;
import com.api.util.assistants.EqualityAssistant;
import com.api.util.assistants.HashCodeAssistant;


/**
 * Peer object that maps to the general_codes_group database table/view.
 *
 * @author auto generated.
 */
public class GeneralCodesGroup extends OrmBean {




	// Property name constants that belong to respective DataSource, GeneralCodesGroupView

/** The property name constant equivalent to property, CodeGrpId, of respective DataSource view. */
  public static final String PROP_CODEGRPID = "CodeGrpId";
/** The property name constant equivalent to property, Description, of respective DataSource view. */
  public static final String PROP_DESCRIPTION = "Description";
/** The property name constant equivalent to property, DateCreated, of respective DataSource view. */
  public static final String PROP_DATECREATED = "DateCreated";
/** The property name constant equivalent to property, DateUpdated, of respective DataSource view. */
  public static final String PROP_DATEUPDATED = "DateUpdated";
/** The property name constant equivalent to property, UserId, of respective DataSource view. */
  public static final String PROP_USERID = "UserId";
/** The property name constant equivalent to property, Permcol, of respective DataSource view. */
  public static final String PROP_PERMCOL = "Permcol";



	/** The javabean property equivalent of database column general_codes_group.code_grp_id */
  private int codeGrpId;
/** The javabean property equivalent of database column general_codes_group.description */
  private String description;
/** The javabean property equivalent of database column general_codes_group.date_created */
  private java.util.Date dateCreated;
/** The javabean property equivalent of database column general_codes_group.date_updated */
  private java.util.Date dateUpdated;
/** The javabean property equivalent of database column general_codes_group.user_id */
  private String userId;
/** The javabean property equivalent of database column general_codes_group.permcol */
  private String permcol;



	// Getter/Setter Methods

/**
 * Default constructor.
 */
  public GeneralCodesGroup() throws SystemException {
	super();
 }
/**
 * Sets the value of member variable codeGrpId
 */
  public void setCodeGrpId(int value) {
    this.codeGrpId = value;
  }
/**
 * Gets the value of member variable codeGrpId
 */
  public int getCodeGrpId() {
    return this.codeGrpId;
  }
/**
 * Sets the value of member variable description
 */
  public void setDescription(String value) {
    this.description = value;
  }
/**
 * Gets the value of member variable description
 */
  public String getDescription() {
    return this.description;
  }
/**
 * Sets the value of member variable dateCreated
 */
  public void setDateCreated(java.util.Date value) {
    this.dateCreated = value;
  }
/**
 * Gets the value of member variable dateCreated
 */
  public java.util.Date getDateCreated() {
    return this.dateCreated;
  }
/**
 * Sets the value of member variable dateUpdated
 */
  public void setDateUpdated(java.util.Date value) {
    this.dateUpdated = value;
  }
/**
 * Gets the value of member variable dateUpdated
 */
  public java.util.Date getDateUpdated() {
    return this.dateUpdated;
  }
/**
 * Sets the value of member variable userId
 */
  public void setUserId(String value) {
    this.userId = value;
  }
/**
 * Gets the value of member variable userId
 */
  public String getUserId() {
    return this.userId;
  }
/**
 * Sets the value of member variable permcol
 */
  public void setPermcol(String value) {
    this.permcol = value;
  }
/**
 * Gets the value of member variable permcol
 */
  public String getPermcol() {
    return this.permcol;
  }

@Override
public boolean equals(Object obj) {
   if (this == obj) {
      return true;
   }
   if (obj == null) {
      return false;
   }
   if (getClass() != obj.getClass()) {
      return false;
   }
   final GeneralCodesGroup other = (GeneralCodesGroup) obj; 
   if (EqualityAssistant.notEqual(this.codeGrpId, other.codeGrpId)) {
      return false;
   }
   if (EqualityAssistant.notEqual(this.description, other.description)) {
      return false;
   }
   if (EqualityAssistant.notEqual(this.permcol, other.permcol)) {
      return false;
   }
   return true; 
} 

@Override
public int hashCode() {
   return HashCodeAssistant.combineHashCodes(HashCodeAssistant.hashObject(this.codeGrpId),
               HashCodeAssistant.hashObject(this.description),
               HashCodeAssistant.hashObject(this.permcol));
} 

@Override
public String toString() {
   return "GeneralCodesGroup [codeGrpId=" + codeGrpId + 
          ", description=" + description + 
          ", permcol=" + permcol  + "]";
}

/**
 * Stubbed initialization method designed to implemented by developer.

 */
  public void initBean() throws SystemException {}
}