package buildtree;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {

    @JSONField(serialize = false)
    private String id;          //结点id

    private String nodeName;        //结点名称

    private String nodeValue;

    private List<Node> sonList; //该结点的 子结点集合

    public Node(){}
    public Node(String id, String name, String value){
        this.id = id;
        this.nodeName = name;
        this.nodeValue = value;
    }

    public Node(Node node){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String name) {
        this.nodeName = name;
    }
    public String getNodeValue(){
        return nodeValue;
    }
    public void setNodeValue(String value){
        this.nodeValue = value;
    }

    public List<Node> getSonList() {
        return sonList;
    }

    public void setSonList(List<Node> sonList) {
        this.sonList = sonList;
    }
}
