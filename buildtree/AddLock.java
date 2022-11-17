package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class AddLock implements Mutation{
    public ArrayList<Node> main(ArrayList<Node> list) throws CoreException, IOException {
        String path2 = "C:\\Users\\eclipse\\Desktop\\数据\\变异\\pthreadLock\\main.txt";
        Node node = BuildTree.getTree(path2);
        ArrayList<Node> lockNodes = new ArrayList<>();
        TreeUtils.getStatementNode(node, lockNodes);

        ArrayList<Node> reNodes = new ArrayList<>();

        for (int l = 0; l < list.size(); l++){
            ArrayList<Node> funNodeList = new ArrayList<>();
            TreeUtils.getFunNode(list.get(l), funNodeList);
            for (int i = 0; i < funNodeList.size(); i++) {
                ArrayList<Node> normalStmNode = new ArrayList<>();
                TreeUtils.getStatementNode(funNodeList.get(i), normalStmNode);
                for (int j = 0; j < normalStmNode.size(); j++) {
                    for (int k = j; k < normalStmNode.size(); k++) {
                        Node pNode = SerializationUtils.clone(list.get(l));
                        findFunNode(pNode,j, k,normalStmNode, funNodeList.get(i), lockNodes);
                        reNodes.add(pNode);
                    }
                }
            }
            System.out.println(reNodes.size());
        }
        System.out.println(reNodes.size());
        return reNodes;
    }

    public static void findFunNode(Node pNode,int j, int k,ArrayList<Node> normalStmNode, Node funNode, ArrayList<Node> nodes){
        List<Node> sonList = pNode.getSonList();
        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getId().equals(funNode.getId())){
                    addNode(sonList.get(i),j, k, normalStmNode, nodes);
                    break;
                }
                findFunNode(sonList.get(i),j, k, normalStmNode,funNode, nodes);
            }
        }

    }

    public static void addNode(Node sonNode, int j, int k, ArrayList<Node> normalStmNode, ArrayList<Node> nodes){
        List<Node> sonList = sonNode.getSonList();
        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {

                if(sonList.get(i).getId().equals(normalStmNode.get(j).getId())){
                    sonList.add(i, nodes.get(0));
                    i++;
                }
                if(sonList.get(i).getId().equals(normalStmNode.get(k).getId())){
                    i = i +1;
                    sonList.add(i, nodes.get(1));
                    break;
                }
                addNode(sonList.get(i), j, k, normalStmNode, nodes);
            }
        }
    }

}
