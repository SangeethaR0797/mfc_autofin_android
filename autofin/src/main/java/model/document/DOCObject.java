package model.document;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DOCObject
{

        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("files")
        @Expose
        private List<DocumentFile> files = null;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<DocumentFile> getFiles() {
            return files;
        }

        public void setFiles(List<DocumentFile> files) {
            this.files = files;
        }

}
