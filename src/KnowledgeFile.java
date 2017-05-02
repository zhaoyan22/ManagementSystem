import java.io.Serializable;
import java.util.ArrayList;

 class KnowledgeFile implements Serializable{
	ArrayList<String> label=new ArrayList<String>();
	String content;
	public KnowledgeFile(String content,ArrayList<String> label){
		this.content=content;
		this.label=label;
	}
}

