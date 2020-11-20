package model.document;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DocData
{
        @SerializedName("docs")
        @Expose
        private List<DOCObject> docs = null;

        public List<DOCObject> getDocs() {
            return docs;
        }

        public void setDocs(List<DOCObject> docs) {
            this.docs = docs;
        }


}
