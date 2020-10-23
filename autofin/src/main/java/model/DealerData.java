package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerData
{

        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("TabName")
        @Expose
        private String tabName;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTabName() {
            return tabName;
        }

        public void setTabName(String tabName) {
            this.tabName = tabName;
        }

    }


