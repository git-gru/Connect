package com.jarrebnnee.connect;

/**
 * Created by vi6 on 20-Jun-17.
 */

public class CatListGS {
   public String c_ar_title, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified, c_is_sub_category;
    boolean setCheckedBox;

    public CatListGS(String c_ar_title, String c_id, String c_images, String c_type, String c_title, String c_is_parent_id, String c_total_services, String c_created, String s_modified, String c_is_sub_category, boolean setCheckedBox) {
        this.c_ar_title = c_ar_title;
        this.c_id = c_id;
        this.c_images = c_images;
        this.c_type = c_type;
        this.c_title = c_title;
        this.c_is_parent_id = c_is_parent_id;
        this.c_total_services = c_total_services;
        this.c_created = c_created;
        this.s_modified = s_modified;
        this.c_is_sub_category = c_is_sub_category;
        this.setCheckedBox = setCheckedBox;
    }

    public String getC_ar_title() {
        return c_ar_title;
    }

    public void setC_ar_title(String c_ar_title) {
        this.c_ar_title = c_ar_title;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_images() {
        return c_images;
    }

    public void setC_images(String c_images) {
        this.c_images = c_images;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_is_parent_id() {
        return c_is_parent_id;
    }

    public void setC_is_parent_id(String c_is_parent_id) {
        this.c_is_parent_id = c_is_parent_id;
    }

    public String getC_total_services() {
        return c_total_services;
    }

    public void setC_total_services(String c_total_services) {
        this.c_total_services = c_total_services;
    }

    public String getC_created() {
        return c_created;
    }

    public void setC_created(String c_created) {
        this.c_created = c_created;
    }

    public String getS_modified() {
        return s_modified;
    }

    public void setS_modified(String s_modified) {
        this.s_modified = s_modified;
    }

    public String getC_is_sub_category() {
        return c_is_sub_category;
    }

    public void setC_is_sub_category(String c_is_sub_category) {
        this.c_is_sub_category = c_is_sub_category;
    }

    public boolean getetCheckedBox() {
        return setCheckedBox;
    }

    public void setCheckedBox(boolean setCheckedBox) {
        this.setCheckedBox = setCheckedBox;
    }
}
