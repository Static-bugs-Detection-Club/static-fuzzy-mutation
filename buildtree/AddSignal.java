package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddSignal implements Mutation {


    public ArrayList<Node> main(ArrayList<Node> list) throws IOException, CoreException  {
        String path1 = "D:\\eclipseproject\\新建文件夹\\eclipse-cdt-standalone-astparser-master\\src\\main\\tempfile\\Semaphore.txt";
        String path2 = "D:\\eclipseproject\\新建文件夹\\eclipse-cdt-standalone-astparser-master\\src\\main\\tempfile\\sem.txt";
        Node node = BuildTree.getTree(path1);
        Node semNode = BuildTree.getTree(path2);
        ArrayList<Node> semNodes = new ArrayList<>();
        TreeUtils.getStatementNode(semNode, semNodes);

        ArrayList<Node> reNode = new ArrayList<>();

        for (int k = 0; k < list.size(); k++) {
            ArrayList<Node> funNodeList = new ArrayList<>();
            TreeUtils.getFunNode(list.get(k), funNodeList);
            for (int i = 0; i < funNodeList.size(); i++) {
                ArrayList<Node> normalStmNode = new ArrayList<>();
                TreeUtils.getStatementNode(funNodeList.get(i), normalStmNode);
                for (int j = 0; j < normalStmNode.size(); j++) {
                    Node pNode = SerializationUtils.clone(list.get(k));
                    pNode.getSonList().add(0, node.getSonList().get(0));
                    findFunNode(pNode, j, normalStmNode, funNodeList.get(i), semNodes);
                    reNode.add(pNode);
                }
            }
        }
        System.out.println(reNode.size());
        return reNode;

    }

    public static void findFunNode(Node pNode, int j, ArrayList<Node> normalStmNode, Node funNode, ArrayList<Node> nodes) {
        List<Node> sonList = pNode.getSonList();
        if (sonList == null || sonList.size() == 0) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                if (sonList.get(i).getId().equals(funNode.getId())) {
                    addNode(sonList.get(i), j, normalStmNode, nodes);
                    break;
                }
                findFunNode(sonList.get(i), j, normalStmNode, funNode, nodes);
            }
        }

    }

    public static void addNode(Node sonNode, int j, ArrayList<Node> normalStmNode, ArrayList<Node> nodes){
        List<Node> sonList = sonNode.getSonList();
        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getId().equals(normalStmNode.get(j).getId())){
                    sonList.add(i, nodes.get(1));
                    i++;
                }
                if(sonList.get(i).getId().indexOf("free(") >= 0 && !sonList.get(i).getNodeName().equals("CASTCompoundStatement")){
                    i = i +1;
                    sonList.add(i, nodes.get(0));

                }
                addNode(sonList.get(i), j, normalStmNode, nodes);
            }
        }
    }

}
