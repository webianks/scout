/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scout;

/**
 *
 * @author R Ankit
 */
public class SearchCriteria {
     
    private String from;
    private String to;
    private String toDate;
    private String fromDate;
    private boolean hasAttachments;
    private String attachmentType;
    private String attachmentSize;
    private String attachmentName;
    private String subject;
    private String cc;
    
    
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
   public void setHasAttachments(boolean hasAttachments){
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
   public boolean hasAttachments(){
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
