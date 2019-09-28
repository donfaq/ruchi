package com.donfaq.ruchi.integration.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallpost {
    public Wallpost() {
    }

    /**
     * Access key to private object
     */
    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("attachments")
    private List<WallpostAttachment> attachments;

    /**
     * Date of publishing in Unixtime
     */
    @JsonProperty("date")
    private Integer date;

    /**
     * Date of editing in Unixtime
     */
    @JsonProperty("edited")
    private Integer edited;

    /**
     * Post author ID
     */
    @JsonProperty("from_id")
    private Integer fromId;

    /**
     * Post ID
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Is post archived, only for post owners
     */
    @JsonProperty("is_archived")
    private Boolean isArchived;

    /**
     * Information whether the post in favorites list
     */
    @JsonProperty("is_favorite")
    private Boolean isFavorite;

    /**
     * Wall owner's ID
     */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /**
     * Post signer ID
     */
    @JsonProperty("signer_id")
    private Integer signerId;

    /**
     * Post text
     */
    @JsonProperty("text")
    private String text;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public List<WallpostAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<WallpostAttachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getEdited() {
        return edited;
    }

    public void setEdited(Integer edited) {
        this.edited = edited;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getSignerId() {
        return signerId;
    }

    public void setSignerId(Integer signerId) {
        this.signerId = signerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Wallpost{" +
                "accessKey='" + accessKey + '\'' +
                ", attachments=" + attachments +
                ", date=" + date +
                ", edited=" + edited +
                ", fromId=" + fromId +
                ", id=" + id +
                ", isArchived=" + isArchived +
                ", isFavorite=" + isFavorite +
                ", ownerId=" + ownerId +
                ", signerId=" + signerId +
                ", text='" + text + '\'' +
                '}';
    }
}
