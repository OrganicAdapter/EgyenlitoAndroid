package com.kovacsbk.ujegy;

public class Cikk {
	int ID;
	String Title;
	String Author;
	String PDFUri;
	int UjsID;
	
	public Cikk(int ID,String Title,String Author,String PDFUri,int UjsID){
    	this.ID = ID;
    	this.Title = Title;
    	this.Author = Author;
    	this.PDFUri = PDFUri;
    	this.UjsID = UjsID;
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

    public String getAuthor() {
        return Author;
    }
    
    public void setAuthor(String Author) {
    	this.Author = Author;
    }
    
    public String getPDFUri(){
    	return PDFUri;
    }
    
    public void setPDFUri(String PDFUri){
    	this.PDFUri = PDFUri;
    }
    
    public int getUjsID(){
    	return UjsID;
    }
    
    public void setUjsID(int UjsID){
    	this.UjsID = UjsID;
    }
}
