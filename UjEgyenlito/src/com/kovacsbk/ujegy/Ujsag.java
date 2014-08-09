package com.kovacsbk.ujegy;

public class Ujsag{
    int ID;
    String Title;
    int Pages;
    String ReleaseDate;
    String UploadDate;
    String CoverUri;

    public Ujsag(int ID,String Title,int Pages,String ReleaseDate,String UploadDate,String CoverUri){
    	this.ID = ID;
    	this.Title = Title;
    	this.Pages = Pages;
    	this.ReleaseDate = ReleaseDate;
    	this.UploadDate = UploadDate;
    	this.CoverUri = CoverUri;
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public int getPages() {
        return Pages;
    }
    
    public void setPages(int Pages) {
    	this.Pages = Pages;
    }
    
    public String getReleaseDate(){
    	return ReleaseDate;
    }
    
    public void setReleaseDate(String ReleaseDate){
    	this.ReleaseDate = ReleaseDate;
    }
    
    public String getUploadReleaseDate(){
    	return ReleaseDate;
    }
    
    public void setUploadDate(String UploadDate){
    	this.UploadDate = UploadDate;
    }
    
    public String getCoverUri(){
    	return CoverUri;
    }
    
    public void setCoverUri(String CoverUri){
    	this.CoverUri = CoverUri;
    }

}
