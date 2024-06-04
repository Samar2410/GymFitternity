/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.samarvir.GymFitternity.model;

/**
 *
 * @author samar
 */
public class Photo {
    public int id;
   public String path;
   public int gymid;

    public Photo(int id, String path, int gymid) {
        this.id = id;
        this.path = path;
        this.gymid = gymid;
    }
    
    
}
