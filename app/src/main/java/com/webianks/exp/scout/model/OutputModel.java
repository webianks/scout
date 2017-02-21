package com.webianks.exp.scout.model;

/**
 * Created by R Ankit on 21-02-2017.
 */

public class OutputModel {

    private String from = "Any";
    private String to = "Any";
    private String toDate = "Any";
    private String fromDate = "Any";
    private String hasAttachments = "Any";
    private String attachmentType = "Any";
    private String attachmentSize = "Any";
    private String attachmentName = "Any";
    private String subject = "Any";
    private String cc = "Any";

    //setter methods

    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }
    public void setToDate(String toDate){
        this.toDate = toDate;
    }
    public void setFromDate(String fromDate){
        this.fromDate = fromDate;
    }
    public void setHasAttachments(String hasAttachments){
        this.hasAttachments = hasAttachments;
    }
    public void setAttachmentType(String attachmentType){
        this.attachmentType = attachmentType;
    }
    public void setAttachmentSize(String attachmentSize){
        this.attachmentSize = attachmentSize;
    }
    public void setAttachmentName(String attachmentName){
        this.attachmentName = attachmentName;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setCC(String cc){
        this.cc = cc;
    }

    //getter methods

    public String getFrom(){
        return from;
    }

    public String getTo(){
        return to;
    }
    public String getToDate(){
        return toDate;
    }
    public String getFromDate(){
        return fromDate;
    }
    public String hasAttachments(){
        return hasAttachments;
    }
    public String getAttachmentType(){
        return attachmentType;
    }
    public String getAttachmentSize(){
        return attachmentSize;
    }
    public String getAttachmentName(){
        return attachmentName;
    }
    public String getSubject(){
        return subject;
    }
    public String getCC(){
        return cc;
    }

}
