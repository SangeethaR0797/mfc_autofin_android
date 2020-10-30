package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {

        @SerializedName("all")
        @Expose
        private Integer all;
        @SerializedName("open")
        @Expose
        private Integer open;
        @SerializedName("bank")
        @Expose
        private Integer bank;
        @SerializedName("closed")
        @Expose
        private Integer closed;

        public Integer getAll() {
            return all;
        }

        public void setAll(Integer all) {
            this.all = all;
        }

        public Integer getOpen() {
            return open;
        }

        public void setOpen(Integer open) {
            this.open = open;
        }

        public Integer getBank() {
            return bank;
        }

        public void setBank(Integer bank) {
            this.bank = bank;
        }

        public Integer getClosed() {
            return closed;
        }

        public void setClosed(Integer closed) {
            this.closed = closed;
        }

    }

