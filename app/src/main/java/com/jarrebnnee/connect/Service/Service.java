package com.jarrebnnee.connect.Service;

/**
 * Created by Vardhman Infonet 4 on 24-Mar-17.
 */

public class Service {

        String code = null;
        String name = null;
        boolean selected = false;

        public Service(String code, String name, boolean selected) {
            super();
            this.code = code;
            this.name = name;
            this.selected = selected;
        }

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }


}
