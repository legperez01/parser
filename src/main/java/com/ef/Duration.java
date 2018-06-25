/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ef;

/**
 *
 * @author Luis
 */
public enum Duration {

    HOURLY("hourly"),
    DIALY("dialy");
    
    String duration;

    Duration(String duration) {
        this.duration = duration;
    }
    
    @Override
    public String toString(){
        return this.duration;
    }
   

}
