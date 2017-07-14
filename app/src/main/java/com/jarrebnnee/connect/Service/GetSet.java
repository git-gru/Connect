package com.jarrebnnee.connect.Service;

/**
 * Created by Vardhman Infonet 4 on 24-Feb-17.
 */

public class GetSet {

    public static GetSet instance;

    String u_fname,u_lname,c_id,c_name,js_id,js_title,jsr_id,jsr_posted_user_id,search;

    public static GetSet getInstance() {

        if (instance == null) {
            instance = new GetSet();
        }
        return instance;

    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getU_fname() {
        return u_fname;
    }

    public void setU_fname(String u_fname) {
        this.u_fname = u_fname;
    }

    public String getU_lname() {
        return u_lname;
    }

    public void setU_lname(String u_lname) {
        this.u_lname = u_lname;
    }

    public String getc_id() {
        return c_id;
    }

    public void setc_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getsubc_id() {
        return c_id;
    }

    public void setsubc_name(String c_name) {
        this.c_name = c_name;
    }
    public String getsubc_name() {
        return c_name;
    }

    public void setsubc_id(String c_id) {
        this.c_id = c_id;
    }

    public String getjs_id() {
        return js_id;
    }

    public void setjs_id(String js_id) {
        this.js_id = js_id;
    }
    public String getjs_title() {
        return js_title;
    }

    public void setjs_title(String js_title) {
        this.js_title = js_title;
    }

    public String getjsr_id() {
        return jsr_id;
    }

    public void setjsr_id(String jsr_id) {
        this.jsr_id = jsr_id;
    }

    public String getjsr_posted_user_id() {
        return jsr_posted_user_id;
    }

    public void setjsr_posted_user_id(String jsr_posted_user_id) {
        this.jsr_posted_user_id = jsr_posted_user_id;
    }



}
